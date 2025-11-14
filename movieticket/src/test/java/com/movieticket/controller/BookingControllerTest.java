package com.movieticket.controller;

import com.movieticket.config.GlobalControllerAdvice;
import com.movieticket.repository.UserRepository;
import com.movieticket.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    // Prevent ApplicationContext crash
    @MockBean
    private GlobalControllerAdvice advice;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testCancelBookingSuccess() throws Exception {
        when(bookingService.cancelBooking(1L)).thenReturn(true);

        mockMvc.perform(post("/api/bookings/booking/cancel/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/booking/history"));
    }

    @Test
    void testCancelBookingFailure() throws Exception {
        when(bookingService.cancelBooking(1L)).thenThrow(new IllegalStateException("Too late"));

        mockMvc.perform(post("/api/bookings/booking/cancel/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/booking/history"));
    }
}
