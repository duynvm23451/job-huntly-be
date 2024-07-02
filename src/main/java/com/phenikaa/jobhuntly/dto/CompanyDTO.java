package com.phenikaa.jobhuntly.dto;

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
            String logo,
            String facebookLink,
            String youtubeLink,
            String linkedinLink,
            String websiteLink,
            Set<IndustryDTO.IndustryResponse> industries
    ) {}

    public record ListCompanyResponse(
            Integer id,
            String name,
            String description,
            String logo,
            Integer availableJobs,
            Set<IndustryDTO.IndustryResponse> industries
    ) {}

    public record CompanyForJobResponse(
            Integer id,
            String name,
            String logo,
            String location,
            Integer employees
    ) {}
}
