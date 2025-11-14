package com.movieticket.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@SuppressWarnings("null")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Theatre {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private int seatCount;
}
