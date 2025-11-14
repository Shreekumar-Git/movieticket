package com.movieticket.config;

import com.movieticket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final UserRepository userRepository;

    @ModelAttribute
    public void addUserInfo(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            String email = null;

            if (principal instanceof UserDetails userDetails) {
                email = userDetails.getUsername();
            } else if (principal instanceof String str && !str.equals("anonymousUser")) {
                email = str;
            }

            if (email != null) {
                userRepository.findByEmail(email).ifPresent(user -> {
                    model.addAttribute("currentUserEmail", user.getEmail());
                    model.addAttribute("currentUserRole", user.getRole().name()); // Convert enum to string
                });
            }
        }
    }
}
