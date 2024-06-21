package com.phenikaa.jobhuntly.service;

import com.phenikaa.jobhuntly.dto.CompanyDTO;
import com.phenikaa.jobhuntly.entity.Company;
import com.phenikaa.jobhuntly.mapper.CompanyMapper;
import com.phenikaa.jobhuntly.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;


    public Page<CompanyDTO.CompanyResponse> findCompaniesFilter(Pageable pageable) {
        Page<Company> companies = companyRepository.findAll(pageable);
        Page<CompanyDTO.CompanyResponse> companyResponses = companies.map(companyMapper::toCompanyResponse);
        return companyResponses;
    }
}
