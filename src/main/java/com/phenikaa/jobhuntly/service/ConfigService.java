package com.phenikaa.jobhuntly.service;


import com.phenikaa.jobhuntly.entity.*;
import com.phenikaa.jobhuntly.enums.ApplicationStatus;
import com.phenikaa.jobhuntly.enums.JobLevel;
import com.phenikaa.jobhuntly.enums.JobType;
import com.phenikaa.jobhuntly.enums.Role;
import com.phenikaa.jobhuntly.exception.ObjectNotFoundException;
import com.phenikaa.jobhuntly.exception.SharedException;
import com.phenikaa.jobhuntly.repository.*;
import com.phenikaa.jobhuntly.specification.ApplicationSpecification;
import com.phenikaa.jobhuntly.specification.JobSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ConfigService {

    private final CategoryRepository categoryRepository;
    private final IndustryRepository industryRepository;
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final JobRepository jobRepository;

    public Map<String, List<String>> getConfigurations() {
        Map<String, List<String>> map = new HashMap<>();
        List<String> categories = categoryRepository.findAll().stream().map(
                Category::getName
        ).toList();
        map.put("categories", categories);
        List<String> jobTypes = Arrays.stream(JobType.values()).map(JobType::name).toList();
        map.put("jobTypes", jobTypes);
        List<String> industries = industryRepository.findAll().stream().map(
                Industry::getName
        ).toList();
        map.put("industries", industries);
        List<String> applicationStatus = Arrays.stream(ApplicationStatus.values()).map(ApplicationStatus::name).toList();
        map.put("applicationStatus", applicationStatus);

        List<String> jobLevels = Arrays.stream(JobLevel.values()).map(JobLevel::name).toList();
        map.put("jobLevels", jobLevels);
        return map;
    }

    public Map<String, Integer> getRecruiterDashboardInfo(Integer userId) {
        Map<String, Integer> map = new HashMap<>();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Người dùng", userId)
        );
        if (user.getCompany() == null) {
            throw new SharedException("Bạn không thuộc công ty nào");
        }
        Specification<Application> applicationSpecification = Specification.where(null);
        applicationSpecification = applicationSpecification.and(ApplicationSpecification.haveCompanyIdAndStatus(user.getCompany().getId(), ApplicationStatus.IN_REVIEW));
        List<Application> applications = applicationRepository.findAll(applicationSpecification);
        map.put("applications", applications.size());

        int notRepChatRooms = chatRoomRepository.countByCompanyAndIsCompanySeen(user.getCompany(), false);
        map.put("notRepChatRooms", notRepChatRooms);

        List<Job> jobs = jobRepository.findAll(JobSpecification.canApplyByCompany(user.getCompany()));
        map.put("applicableJobs", jobs.size());

        List<String> jobLevels = Arrays.stream(JobLevel.values()).map(JobLevel::name).toList();
        for (String jobLevel: jobLevels) {
            map.put(jobLevel.toLowerCase(), applicationRepository.findAll(ApplicationSpecification.haveCompanyAndJobLevel(user.getCompany(), JobLevel.valueOf(jobLevel))).size());
        }

        map.put("totalApplications", applicationRepository.findAll(ApplicationSpecification.statusNotInHIREDAndCANCELLED(user.getCompany())).size());

        return map;
    }
}
