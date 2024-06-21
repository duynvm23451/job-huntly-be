package com.phenikaa.jobhuntly.service;

import com.phenikaa.jobhuntly.dto.JobDTO;
import com.phenikaa.jobhuntly.entity.Job;
import com.phenikaa.jobhuntly.mapper.JobMapper;
import com.phenikaa.jobhuntly.repository.JobRepository;
import com.phenikaa.jobhuntly.specification.JobSpecification;
import com.phenikaa.jobhuntly.specification.filter.JobFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;
    private final JobMapper jobMapper;

    public Page<JobDTO.JobResponse> getJobsFilter(JobFilter filter, Pageable pageable) {
        Specification<Job> jobSpecification = Specification.where(null);
        if (filter.getTitle() != null) {
            System.out.println("title: " + filter.getTitle());
            jobSpecification = jobSpecification.and(JobSpecification.containsTitle(filter.getTitle()));
        }

        if (filter.getLocation() != null) {
            System.out.println("location" + filter.getLocation());
            jobSpecification = jobSpecification.and(JobSpecification.containsCompanyLocation(filter.getLocation()));
        }
        if (filter.getLevels() != null) {
            System.out.println("level" + filter.getLevels());
            jobSpecification = jobSpecification.and(JobSpecification.hasJobLevels(filter.getLevels()));
        }
        if (filter.getCategories() != null) {
            System.out.println("category" + filter.getCategories());
            jobSpecification = jobSpecification.and(JobSpecification.hasCategories(filter.getCategories()));
        }
        if (filter.getTypes() != null) {
            System.out.println("type" + filter.getTypes());
            jobSpecification = jobSpecification.and(JobSpecification.hasJobTypes(filter.getTypes()));
        }
        if (filter.getMinSalary() != null) {
            System.out.println("minSalary" + filter.getMinSalary());
            jobSpecification = jobSpecification.and(JobSpecification.greaterThanMinSalary(filter.getMinSalary()));
        }
        if (filter.getMaxSalary() != null) {
            System.out.println("maxSalary" + filter.getMaxSalary());
            jobSpecification = jobSpecification.and(JobSpecification.lessThanMaxSalary(filter.getMaxSalary()));
        }
        Page<Job> jobs = jobRepository.findAll(jobSpecification, pageable);
        return jobs.map(jobMapper::toJobResponse);
    }
}
