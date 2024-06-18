package com.phenikaa.jobhuntly.dto;

import com.phenikaa.jobhuntly.entity.Job;
import com.phenikaa.jobhuntly.enums.JobLevel;
import com.phenikaa.jobhuntly.enums.JobType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

public class JobDTO {

    public record JobResponse(
            String title,
            String description,
            JobType type,
            int minSalary,
            int maxSalary,
            String responsibilities,
            String niceToHaves,
            String preferredQualifications,
            int numberOfRecruits,
            JobLevel jobLevel
    ) {

    }
}
