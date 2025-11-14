package com.movieticket.repository;

import com.movieticket.model.Show;
import com.movieticket.model.ShowSeat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {
    List<ShowSeat> findByShowId(Long showId);

    List<ShowSeat> findByShowOrderBySeatNumberAsc(Show show);

    Optional<ShowSeat> findByShowIdAndSeatNumber(Long showId, String seatNumber);

    @Transactional
    @Modifying
    @Query("UPDATE ShowSeat s SET s.available = :available WHERE s.show.id = :showId")
    void updateSeatAvailability(Long showId, boolean available);

   @Modifying
    @Query("UPDATE ShowSeat s SET s.status = 'AVAILABLE' " +
       "WHERE s.show.id = :showId AND s.seatNumber IN :seatNumbers")
    void releaseSpecificSeats(@Param("showId") Long showId, @Param("seatNumbers") List<String> seatNumbers);





    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from ShowSeat s where s.show.id=:showId and s.seatNumber in :seats")
    List<ShowSeat> lockSeats(@Param("showId") Long showId, @Param("seats") List<String> seats);
}
