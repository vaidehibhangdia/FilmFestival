package com.filmfestival.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    @JsonProperty("ticket_id")
    private Integer ticketId;

    @Column(name = "screening_id", nullable = false)
    @JsonProperty("screening_id")
    private Integer screeningId;

    @Column(name = "attendee_id")
    @JsonProperty("attendee_id")
    private Integer attendeeId;

    @Column(name = "user_id")
    @JsonProperty("user_id")
    private Integer userId;

    @Column(name = "seat_number", nullable = false)
    @JsonProperty("seat_number")
    private String seatNumber;

    @Column(name = "booking_time", insertable = false, updatable = false)
    private LocalDateTime bookingTime;
}
