package com.phenikaa.jobhuntly.dto;

import com.phenikaa.jobhuntly.enums.Gender;
import com.phenikaa.jobhuntly.enums.Role;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public class UserDTO {
    public record UserResponse(
            Integer id,
            String username,
            String email,
            String avatarFileName,
            String avatarUrl,
            String coverFileName,
            String coverUrl,
            String resumeFileName,
            String resumeUrl,
            String aboutMe,
            String fullName,
            Role role,
            Date dateOfBirth,
            String phoneNumber,
            Gender gender,
            String typeNotificationAccept,
            String address,
            CompanyDTO.ListCompanyResponse company,
            Date createdAt
    ) {
    }

    public record UserRequest(
            String fullName,
            String phoneNumber,
            String dateOfBirth,
            String gender,
            String aboutMe,
            String address,
            MultipartFile avatarFileName,
            MultipartFile coverFileName ,
            MultipartFile resumeFileName

    ) {}
}
