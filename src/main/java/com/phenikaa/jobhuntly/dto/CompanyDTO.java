package com.phenikaa.jobhuntly.dto;

import com.phenikaa.jobhuntly.entity.User;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;
import java.util.Set;

public class CompanyDTO {

    public record CompanyRequest(
            Integer id,
            MultipartFile file,
            @NotBlank(message = "Không được để trống tên")
            String name,
            @NotBlank(message = "Không được để trống địa chỉ")
            String location,
            @Positive(message = "Số lượng nhân viên phải lớn hơn 0")
            Integer employees,
            @NotNull(message = "Không được để trống lĩnh vực")
            Set<String> industries,
            @NotNull(message = "Không được để trống ngành thành lập")
            @PastOrPresent(message = "Ngày thành lập không hợp lệ")
            Date dateFounded,
            @NotBlank(message = "Không được để trống mô tả")
            String description,
            String facebookLink,
            String youtubeLink,
            String linkedinLink,
            String websiteLink
            ) {
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static  class CompanyResponse {
        Integer id;
        String name;
        String description;
        String location;
        Integer employees;
        Date dateFounded;
        String logo;
        String url;
        String facebookLink;
        String youtubeLink;
        String linkedinLink;
        String websiteLink;
        Set<IndustryDTO.IndustryResponse> industries;
        Set<UserDTO.UserResponse> users;
    }

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
