package com.phenikaa.jobhuntly.dto;

import java.util.Date;

public class ApplicationDto {
    public record ApplicationResponse(
            Integer id,
            UserDTO.UserResponse user,
            JobDTO.JobResponse job,
            String status,
            Date interviewTime,
            Date createdAt
    ) {
    }
}
