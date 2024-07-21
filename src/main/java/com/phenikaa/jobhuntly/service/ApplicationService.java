package com.phenikaa.jobhuntly.service;

import com.phenikaa.jobhuntly.entity.Application;
import com.phenikaa.jobhuntly.entity.Job;
import com.phenikaa.jobhuntly.entity.User;
import com.phenikaa.jobhuntly.enums.ApplicationStatus;
import com.phenikaa.jobhuntly.enums.Role;
import com.phenikaa.jobhuntly.exception.ObjectNotFoundException;
import com.phenikaa.jobhuntly.exception.SharedException;
import com.phenikaa.jobhuntly.repository.ApplicationRepository;
import com.phenikaa.jobhuntly.repository.JobRepository;
import com.phenikaa.jobhuntly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    public Application createApplication(Integer jobId, Integer userId) {
        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new ObjectNotFoundException("Công ty", jobId)
        );
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Người dùng", userId)
        );
        if (user.getRole() != Role.EMPLOYEE) {
            throw new AccessDeniedException("Chỉ ứng viên mới có thể ứng tuyển");
        }
        Optional<Application> existingApplication = applicationRepository.findByJobIdAndUserId(jobId, userId);
        if (existingApplication.isPresent()) {
            throw new SharedException("Bạn đã ứng tuyển công việc này rồi");
        }
        int numberOfApplicantForJob = applicationRepository.countByJobId(jobId);
        if (numberOfApplicantForJob >= job.getNumberOfRecruits()) {
            throw new SharedException("Công việc này đã tuyển đủ số người");
        }
        Application application = new Application();
        application.setJob(job);
        application.setUser(user);
        application.setStatus(ApplicationStatus.IN_REVIEW);
        return applicationRepository.save(application);
    }

    public Map<String, Integer> countAllAndByStatus(Integer userId) {
        return applicationRepository.countAllAndByStatus(userId);
    }

    public Page<Application> getApplicationsByUser(Integer userId, Pageable pageable) {
        return applicationRepository.findApplicationsByUser(userId, pageable);
    }

    public Page<Application> getLatestInterviewing(Integer userId, Pageable pageable) {
        LocalDateTime currentTime = LocalDateTime.now();
        return applicationRepository.findByUserIdAndStatusAndInterviewTimeBefore(userId, ApplicationStatus.INTERVIEWING, currentTime, pageable);
    }
}
