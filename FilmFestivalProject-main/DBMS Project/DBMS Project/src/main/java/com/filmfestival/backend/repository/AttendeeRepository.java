package com.filmfestival.backend.repository;

import com.filmfestival.backend.model.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AttendeeRepository extends JpaRepository<Attendee, Integer> {
    Optional<Attendee> findByUserId(Integer userId);
    Optional<Attendee> findByEmail(String email);
    boolean existsByEmail(String email);
}
