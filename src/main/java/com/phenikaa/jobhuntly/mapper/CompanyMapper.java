package com.phenikaa.jobhuntly.mapper;

import com.phenikaa.jobhuntly.dto.CompanyDTO;
import com.phenikaa.jobhuntly.entity.Company;
import com.phenikaa.jobhuntly.s3.S3ImageUploader;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {JobMapper.class, UserMapper.class})
public abstract class CompanyMapper {

    @Autowired
    S3ImageUploader s3ImageUploader;

    @Mapping(source = "industries", target = "industries")
    @Mapping(source = "users", target = "users")
    @Mapping(source = "logo", target = "logo", qualifiedByName = "convertLogoToUrl")
    public abstract CompanyDTO.CompanyResponse toCompanyResponse(Company company);

    @Mapping(source = "industries", target = "industries")
    @Mapping(target = "availableJobs", expression = "java(calculateAvailableJobs(company))")
    @Mapping(source = "logo", target = "logo", qualifiedByName = "convertLogoToUrl")
    public abstract CompanyDTO.ListCompanyResponse toListCompanyResponse(Company company);

    Integer calculateAvailableJobs(Company company) {
        return company.getJobs().size();
    }

    @Named("convertLogoToUrl")
    String convertLogoToUrl(String logo) {  // Removed @Context annotation
        if (logo != null) {
            return s3ImageUploader.getImageUrl(logo);  // Assuming a static method
        }
        return "";
    }
}
