package com.filmfestival.backend.repository;

import com.filmfestival.backend.model.Jury;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface JuryRepository extends JpaRepository<Jury, Integer> {
    Optional<Jury> findByUserId(Integer userId);
}
