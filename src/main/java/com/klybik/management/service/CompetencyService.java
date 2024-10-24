package com.klybik.management.service;

import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.Duplicate;
import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.NotFound;
import com.klybik.management.dto.competency.CreateCompetencyRequest;
import com.klybik.management.entity.Competency;
import com.klybik.management.repository.CompetencyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompetencyService {

    private final CompetencyRepository competencyRepository;

    public List<Competency> getAllCompetency() {
        return competencyRepository.findAll();
    }

    public Competency getCompetencyById(UUID id) {
        return competencyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NotFound.COMPETENCY.formatted(id)));
    }

    public Competency createCompetency(CreateCompetencyRequest createCompetencyRequest) {
        validateIfCompetencyExistsByName(createCompetencyRequest.getName());
        return competencyRepository.save(Competency.builder()
                .name(createCompetencyRequest.getName())
                .description(createCompetencyRequest.getDescription())
                .build());
    }

    public Competency updateCompetency(UUID id, CreateCompetencyRequest competencyRequest) {
        validateIfCompetencyExistsByName(competencyRequest.getName());
        return competencyRepository.save(
                getCompetencyById(id)
        );
    }

    private void validateIfCompetencyExistsByName(String name) {
        if (competencyRepository.existsByNameIgnoreCase(name)) {
            throw new DuplicateKeyException(Duplicate.COMPETENCY.formatted(name));
        }
    }
}
