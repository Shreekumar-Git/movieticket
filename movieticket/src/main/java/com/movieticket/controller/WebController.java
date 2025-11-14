package com.movieticket.controller;

import com.movieticket.model.*;
import com.movieticket.repository.UserRepository;
import com.movieticket.repository.MovieRepository;
import com.movieticket.service.BookingService;
import com.movieticket.service.MovieService;
import com.movieticket.service.ShowService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final MovieService movieService;
    private final ShowService showService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BookingService bookingService;
    private final MovieRepository movieRepository;

   @GetMapping("/")
    public String home(@RequestParam(required = false) String search, Model model) {
        List<Movie> movies;
        if (search != null && !search.trim().isEmpty()) {
            movies = movieRepository.findByTitleContainingIgnoreCaseOrGenreContainingIgnoreCase(search, search);
        } else {
            movies = movieRepository.findAll();
        }
        model.addAttribute("movies", movies);
        return "home";
    }


    @GetMapping("/movies/{id}")
    public String movie(@PathVariable Long id, Model model){
        Movie m = movieService.get(id);
        model.addAttribute("movie", m);
        model.addAttribute("shows", showService.all().stream().filter(s -> s.getMovie().getId().equals(id)).toList());
        return "movie";
    }

    @GetMapping("/shows/{id}/seats")
    public String seats(@PathVariable Long id, Model model){
        model.addAttribute("showId", id);
        model.addAttribute("seats", showService.seats(id));
        return "seats";
    }

    @PostMapping("/book")
    public String book(@RequestParam Long showId, @RequestParam String seats, Principal principal, Model model){
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        List<String> list = Arrays.stream(seats.split(",")).map(String::trim).filter(s->!s.isEmpty()).toList();
        Booking booking = bookingService.createBooking(user.getId(), showId, list);
        model.addAttribute("booking", booking);
        return "booking_success";
    }

    @GetMapping("/login")
    public String login(){ return "login"; }

    @GetMapping("/register")
    public String registerForm(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) user.setRole(Role.USER);
        userRepository.save(user);
        return "redirect:/login?registered";
    }
    @GetMapping("/booking/history")
    public String bookingHistory(Model model, Principal principal) {
    var user = userRepository.findByEmail(principal.getName()).orElseThrow();
    model.addAttribute("bookings", bookingService.history(user.getId()));
    return "booking_history";
}



    @GetMapping("/booking/success/{id}")
    public String bookingSuccess(@PathVariable Long id, Model model){
        model.addAttribute("bookingId", id);
        return "booking_success";
    }
}
