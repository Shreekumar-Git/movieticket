package com.movieticket.controller;

import com.movieticket.model.Booking;
import com.movieticket.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")

@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    @PostMapping
    public Booking create(@RequestBody Map<String, Object> payload){
        Long userId = Long.valueOf(payload.get("userId").toString());
        Long showId = Long.valueOf(payload.get("showId").toString());
        @SuppressWarnings("unchecked")
        List<String> seats = (List<String>) payload.get("seats");
        return service.createBooking(userId, showId, seats);
    }
   

    @PostMapping("/{id}/pay")
    public Booking pay(@PathVariable Long id){
        service.markPaid(id);
        return service.getBooking(id);
        
        
    }

@PostMapping("/booking/cancel/{id}")
public ModelAndView cancelBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    System.out.println("🔹 Cancel request received for booking ID: " + id);
    try {
        boolean cancelled = service.cancelBooking(id);
        if (cancelled)
            redirectAttributes.addFlashAttribute("success", "Booking cancelled successfully!");
        else
            redirectAttributes.addFlashAttribute("error", "Cannot cancel this booking (less than 1 hour left).");
    } catch (IllegalStateException e) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
    }
    return new ModelAndView("redirect:/booking/history");
}





}
