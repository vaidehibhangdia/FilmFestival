package com.filmfestival.backend.repository;

import com.filmfestival.backend.model.Award;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AwardRepository extends JpaRepository<Award, Integer> {
}
