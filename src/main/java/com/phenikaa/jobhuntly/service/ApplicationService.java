package com.phenikaa.jobhuntly.service;

import com.phenikaa.jobhuntly.dto.ApplicationDto;
import com.phenikaa.jobhuntly.entity.Application;
import com.phenikaa.jobhuntly.entity.Job;
import com.phenikaa.jobhuntly.entity.User;
import com.phenikaa.jobhuntly.enums.ApplicationStatus;
import com.phenikaa.jobhuntly.enums.Role;
import com.phenikaa.jobhuntly.exception.ObjectNotFoundException;
import com.phenikaa.jobhuntly.exception.SharedException;
import com.phenikaa.jobhuntly.mapper.ApplicationMapper;
import com.phenikaa.jobhuntly.repository.ApplicationRepository;
import com.phenikaa.jobhuntly.repository.JobRepository;
import com.phenikaa.jobhuntly.repository.UserRepository;
import com.phenikaa.jobhuntly.specification.ApplicationSpecification;
import com.phenikaa.jobhuntly.specification.filter.ApplicationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;

    public Application createApplication(Integer jobId, Integer userId) {
        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new ObjectNotFoundException("Công ty", jobId)
        );
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Người dùng", userId)
        );
        if (user.getRole() != Role.EMPLOYEE) {
            throw new SharedException("Chỉ ứng viên mới có thể ứng tuyển");
        }

        Optional<Application> existingApplication = applicationRepository.findByJobIdAndUserId(jobId, userId);
        if (existingApplication.isPresent()) {
            throw new SharedException("Bạn đã ứng tuyển công việc này rồi");
        }
        int numberOfApplicantForJob = applicationRepository.findByJobAndStatus(job, ApplicationStatus.HIRED).size();
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

    public Page<Application> getApplicationsByUser(ApplicationFilter filter,Integer userId, Pageable pageable) {
        Specification<Application> specification = Specification.where(null);
        specification = specification.and(ApplicationSpecification.byUserId(userId));
        if (filter.getStatus() != null) {
            System.out.println(filter.getStatus());
            specification = specification.and(ApplicationSpecification.hasStatus(ApplicationStatus.valueOf(filter.getStatus())));
        }
        if (filter.getJobTitle() != null) {
            specification = specification.and(ApplicationSpecification.containJobTitle(filter.getJobTitle()));
        }
        return applicationRepository.findAll(specification, pageable);
    }

    public Page<Application> getLatestInterviewing(Integer userId, Pageable pageable) {
        Specification<Application> specification = Specification.where(null);
        specification = specification.and(ApplicationSpecification.closestToNowAndGreaterThanNow());
        return applicationRepository.findAll(specification, pageable);
    }

    public Page<Application> getApplicants(Integer userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Người dùng", userId)
        );
        if (user.getCompany() == null) {
            throw new SharedException("Người dùng chưa thuộc công ty nào!!!");
        }
        Specification<Application> specification = Specification.where(null);
        specification = specification.and(ApplicationSpecification.byCompanyId(user.getCompany().getId()));
        return applicationRepository.findAll(specification, pageable);
    }

    public Page<Application> getApplicationsByJobId(Integer userId, int jobId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Người dùng", userId)
        );
        if (user.getCompany() == null) {
            throw new SharedException("Người dùng chưa thuộc công ty nào!!!");
        }
        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new ObjectNotFoundException("Công việc", jobId)
        );
        if (!Objects.equals(user.getCompany().getId(), job.getCompany().getId())) {
            throw new SharedException("Bạn không có quyền xem đơn ứng tuyển của công việc này");
        }
        Specification<Application> specification = Specification.where(null);
        specification = specification.and(ApplicationSpecification.byJobId(jobId));
        return applicationRepository.findAll(specification, pageable);
    }

    public ApplicationDto.ApplicationResponse getApplicationsById(int id) {
        Application application = applicationRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("Đơn ứng tuyển", id)
        );
        return applicationMapper.toApplicationResponse(application);
    }

    public ApplicationDto.ApplicationResponse updateApplication(int id, String status, Timestamp interviewTime) {
        System.out.println(id);
        System.out.println(status);
        System.out.println(interviewTime);
        Application application = applicationRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("Đơn ứng tuyển", id)
        );

        if (ApplicationStatus.valueOf(status) == ApplicationStatus.INTERVIEWING && interviewTime == null) {
            throw new SharedException("Vui lòng chọn thời gian phỏng vấn");
        }

        application.setStatus(ApplicationStatus.valueOf(status));
        if (interviewTime != null) {
            application.setInterviewTime(interviewTime);
        }
        return applicationMapper.toApplicationResponse(applicationRepository.save(application));
    }
}
