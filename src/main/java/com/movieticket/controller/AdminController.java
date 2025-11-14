package com.movieticket.controller;

import com.movieticket.model.Movie;
import com.movieticket.model.Show;
import com.movieticket.model.Theatre;
import com.movieticket.repository.MovieRepository;
import com.movieticket.repository.TheatreRepository;
import com.movieticket.repository.BookingRepository;
import com.movieticket.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@SuppressWarnings("null")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final MovieRepository movieRepo;
    private final TheatreRepository theatreRepo;
    private final ShowService showService;
    private final BookingRepository bookingRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("movies", movieRepo.findAll());
        model.addAttribute("theatres", theatreRepo.findAll());
        model.addAttribute("shows", showService.getAllShows());
        return "admin";
    }

    @PostMapping("/movies/add")
    public String addMovie(Movie movie) {
        movieRepo.save(movie);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/movies/delete/{id}")
    public String deleteMovie(@PathVariable Long id) {
        movieRepo.deleteById(id);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/theatres/add")
    public String addTheatre(Theatre theatre) {
        theatreRepo.save(theatre);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/theatres/delete/{id}")
    public String deleteTheatre(@PathVariable Long id) {
        theatreRepo.deleteById(id);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/shows/add")
    public String addShow(@RequestParam Long movieId,
                          @RequestParam Long theatreId,
                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
                          @RequestParam double pricePerSeat) {
        Movie movie = movieRepo.findById(movieId).orElseThrow();
        Theatre theatre = theatreRepo.findById(theatreId).orElseThrow();
        Show show = Show.builder().movie(movie).theatre(theatre).startTime(startTime).pricePerSeat(pricePerSeat).build();
        showService.create(show);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/shows/delete/{id}")
public String deleteShow(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    if (bookingRepository.existsByShowId(id)) {
        redirectAttributes.addFlashAttribute("error", "Cannot delete show with existing bookings!");
        return "redirect:/admin/dashboard";
    }

    showService.deleteById(id);
    redirectAttributes.addFlashAttribute("success", "Show deleted successfully!");
    return "redirect:/admin/dashboard";
}
}

