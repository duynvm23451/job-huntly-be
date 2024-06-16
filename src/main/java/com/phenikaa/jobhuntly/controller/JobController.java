package com.phenikaa.jobhuntly.controller;

import com.phenikaa.jobhuntly.dto.ResponseDTO;
import com.phenikaa.jobhuntly.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @GetMapping
    public ResponseDTO getJobsFilter(Pageable pageable) {
        return null;
    }
}
