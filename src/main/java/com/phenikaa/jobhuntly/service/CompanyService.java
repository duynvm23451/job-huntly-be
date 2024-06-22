package com.phenikaa.jobhuntly.service;

import com.phenikaa.jobhuntly.dto.CompanyDTO;
import com.phenikaa.jobhuntly.entity.Company;
import com.phenikaa.jobhuntly.mapper.CompanyMapper;
import com.phenikaa.jobhuntly.repository.CompanyRepository;
import com.phenikaa.jobhuntly.specification.CompanySpecification;
import com.phenikaa.jobhuntly.specification.filter.CompanyFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;


    public Page<CompanyDTO.ListCompanyResponse> findCompaniesFilter(CompanyFilter companyFilter,Pageable pageable) {
        Specification<Company> companySpecification = Specification.where(null);
        if (companyFilter.getName() != null) {
            companySpecification = companySpecification.and(CompanySpecification.containsName(companyFilter.getName()));
        }
        if (companyFilter.getLocation() != null) {
            companySpecification = companySpecification.and(CompanySpecification.containsLocation(companyFilter.getLocation()));
        }
        if (companyFilter.getIndustries() != null) {
            companySpecification = companySpecification.and(CompanySpecification.hasIndustries(companyFilter.getIndustries()));
        }
        if (companyFilter.getMinEmployees() != null && companyFilter.getMaxEmployees() != null) {
            companySpecification = companySpecification.and(CompanySpecification.inRangeEmployees(companyFilter.getMinEmployees(), companyFilter.getMaxEmployees()));
        }
        Page<Company> companies = companyRepository.findAll(companySpecification, pageable);
        return companies.map(companyMapper::toListCompanyResponse);
    }

    public CompanyDTO.CompanyResponse findCompanyDetail(Integer comapanyId) {
        return companyRepository.findById(comapanyId).map(companyMapper::toCompanyResponse).orElseThrow(
                () -> new RuntimeException("Company not found")
        );
    }
}
