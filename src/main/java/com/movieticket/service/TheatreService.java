package com.movieticket.service;

import com.movieticket.model.Theatre;
import com.movieticket.repository.TheatreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@SuppressWarnings("null")
@RequiredArgsConstructor
public class TheatreService {
    private final TheatreRepository repo;
    public List<Theatre> all(){ return repo.findAll(); }
    public Theatre get(Long id){ return repo.findById(id).orElseThrow(); }
    public Theatre create(Theatre t){ return repo.save(t); }
    public Theatre update(Long id, Theatre t){ t.setId(id); return repo.save(t); }
    public void delete(Long id){ repo.deleteById(id); }
}
