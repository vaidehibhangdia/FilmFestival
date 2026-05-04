package com.filmfestival.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "jury_assignment")
public class JuryAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "jury_id", nullable = false)
    @JsonProperty("jury_id")
    private Integer juryId;

    @Column(name = "film_id", nullable = false)
    @JsonProperty("film_id")
    private Integer filmId;

    @Column(name = "assigned_at", insertable = false, updatable = false)
    private LocalDateTime assignedAt;
}
