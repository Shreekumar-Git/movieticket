package com.movieticket.service;

import com.movieticket.model.*;
import com.movieticket.repository.BookingRepository;
import com.movieticket.repository.ShowSeatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepo;

    @Mock
    private ShowSeatRepository seatRepo;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void testCancelBookingSuccess() {

        // ⭐ Mock User
        User user = new User();
        user.setId(10L);
        user.setEmail("test@example.com");

        // ⭐ Mock Movie
        Movie movie = new Movie();
        movie.setId(99L);
        movie.setTitle("Mock Movie");

        // ⭐ Mock Show
        Show show = new Show();
        show.setId(5L);
        show.setStartTime(LocalDateTime.now().plusHours(3));
        show.setMovie(movie);  // 🔥 REQUIRED (Fixes your error)

        // ⭐ Mock Booking
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setShow(show);
        booking.setSeatNumbers("A1,A2");
        booking.setStatus(BookingStatus.ACTIVE);
        booking.setUser(user);

        when(bookingRepo.findById(1L)).thenReturn(Optional.of(booking));

        // Execute
        boolean result = bookingService.cancelBooking(1L);

        // Verify actions
        assertTrue(result);

        verify(bookingRepo).save(booking);
        verify(seatRepo).releaseSpecificSeats(eq(show.getId()), anyList());
        verify(emailService).send(eq("test@example.com"), anyString(), contains("Mock Movie"));
    }
}
