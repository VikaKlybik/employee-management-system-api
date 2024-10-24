package com.klybik.management.controller;

import com.klybik.management.dto.competency.CreateCompetencyRequest;
import com.klybik.management.entity.Competency;
import com.klybik.management.service.CompetencyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/competency")
@RequiredArgsConstructor
@Tag(name = "Competency Controller", description = "APIs for working with competency data")
public class CompetencyController {

    private final CompetencyService competencyService;

    @GetMapping
    public List<Competency> getCompetencies() {
        return competencyService.getAllCompetency();
    }

    @GetMapping("/{id}")
    public Competency getCompetency(@PathVariable UUID id) {
        return competencyService.getCompetencyById(id);
    }

    @PostMapping
    public Competency createCompetency(@RequestBody CreateCompetencyRequest createCompetencyRequest) {
        return competencyService.createCompetency(createCompetencyRequest);
    }

    @PutMapping("/{id}")
    public Competency updateCompetency(@PathVariable UUID id, @RequestBody CreateCompetencyRequest competencyRequest) {
        return competencyService.updateCompetency(id, competencyRequest);
    }
}
