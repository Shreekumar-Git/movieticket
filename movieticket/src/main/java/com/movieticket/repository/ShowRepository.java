package com.movieticket.repository;

import com.movieticket.model.Show;
import com.movieticket.model.Theatre;
import com.movieticket.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByMovie(Movie movie);
    List<Show> findByTheatre(Theatre theatre);
    List<Show> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
}
