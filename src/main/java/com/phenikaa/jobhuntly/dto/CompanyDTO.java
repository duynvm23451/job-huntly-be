package com.phenikaa.jobhuntly.dto;

import com.phenikaa.jobhuntly.entity.Industry;
import com.phenikaa.jobhuntly.entity.Job;
import com.phenikaa.jobhuntly.entity.User;

import java.util.Date;
import java.util.Set;

public class CompanyDTO {
    public record CompanyResponse(
            Integer id,
            String name,
            String description,
            String location,
            String employees,
            Date dateFounded,
            String facebookLink,
            String youtubeLink,
            String linkedinLink,
            String websiteLink,
            Industry industry
    ) {}
}
