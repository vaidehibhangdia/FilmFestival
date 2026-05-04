package com.filmfestival.backend.repository;

import com.filmfestival.backend.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByAttendeeId(Integer attendeeId);
    List<Ticket> findByScreeningId(Integer screeningId);
}
