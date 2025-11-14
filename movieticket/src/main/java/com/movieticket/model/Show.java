package com.movieticket.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Data
@SuppressWarnings("null")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name="shows")
public class Show {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Movie movie;

    @ManyToOne
    private Theatre theatre;

    private LocalDateTime startTime;

    private double pricePerSeat;

    // ✅ Cascade delete bookings
    @Builder.Default
    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    // ✅ Cascade delete seats linked to this show
    @Builder.Default
    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShowSeat> seats = new ArrayList<>();
}
