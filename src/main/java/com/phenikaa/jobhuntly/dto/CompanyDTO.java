package com.phenikaa.jobhuntly.dto;

import com.phenikaa.jobhuntly.entity.User;

import java.util.Date;
import java.util.List;
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
            Set<IndustryDTO.IndustryResponse> industries,
            Set<UserDTO.UserResponse> users
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
