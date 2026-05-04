package com.filmfestival.backend.repository;

import com.filmfestival.backend.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {
    List<Evaluation> findByJuryId(Integer juryId);
}
