package com.phenikaa.jobhuntly.controller;

import com.phenikaa.jobhuntly.dto.CompanyDTO;
import com.phenikaa.jobhuntly.dto.ResponseDTO;
import com.phenikaa.jobhuntly.service.CompanyService;
import com.phenikaa.jobhuntly.specification.filter.CompanyFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
