package com.movieticket.config;

import com.movieticket.model.*;
import com.movieticket.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.stream.IntStream;
@SuppressWarnings("null")
@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner init(UserRepository users, MovieRepository movies, TheatreRepository theatres,
                           ShowRepository shows, ShowSeatRepository showSeats) {
        return args -> {
            if (users.findByEmail("admin@movietix.local").isEmpty()) {
                users.save(User.builder()
                        .name("Admin")
                        .email("admin@movietix.local")
                        .password(passwordEncoder.encode("admin123"))
                        .role(Role.ADMIN)
                        .build());
            }
            if (users.findByEmail("user@movietix.local").isEmpty()) {
                users.save(User.builder()
                        .name("Demo User")
                        .email("user@movietix.local")
                        .password(passwordEncoder.encode("user123"))
                        .role(Role.USER)
                        .build());
            }

            if (movies.count() == 0) {
                Movie m1 = movies.save(Movie.builder().title("Interstellar").genre("Sci-Fi").durationMinutes(169).rating("PG-13").posterUrl("https://i.imgur.com/EuKQX4F.jpeg").build());
                Movie m2 = movies.save(Movie.builder().title("Inception").genre("Action").durationMinutes(148).rating("PG-13").posterUrl("https://i.imgur.com/6W0Z7sE.jpeg").build());

                Theatre t1 = theatres.save(Theatre.builder().name("Grand Cinema").location("City Center").seatCount(30).build());
                Theatre t2 = theatres.save(Theatre.builder().name("Sunset Screens").location("West End").seatCount(20).build());

                Show s1 = shows.save(Show.builder().movie(m1).theatre(t1).startTime(LocalDateTime.now().plusDays(1).withHour(18).withMinute(0)).pricePerSeat(200).build());
                Show s2 = shows.save(Show.builder().movie(m2).theatre(t2).startTime(LocalDateTime.now().plusDays(1).withHour(21).withMinute(0)).pricePerSeat(250).build());

                // generate seats
                for (Show s : new Show[]{s1, s2}) {
                    int count = s.getTheatre().getSeatCount();
                    IntStream.rangeClosed(1, count).forEach(i -> {
                        ShowSeat seat = ShowSeat.builder().show(s).seatNumber(String.valueOf(i)).status(SeatStatus.AVAILABLE).build();
                        showSeats.save(seat);
                    });
                }
            }
        };
    }
}
