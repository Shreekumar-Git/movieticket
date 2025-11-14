package com.movieticket.repository;

import com.movieticket.model.Booking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test") // use H2
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void testSaveBooking() {

        Booking booking = new Booking();
        booking.setSeatNumbers("A1,A2");  // <-- Correct setter

        Booking saved = bookingRepository.save(booking);

        assertNotNull(saved.getId());
        assertEquals("A1,A2", saved.getSeatNumbers());
    }
}
