package com.filmfestival.backend.controller;

import com.filmfestival.backend.model.Ticket;
import com.filmfestival.backend.model.Attendee;
import com.filmfestival.backend.repository.TicketRepository;
import com.filmfestival.backend.repository.AttendeeRepository;
import com.filmfestival.backend.repository.UserRepository;
import com.filmfestival.backend.security.services.UserDetailsImpl;
import com.filmfestival.backend.service.DashboardQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private AttendeeRepository attendeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DashboardQueryService dashboardQueryService;

    @GetMapping("/my-bookings")
    public ResponseEntity<?> getMyBookings() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(dashboardQueryService.getBookingsForUser(userDetails.getId()));
    }

    @GetMapping("/occupied-seats")
    public ResponseEntity<?> getOccupiedSeats(@RequestParam Integer screening_id) {
        return ResponseEntity.ok(dashboardQueryService.getOccupiedSeats(screening_id));
    }

    @PostMapping("/book-ticket")
    public ResponseEntity<?> bookTicket(@RequestBody Map<String, Object> payload) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<Attendee> attendee = attendeeRepository.findByUserId(userDetails.getId());

            if (attendee.isEmpty()) {
                return ResponseEntity.badRequest().body("Attendee record not found for user");
            }

            Integer screeningId = (Integer) payload.get("screening_id");
            List<String> seatNumbers;
            
            if (payload.get("seat_numbers") instanceof List) {
                seatNumbers = (List<String>) payload.get("seat_numbers");
            } else if (payload.get("seat_number") != null) {
                seatNumbers = List.of((String) payload.get("seat_number"));
            } else {
                return ResponseEntity.badRequest().body("screening_id and seat_numbers are required");
            }

            if (screeningId == null || seatNumbers == null || seatNumbers.isEmpty()) {
                return ResponseEntity.badRequest().body("screening_id and seat_numbers are required");
            }

            // Check if any seat is already taken
            List<String> occupied = dashboardQueryService.getOccupiedSeats(screeningId);
            for (String seatNumber : seatNumbers) {
                if (occupied.contains(seatNumber)) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Seat " + seatNumber + " is already booked"));
                }
            }

            // Book all seats
            for (String seatNumber : seatNumbers) {
                Ticket ticket = new Ticket();
                ticket.setScreeningId(screeningId);
                ticket.setAttendeeId(attendee.get().getAttendeeId());
                ticket.setUserId(userDetails.getId());
                ticket.setSeatNumber(seatNumber);
                ticketRepository.save(ticket);
            }

            return ResponseEntity.ok(Map.of(
                "message", seatNumbers.size() + " tickets booked successfully",
                "count", seatNumbers.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error booking tickets: " + e.getMessage()));
        }
    }
}
