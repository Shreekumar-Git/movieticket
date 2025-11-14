package com.movieticket.repository;

import com.movieticket.model.Movie;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> { 
    List<Movie> findByTitleContainingIgnoreCaseOrGenreContainingIgnoreCase(String title, String genre);

}
