package com.filmfestival.backend.controller;

import com.filmfestival.backend.model.*;
import com.filmfestival.backend.repository.*;
import com.filmfestival.backend.payload.request.*;
import com.filmfestival.backend.payload.response.*;
import com.filmfestival.backend.security.jwt.JwtUtils;
import com.filmfestival.backend.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    JuryRepository juryRepository;

    @Autowired
    AttendeeRepository attendeeRepository;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Ensure Jury/Attendee record exists for legacy accounts or missed syncs
        User user = userRepository.findById(userDetails.getId()).orElse(null);
        if (user != null) {
            if (user.getRole() == Role.JURY && juryRepository.findByUserId(user.getId()).isEmpty()) {
                Jury jury = new Jury();
                jury.setUserId(user.getId());
                juryRepository.save(jury);
            } else if (user.getRole() == Role.USER && attendeeRepository.findByUserId(user.getId()).isEmpty()) {
                // Check if attendee with this email already exists but not linked to user_id
                Attendee attendee = attendeeRepository.findByEmail(user.getEmail())
                        .orElse(new Attendee());
                
                attendee.setUserId(user.getId());
                attendee.setName(user.getName());
                attendee.setEmail(user.getEmail());
                attendeeRepository.save(attendee);
            }
        }

        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(item -> item.getAuthority().replace("ROLE_", ""))
                .orElse("USER");

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getName(),
                userDetails.getEmail(),
                role));
    }

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));

        String strRole = signUpRequest.getRole();
        if (strRole == null) {
            user.setRole(Role.USER);
        } else {
            switch (strRole.toUpperCase()) {
                case "ADMIN":
                    user.setRole(Role.ADMIN);
                    break;
                case "JURY":
                    user.setRole(Role.JURY);
                    break;
                default:
                    user.setRole(Role.USER);
            }
        }

        userRepository.save(user);
        userRepository.flush(); // Ensure ID is generated before syncing

        // Sync with Jury/Attendee tables
        if (user.getRole() == Role.JURY) {
            Jury jury = new Jury();
            jury.setUserId(user.getId());
            juryRepository.save(jury);
        } else if (user.getRole() == Role.USER) {
            // Check if attendee with this email already exists but not linked to user_id
            Attendee attendee = attendeeRepository.findByEmail(user.getEmail())
                    .orElse(new Attendee());
            
            attendee.setUserId(user.getId());
            attendee.setName(user.getName());
            attendee.setEmail(user.getEmail());
            // phone can be empty for now
            attendeeRepository.save(attendee);
        }

        // Auto-login the newly registered user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signUpRequest.getEmail(), signUpRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(item -> item.getAuthority().replace("ROLE_", ""))
                .orElse("USER");

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getName(),
                userDetails.getEmail(),
                role));
    }
}
