package com.phenikaa.jobhuntly.dto;

import com.phenikaa.jobhuntly.entity.Job;
import com.phenikaa.jobhuntly.entity.JobCategory;
import com.phenikaa.jobhuntly.enums.JobLevel;
import com.phenikaa.jobhuntly.enums.JobType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class JobDTO {

    public record JobResponse(
            Integer id,
            String title,
            String description,
            JobType type,
            int minSalary,
            int maxSalary,
            String responsibilities,
            String niceToHaves,
            String preferredQualifications,
            String perkAndBenefits,
            int numberOfRecruits,
            JobLevel jobLevel,
            Date createdAt,
            Date updatedAt,
            Date deadline,
            List<String> categories,
            CompanyDTO.CompanyForJobResponse company,
            Integer numberOfHired
    ) {
    }

    public record JobRequest(
            String title,
            String description,
            JobType type,
            String responsibilities,
            String niceToHaves,
            String preferredQualifications,
            String perkAndBenefits,
            Integer numberOfRecruits,
            JobLevel jobLevel,
            Date deadline,
            List<String> categories,
            Integer minSalary,
            Integer maxSalary
    ) {}
}
