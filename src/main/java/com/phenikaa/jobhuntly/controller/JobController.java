package com.phenikaa.jobhuntly.controller;

import com.phenikaa.jobhuntly.dto.JobDTO;
import com.phenikaa.jobhuntly.dto.ResponseDTO;
import com.phenikaa.jobhuntly.entity.Job;
import com.phenikaa.jobhuntly.service.JobService;
import com.phenikaa.jobhuntly.specification.filter.JobFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jobs")
public class JobController {
    private final JobService jobService;

    @GetMapping
    public ResponseDTO getJobsFilter(JobFilter filter, Pageable pageable) {
        Page<JobDTO.JobResponse> jobResponses = this.jobService.getJobsFilter(filter, pageable);
        return ResponseDTO.builder()
                .success(true)
                .message("Tìm kiếm thành công")
                .code(HttpStatus.OK.value())
                .data(jobResponses)
                .build();
    }
}
