package com.filmfestival.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "screening")
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "screening_id")
    @JsonProperty("screening_id")
    private Integer screeningId;

    @Column(name = "film_id")
    @JsonProperty("film_id")
    private Integer filmId;

    @Column(name = "venue_id")
    @JsonProperty("venue_id")
    private Integer venueId;

    @Column(name = "screening_date")
    @JsonProperty("screening_date")
    private LocalDate screeningDate;

    @Column(name = "start_time")
    @JsonProperty("start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    @JsonProperty("end_time")
    private LocalTime endTime;

    @Column(name = "ticket_price")
    @JsonProperty("ticket_price")
    private BigDecimal ticketPrice;
}
