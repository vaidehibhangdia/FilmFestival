package com.filmfestival.backend.repository;

import com.filmfestival.backend.model.FilmCrew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmCrewRepository extends JpaRepository<FilmCrew, Integer> {
}
