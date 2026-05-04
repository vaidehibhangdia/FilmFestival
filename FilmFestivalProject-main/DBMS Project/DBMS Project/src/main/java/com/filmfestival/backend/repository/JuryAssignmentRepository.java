package com.filmfestival.backend.repository;

import com.filmfestival.backend.model.JuryAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JuryAssignmentRepository extends JpaRepository<JuryAssignment, Integer> {
    List<JuryAssignment> findByJuryId(Integer juryId);
}
