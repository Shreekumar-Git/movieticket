package com.movieticket.service;

import com.movieticket.model.*;
import com.movieticket.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;




@Service
@Transactional
@SuppressWarnings("null")
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepo;
    private final UserRepository userRepo;
    private final ShowRepository showRepo;
    private final ShowSeatRepository seatRepo;
    private final EmailService emailService;
    

    @Transactional
    public Booking createBooking(Long userId, Long showId, List<String> seats) {
    User user = userRepo.findById(userId).orElseThrow();
    Show show = showRepo.findById(showId).orElseThrow();

    // pessimistic lock selected seats
    List<ShowSeat> lock = seatRepo.lockSeats(showId, seats);
    if (lock.size() != seats.size())
        throw new IllegalStateException("Some seats do not exist");

    boolean allAvailable = lock.stream().allMatch(s -> s.getStatus() == SeatStatus.AVAILABLE);
    if (!allAvailable)
        throw new IllegalStateException("One or more seats already booked.");

    // mark booked
    lock.forEach(s -> s.setStatus(SeatStatus.BOOKED));
    seatRepo.saveAll(lock);

    double total = show.getPricePerSeat() * seats.size();

    // ✅ sort the seat numbers safely before saving
    List<Integer> sortedSeats = seats.stream()
            .map(Integer::parseInt)
            .sorted()
            .toList();
    String seatNumbersStr = sortedSeats.stream()
            .map(String::valueOf)
            .collect(Collectors.joining(","));

    // ✅ build booking using sorted seat numbers
    Booking booking = Booking.builder()
            .user(user)
            .show(show)
            .seatNumbers(seatNumbersStr)
            .totalAmount(total)
            .status(BookingStatus.ACTIVE)
            .paymentStatus(PaymentStatus.SUCCESS)
            .createdAt(LocalDateTime.now())
            .build();

    Booking saved = bookingRepo.save(booking);

    // ✅ confirmation email
    emailService.send(
            user.getEmail(),
            "Booking Created",
            "Your booking (ID: " + saved.getId() + ") for " + show.getMovie().getTitle() +
                    " at " + show.getTheatre().getName() + " seats: " + saved.getSeatNumbers()
    );

    return saved;
}

    @Transactional
    public Booking markPaid(Long bookingId){
        Booking b = bookingRepo.findById(bookingId).orElseThrow();
        b.setPaymentStatus(PaymentStatus.SUCCESS);
        b.setStatus(BookingStatus.ACTIVE);
        Booking saved = bookingRepo.save(b);
        emailService.send(b.getUser().getEmail(), "Payment Success",
                "Booking #" + saved.getId() + " paid. Seats: " + saved.getSeatNumbers());
        return saved;
    }

    public Booking getBooking(Long id) {
    return bookingRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
}



@Transactional
public boolean cancelBooking(Long id) {
    Booking booking = bookingRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found"));

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime showTime = booking.getShow().getStartTime();

    if (now.isBefore(showTime.minusHours(1))) {
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setPaymentStatus(PaymentStatus.REFUNDED);
        bookingRepo.save(booking);

        // ✅ Split seat numbers and release only those
        List<String> seatNumbers = Arrays.asList(booking.getSeatNumbers().split(","));
        seatRepo.releaseSpecificSeats(booking.getShow().getId(), seatNumbers);

        emailService.send(
                booking.getUser().getEmail(),
                "Booking Cancelled",
                "Your booking #" + booking.getId() + " for movie " +
                        booking.getShow().getMovie().getTitle() +
                        " has been cancelled successfully."
        );

        return true;
    } else {
        throw new IllegalStateException("Cannot cancel within 1 hour of showtime.");
    }
}




    public List<Booking> history(Long userId){
        User user = userRepo.findById(userId).orElseThrow();
        return bookingRepo.findByUser(user);
    }
}
