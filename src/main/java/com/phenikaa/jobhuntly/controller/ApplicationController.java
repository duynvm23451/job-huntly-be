package com.phenikaa.jobhuntly.controller;

import com.phenikaa.jobhuntly.dto.ApplicationDto;
import com.phenikaa.jobhuntly.dto.ResponseDTO;
import com.phenikaa.jobhuntly.entity.Application;
import com.phenikaa.jobhuntly.mapper.ApplicationMapper;
import com.phenikaa.jobhuntly.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private final ApplicationMapper applicationMapper;

    @PostMapping("/jobs/{jobId}/users/{userId}")
    public ResponseDTO addUser(@PathVariable int jobId, @PathVariable int userId) {
        Application application = applicationService.createApplication(jobId, userId);
        ApplicationDto.ApplicationResponse applicationResponse = applicationMapper.toApplicationResponse(application);
        return ResponseDTO.builder()
                .success(true)
                .message("Ứng tuyển thành công")
                .code(HttpStatus.OK.value())
                .data(applicationResponse)
                .build();
    }

    @GetMapping("/applications/count/{userId}")
    public ResponseDTO getApplicationCount(@PathVariable Integer userId) {
        Map<String, Integer> response = applicationService.countAllAndByStatus(userId);
        return ResponseDTO.builder()
                .success(true)
                .message("")
                .code(HttpStatus.OK.value())
                .data(response)
                .build();
    }
}
