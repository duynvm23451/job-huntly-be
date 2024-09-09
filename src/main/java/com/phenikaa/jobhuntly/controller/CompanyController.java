package com.phenikaa.jobhuntly.controller;

import com.phenikaa.jobhuntly.dto.CompanyDTO;
import com.phenikaa.jobhuntly.dto.JobDTO;
import com.phenikaa.jobhuntly.dto.ResponseDTO;
import com.phenikaa.jobhuntly.dto.UserDTO;
import com.phenikaa.jobhuntly.entity.User;
import com.phenikaa.jobhuntly.mapper.UserMapper;
import com.phenikaa.jobhuntly.service.CompanyService;
import com.phenikaa.jobhuntly.specification.filter.AddUserToCompanyFilter;
import com.phenikaa.jobhuntly.specification.filter.CompanyFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping
    public ResponseDTO findCompaniesFilter(CompanyFilter companyFilter, Pageable pageable) {
        Page<CompanyDTO.ListCompanyResponse> companyResponses = this.companyService.findCompaniesFilter(companyFilter, pageable);
        return ResponseDTO.builder()
                .success(true)
                .message("Tìm kiếm công ty thành công")
                .code(HttpStatus.OK.value())
                .data(companyResponses)
                .build();
    }

    @GetMapping("/{comapanyId}")
    public ResponseDTO findById(@PathVariable Integer comapanyId) {
        CompanyDTO.CompanyResponse companyResponse = this.companyService.findCompanyDetail(comapanyId);
        return ResponseDTO.builder()
                .success(true)
                .message("Lấy chi tiết công ty thành công")
                .code(HttpStatus.OK.value())
                .data(companyResponse)
                .build();
    }

    @PostMapping
    public ResponseDTO createUpdate(@AuthenticationPrincipal Jwt jwt, CompanyDTO.CompanyRequest companyRequest) {
        Long userIdLong = jwt.getClaim("userId");
        Integer userId = userIdLong.intValue();
        CompanyDTO.CompanyResponse companyResponse = companyService.createUpdateCompany(companyRequest, userId);
        return ResponseDTO.builder()
                .success(true)
                .message("Tạo/thay đổi công ty thành công")
                .code(HttpStatus.CREATED.value())
                .data(companyResponse)
                .build();
    }

    @PutMapping
    public ResponseDTO addUserCompany(@AuthenticationPrincipal Jwt jwt, @RequestBody AddUserToCompanyFilter addUserToCompanyFilter) {
        Long userIdLong = jwt.getClaim("userId");
        Integer userId = userIdLong.intValue();
        companyService.addUserToCompany(userId, addUserToCompanyFilter.getEmail());
        return ResponseDTO.builder()
                .success(true)
                .message("Thêm người dùng vào công ty thành công")
                .code(HttpStatus.OK.value())
                .build();
    }

    @GetMapping("/users")
    public ResponseDTO findUserCompany(@AuthenticationPrincipal Jwt jwt) {
        Long userIdLong = jwt.getClaim("userId");
        Integer userId = userIdLong.intValue();
        List<UserDTO.UserResponse> userResponses = companyService.getUserInCompany(userId);
        return ResponseDTO.builder()
                .success(true)
                .message("Lấy danh sách người dùng thuộc công ty thành công thành công")
                .data(userResponses)
                .code(HttpStatus.OK.value())
                .build();
    }

    @GetMapping("/applicableJobs")
    public ResponseDTO getApplicableJobs(@AuthenticationPrincipal Jwt jwt, Pageable pageable) {
        Long userIdLong = jwt.getClaim("userId");
        Integer userId = userIdLong.intValue();
        Page<JobDTO.JobResponse> jobResponses = companyService.getApplicableJob(userId, pageable);
        return ResponseDTO.builder()
                .success(true)
                .message("Lấy công việc có thể ứng tuyển thành công")
                .code(HttpStatus.OK.value())
                .data(jobResponses)
                .build();
    }
}
