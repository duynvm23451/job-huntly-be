package com.phenikaa.jobhuntly.dto;

import com.phenikaa.jobhuntly.enums.Gender;
import com.phenikaa.jobhuntly.enums.Role;

import java.util.Date;
import java.util.List;

public class UserDTO {
    public record UserResponse(
            Integer id,
            String username,
            String email,
            String avatarFileName,
            String coverFileName,
            String resumeFileName,
            String aboutMe,
            String fullName,
            Role role,
            Date dateOfBirth,
            String phoneNumber,
            Gender gender,
            String typeNotificationAccept,
            String address
    ) {
    }
}
