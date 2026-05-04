package com.filmfestival.backend.controller;

import com.filmfestival.backend.model.*;
import com.filmfestival.backend.repository.*;
import com.filmfestival.backend.service.DashboardQueryService;
import com.filmfestival.backend.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/jury")
@CrossOrigin(origins = "*", maxAge = 3600)
public class JuryController {

    @Autowired
    private JuryRepository juryRepository;

    @Autowired
    private JuryAssignmentRepository juryAssignmentRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private DashboardQueryService dashboardQueryService;

    @GetMapping("/assigned-films")
    public ResponseEntity<?> getAssignedFilms() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Jury> jury = juryRepository.findByUserId(userDetails.getId());
        
        if (jury.isEmpty()) {
            return ResponseEntity.badRequest().body("Jury record not found for user");
        }

        return ResponseEntity.ok(dashboardQueryService.getAssignedFilmsForJury(jury.get().getJuryId()));
    }

    @GetMapping("/evaluations")
    public ResponseEntity<?> getMyEvaluations() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Jury> jury = juryRepository.findByUserId(userDetails.getId());
        
        if (jury.isEmpty()) {
            return ResponseEntity.badRequest().body("Jury record not found for user");
        }

        return ResponseEntity.ok(dashboardQueryService.getEvaluationsForJury(jury.get().getJuryId()));
    }

    @PostMapping("/evaluate")
    public ResponseEntity<?> submitEvaluation(@RequestBody Evaluation evaluation) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Jury> jury = juryRepository.findByUserId(userDetails.getId());
        
        if (jury.isEmpty()) {
            return ResponseEntity.badRequest().body("Jury record not found for user");
        }

        evaluation.setJuryId(jury.get().getJuryId());
        
        try {
            Evaluation saved = evaluationRepository.save(evaluation);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving evaluation: " + e.getMessage());
        }
    }
}
