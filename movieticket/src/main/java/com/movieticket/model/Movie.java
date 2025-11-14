package com.movieticket.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@SuppressWarnings("null")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Movie {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String genre;
    private int durationMinutes;
    private String rating;
    private String posterUrl;
}
