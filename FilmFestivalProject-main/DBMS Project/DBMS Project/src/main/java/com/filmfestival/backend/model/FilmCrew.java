package com.filmfestival.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "film_crew")
public class FilmCrew {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "film_id", nullable = false)
    @JsonProperty("film_id")
    private Integer filmId;

    @Column(name = "crew_id", nullable = true)
    @JsonProperty("crew_id")
    private Integer crewId;

    private String name;

    private String role;
}
