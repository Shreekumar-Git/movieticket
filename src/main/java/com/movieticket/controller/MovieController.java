package com.movieticket.controller;

import com.movieticket.model.Movie;
import com.movieticket.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService service;

    @GetMapping public List<Movie> all(){ 
        return service.all();
     }
    
     @GetMapping("/{id}") public Movie get(@PathVariable Long id){ 
        return service.get(id); 
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping public Movie create(@RequestBody Movie m){
         return service.create(m); 
        
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}") public Movie update(@PathVariable Long id, @RequestBody Movie m){
         return service.update(id, m); 
        
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id){
         service.delete(id); 
        
    }
}
