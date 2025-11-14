package com.movieticket.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@SuppressWarnings("null")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"show_id","seatNumber"}))
public class ShowSeat {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;  // ✅ linked back to Show

    private String seatNumber; // e.g. "A1", "A2"... or simple numeric "1"

    private boolean available; 

    @Enumerated(EnumType.STRING)
    private SeatStatus status;
}
