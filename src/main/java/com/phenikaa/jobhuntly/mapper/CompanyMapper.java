package com.phenikaa.jobhuntly.mapper;

import com.phenikaa.jobhuntly.dto.CompanyDTO;
import com.phenikaa.jobhuntly.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {JobMapper.class})
public interface CompanyMapper {
    @Mapping(source = "industries", target = "industries")
    CompanyDTO.CompanyResponse toCompanyResponse(Company company);

    @Mapping(source = "industries", target = "industries")
    @Mapping(target = "availableJobs", expression = "java(calculateAvailableJobs(company))")
    CompanyDTO.ListCompanyResponse toListCompanyResponse(Company company);

    default Integer calculateAvailableJobs(Company company) {
        return company.getJobs().size();
    }
}
