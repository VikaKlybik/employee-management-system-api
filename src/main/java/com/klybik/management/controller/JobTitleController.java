package com.klybik.management.controller;

import com.klybik.management.dto.jobtitle.CreateJobTitleRequest;
import com.klybik.management.dto.jobtitle.JobTitleResponse;
import com.klybik.management.dto.jobtitle.SetLeadRequest;
import com.klybik.management.dto.jobtitle.UpdateJobTitleRequest;
import com.klybik.management.entity.JobTitle;
import com.klybik.management.mapper.JobTitleMapper;
import com.klybik.management.service.JobTitleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/job-title")
@RequiredArgsConstructor
@Tag(name = "JobTitle Controller", description = "APIs for working with department data")
public class JobTitleController {

    private final JobTitleService jobTitleService;
    private final JobTitleMapper jobTitleMapper;

    @GetMapping("/{id}")
    public JobTitleResponse getJobTitleById(@PathVariable UUID id) {
        JobTitle jobTitle = jobTitleService.getJobTitleById(id);
        return jobTitleMapper.toJobTitleResponse(jobTitle);
    }

    @GetMapping("/by-department/{departmentId}")
    public List<JobTitleResponse> getJobTitleByDepartmentId(@PathVariable UUID departmentId) {
        List<JobTitle> jobTitleList = jobTitleService.getAllByDepartmentId(departmentId);
        return jobTitleMapper.toListOfJobTitleResponse(jobTitleList);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobTitleResponse createJobTitle(@RequestBody @Valid CreateJobTitleRequest createJobTitleRequest) {
        JobTitle jobTitle = jobTitleService.createJobTitle(createJobTitleRequest);
        return jobTitleMapper.toJobTitleResponse(jobTitle);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteJobTitle(@PathVariable UUID id) {
        jobTitleService.deleteById(id);
    }

    @PutMapping("/{id}")
    public JobTitleResponse updateJobTitle(@PathVariable UUID id, @RequestBody  @Valid UpdateJobTitleRequest updateJobTitleRequest) {
        JobTitle updatedJobTitle = jobTitleService.updateJobTitle(id, updateJobTitleRequest);
        return jobTitleMapper.toJobTitleResponse(updatedJobTitle);
    }

    @PutMapping("/lead/{id}")
    public JobTitleResponse setLead(@PathVariable UUID id, @RequestBody @Valid SetLeadRequest setLeadRequest) {
        JobTitle jobTitle = jobTitleService.setLead(id, setLeadRequest);
        return jobTitleMapper.toJobTitleResponse(jobTitle);
    }

    @DeleteMapping("/lead/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLead(@PathVariable UUID id) {
        jobTitleService.deleteLead(id);
    }
}
