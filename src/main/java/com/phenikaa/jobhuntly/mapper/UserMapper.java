package com.phenikaa.jobhuntly.mapper;

import com.phenikaa.jobhuntly.dto.UserDTO;
import com.phenikaa.jobhuntly.entity.User;
import com.phenikaa.jobhuntly.s3.S3ImageUploader;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Autowired
    private S3ImageUploader s3ImageUploader;

    @Mapping(source = "company.logo", target = "company.logo", qualifiedByName = "convertLogCompanyInUserToUrl")
    @Mapping(target = "avatarUrl", expression = "java(covertAvatarUrl(user))")
    @Mapping(target = "coverUrl", expression = "java(covertCoverUrl(user))")
    @Mapping(target = "resumeUrl", expression = "java(covertResumeUrl(user))")
    public abstract UserDTO.UserResponse toUserResponse(User user);

    @Named("convertLogCompanyInUserToUrl")
    String convertLogoToUrl(String logo) {  // Removed @Context annotation
        if (logo != null) {
            return s3ImageUploader.getImageUrl(logo);  // Assuming a static method
        }
        return "";
    }

    String covertAvatarUrl(User user) {
        if (user != null) {
            return s3ImageUploader.getImageUrl(user.getAvatarFileName());
        }
        return "";
    }

    String covertCoverUrl(User user) {
        if (user != null) {
            return s3ImageUploader.getImageUrl(user.getCoverFileName());
        }
        return "";
    }

    String covertResumeUrl(User user) {
        if (user != null) {
            return s3ImageUploader.getImageUrl(user.getResumeFileName());
        }
        return "";
    }
}
