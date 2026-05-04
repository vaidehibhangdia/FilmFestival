package com.filmfestival.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "crew")
public class Crew {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crew_id")
    @JsonProperty("crew_id")
    private Integer crewId;

    @Column(nullable = false)
    private String name;

    private String role;
}
