package com.phenikaa.jobhuntly.controller;

import com.phenikaa.jobhuntly.dto.CompanyDTO;
import com.phenikaa.jobhuntly.dto.ResponseDTO;
import com.phenikaa.jobhuntly.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping
    public ResponseDTO findAll(Pageable pageable) {
        Page<CompanyDTO.CompanyResponse> companyResponses = this.companyService.findCompaniesFilter(pageable);
        return ResponseDTO.builder()
                .success(true)
                .message("Tìm kiếm công ty thành công")
                .code(HttpStatus.OK.value())
                .data(companyResponses)
                .build();
    }
}
