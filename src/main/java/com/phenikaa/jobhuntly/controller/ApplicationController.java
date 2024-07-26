package com.phenikaa.jobhuntly.controller;

import com.phenikaa.jobhuntly.dto.ApplicationDto;
import com.phenikaa.jobhuntly.dto.ResponseDTO;
import com.phenikaa.jobhuntly.entity.Application;
import com.phenikaa.jobhuntly.mapper.ApplicationMapper;
import com.phenikaa.jobhuntly.service.ApplicationService;
import com.phenikaa.jobhuntly.specification.filter.ApplicationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/applications")
    public ResponseDTO getApplicationsByUser(@AuthenticationPrincipal Jwt jwt, ApplicationFilter filter, Pageable pageable) {
        Long userIdLong = jwt.getClaim("userId");
        Integer userId = userIdLong.intValue();
        Page<Application> applications = applicationService.getApplicationsByUser(filter, userId, pageable);
        Page<ApplicationDto.ApplicationResponse> responses = applications.map(applicationMapper::toApplicationResponse);
        return ResponseDTO.builder()
                .success(true)
                .message("Lấy các đơn ứng tuyển thành công")
                .code(HttpStatus.OK.value())
                .data(responses)
                .build();
    }

    @GetMapping("/applications/latest/interviewing")
    public ResponseDTO getLatestInterviewingApplications(@AuthenticationPrincipal Jwt jwt, Pageable pageable) {
        Long userIdLong = jwt.getClaim("userId");
        Integer userId = userIdLong.intValue();
        Page<Application> applications = applicationService.getLatestInterviewing(userId, pageable);
        Page<ApplicationDto.ApplicationResponse> responses = applications.map(applicationMapper::toApplicationResponse);
        return ResponseDTO.builder()
                .success(true)
                .message("Lấy các đơn ứng tuyển mới nhất đang phỏng vấn thành công")
                .code(HttpStatus.OK.value())
                .data(responses)
                .build();
    }
}
