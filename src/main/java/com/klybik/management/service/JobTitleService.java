package com.klybik.management.service;

import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.Logic;
import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.Duplicate;
import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.NotFound;
import com.klybik.management.dto.jobtitle.CreateJobTitleRequest;
import com.klybik.management.dto.jobtitle.SetLeadRequest;
import com.klybik.management.dto.jobtitle.UpdateJobTitleRequest;
import com.klybik.management.entity.JobTitle;
import com.klybik.management.exception.JobTitleDeletedException;
import com.klybik.management.exception.SetLeadException;
import com.klybik.management.exception.UpdateLeadException;
import com.klybik.management.repository.JobTitleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobTitleService {

    private final JobTitleRepository jobTitleRepository;
    private final DepartmentService departmentService;

    public JobTitle getJobTitleById(UUID id) {
        return jobTitleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NotFound.JOB_TITLE));
    }

    public JobTitle createJobTitle(CreateJobTitleRequest createJobTitleRequest) {
        validateJobTitleExistByDepartmentIdAndName(createJobTitleRequest.getDepartmentId(), createJobTitleRequest.getName());
        JobTitle jobTitle = JobTitle.builder()
                .name(createJobTitleRequest.getName())
                .department(departmentService.getDepartmentById(createJobTitleRequest.getDepartmentId()))
                .isLead(createJobTitleRequest.getIsLead())
                .build();

        if(createJobTitleRequest.getLeadId() != null) {
            jobTitle.setLead(getJobTitleById(createJobTitleRequest.getLeadId()));
        }
        return jobTitleRepository.save(jobTitle);
    }

    public List<JobTitle> getAllByDepartmentId(UUID departmentId) {
        return jobTitleRepository.findAllByDepartmentId(departmentId);
    }

    public void deleteById(UUID id) {
        JobTitle jobTitle = getJobTitleById(id);
        if (!jobTitle.getSubordinates().isEmpty()) {
            throw new JobTitleDeletedException();
        }

        jobTitleRepository.delete(jobTitle);
    }

    public JobTitle updateJobTitle(UUID jobTitleId, UpdateJobTitleRequest updateJobTitleRequest) {
        JobTitle jobTitle = getJobTitleById(jobTitleId);
        validateJobTitleExistByDepartmentIdAndName(jobTitle.getDepartment().getId(), updateJobTitleRequest.getName());

        if (updateJobTitleRequest.getIsLead().equals(Boolean.FALSE)
                && jobTitle.getIsLead().equals(Boolean.TRUE)
                && !jobTitle.getSubordinates().isEmpty()) {
            throw new UpdateLeadException();
        }

        jobTitle.setName(updateJobTitleRequest.getName())
                .setIsLead(updateJobTitleRequest.getIsLead());

        return jobTitleRepository.save(jobTitle);
    }

    public JobTitle setLead(UUID id, SetLeadRequest setLeadRequest) {
        JobTitle jobTitle = getJobTitleById(id);
        JobTitle leadJobTitle = getJobTitleById(setLeadRequest.getLeadId());

        if(leadJobTitle.getIsLead().equals(Boolean.FALSE)) {
            throw new SetLeadException(Logic.NOT_LEADER);
        }
        jobTitle.setLead(leadJobTitle);
        return jobTitleRepository.save(
                jobTitle
        );
    }

    public void deleteLead(UUID id) {
        JobTitle jobTitle = getJobTitleById(id);
        jobTitleRepository.save(
                jobTitle.setLead(null)
        );
    }

    private void validateJobTitleExistByDepartmentIdAndName(UUID departmentId, String name) {
        if (jobTitleRepository.existsByNameIgnoreCaseAndDepartmentId(name, departmentId)) {
            throw new DuplicateKeyException(Duplicate.JOB_TITLE.formatted(name, departmentId));
        }
    }
}
