package com.filmfestival.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "jury")
public class Jury {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jury_id")
    @JsonProperty("jury_id")
    private Integer juryId;

    @Column(name = "user_id", nullable = false, unique = true)
    @JsonProperty("user_id")
    private Integer userId;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
