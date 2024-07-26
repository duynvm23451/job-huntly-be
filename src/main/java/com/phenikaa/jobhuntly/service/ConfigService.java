package com.phenikaa.jobhuntly.service;


import com.phenikaa.jobhuntly.entity.Category;
import com.phenikaa.jobhuntly.entity.Industry;
import com.phenikaa.jobhuntly.enums.ApplicationStatus;
import com.phenikaa.jobhuntly.enums.JobType;
import com.phenikaa.jobhuntly.repository.CategoryRepository;
import com.phenikaa.jobhuntly.repository.IndustryRepository;
import lombok.RequiredArgsConstructor;
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
        return map;
    }
}
