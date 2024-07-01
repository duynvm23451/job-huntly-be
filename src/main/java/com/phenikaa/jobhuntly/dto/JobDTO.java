package com.phenikaa.jobhuntly.dto;

import com.phenikaa.jobhuntly.entity.Job;
import com.phenikaa.jobhuntly.enums.JobLevel;
import com.phenikaa.jobhuntly.enums.JobType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

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
            Date deadline,
            List<String> categories,
            CompanyDTO.CompanyForJobResponse company
    ) {

    }
}
