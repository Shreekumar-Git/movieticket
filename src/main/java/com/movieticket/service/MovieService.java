package com.movieticket.service;

import com.movieticket.model.Movie;
import com.movieticket.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@SuppressWarnings("null")
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository repo;
    public List<Movie> all(){ return repo.findAll(); }
    public Movie get(Long id){ return repo.findById(id).orElseThrow(); }
    public Movie create(Movie m){ return repo.save(m); }
    public Movie update(Long id, Movie m){ m.setId(id); return repo.save(m); }
    public void delete(Long id){ repo.deleteById(id); }
}
