package com.movieticket.controller;

import com.movieticket.model.Role;
import com.movieticket.model.User;
import com.movieticket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @PostMapping("/register")
    public User register(@RequestBody User user){
        user.setPassword(encoder.encode(user.getPassword()));
        if(user.getRole() == null) user.setRole(Role.USER);
        return userRepository.save(user);
    }
}
