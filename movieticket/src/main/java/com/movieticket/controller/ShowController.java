package com.movieticket.controller;

import com.movieticket.model.Show;
import com.movieticket.model.ShowSeat;
import com.movieticket.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shows")
@RequiredArgsConstructor
public class ShowController {
    private final ShowService service;

    @GetMapping 
    public List<Show> all(){ 
        return service.all(); 
    }
    
    @GetMapping("/{id}") 
    public Show get(@PathVariable Long id){ 
        return service.get(id); 
    }
    @GetMapping("/{id}/seats") 
    public List<ShowSeat> seats(@PathVariable Long id){
         return service.seats(id); 
        }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping 
    public Show create(@RequestBody Show show){ 
        return service.create(show); 
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}") 
    public Show update(@PathVariable Long id, @RequestBody Show show){ 
        return service.update(id, show);
     }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}") 
    public void delete(@PathVariable Long id){
         service.delete(id); 
        }
}
