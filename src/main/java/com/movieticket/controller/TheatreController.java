package com.movieticket.controller;

import com.movieticket.model.Theatre;
import com.movieticket.service.TheatreService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theatres")
@RequiredArgsConstructor
public class TheatreController {
    private final TheatreService service;

    @GetMapping public List<Theatre> all(){ return service.all(); }
    @GetMapping("/{id}") public Theatre get(@PathVariable Long id){
         return service.get(id); 
        
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping public Theatre create(@RequestBody Theatre t){
         return service.create(t); 
        
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}") public Theatre update(@PathVariable Long id, @RequestBody Theatre t){
         return service.update(id, t); 
        
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id){
         service.delete(id); 
        
    }
}
