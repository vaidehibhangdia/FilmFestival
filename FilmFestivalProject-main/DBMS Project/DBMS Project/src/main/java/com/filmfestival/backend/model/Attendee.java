package com.filmfestival.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "attendee")
public class Attendee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendee_id")
    @JsonProperty("attendee_id")
    private Integer attendeeId;

    @Column(name = "user_id")
    @JsonProperty("user_id")
    private Integer userId;

    private String name;
    private String email;
    private String phone;
}
