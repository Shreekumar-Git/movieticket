package com.movieticket.service;

import com.movieticket.model.*;
import com.movieticket.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@SuppressWarnings("null")
@RequiredArgsConstructor

public class ShowService {
    private final ShowRepository showRepo;
    private final ShowSeatRepository seatRepo;

    public List<Show> all(){ return showRepo.findAll(); }
    public Show get(Long id){ return showRepo.findById(id).orElseThrow(); }

    @Transactional
    public Show create(Show s){
        Show saved = showRepo.save(s);
        // auto create seats based on theatre seatCount
        int count = saved.getTheatre().getSeatCount();
        IntStream.rangeClosed(1, count).forEach(i ->
                seatRepo.save(ShowSeat.builder().show(saved).seatNumber(String.valueOf(i)).status(SeatStatus.AVAILABLE).build())
        );
        return saved;
    }
     public List<Show> getAllShows() {
        return showRepo.findAll();
    }
    public void deleteById(Long id) {
        showRepo.deleteById(id);
    }

    public Show update(Long id, Show s){
        s.setId(id); return showRepo.save(s); 
    }


    public void delete(Long id){ 
        showRepo.deleteById(id); 
    }
    
    public List<ShowSeat> seats(Long showId) {
    Show show = showRepo.findById(showId)
            .orElseThrow(() -> new RuntimeException("Show not found"));
    return seatRepo.findByShowOrderBySeatNumberAsc(show);
}

}
