package com.filmfestival.backend.controller;

import com.filmfestival.backend.model.Film;
import com.filmfestival.backend.repository.FilmRepository;
import com.filmfestival.backend.service.DashboardQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "*")
public class PublicController {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private DashboardQueryService dashboardQueryService;

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    @GetMapping("/screenings")
    public List<Map<String, Object>> getScreeningsByFilm(@RequestParam(required = false) Integer film_id) {
        if (film_id != null) {
            return dashboardQueryService.getScreeningsByFilmId(film_id);
        }
        return dashboardQueryService.getAllScreenings();
    }
}
