package com.phenikaa.jobhuntly.mapper;

import com.phenikaa.jobhuntly.dto.JobDTO;
import com.phenikaa.jobhuntly.entity.Industry;
import com.phenikaa.jobhuntly.entity.Job;
import com.phenikaa.jobhuntly.entity.JobCategory;
import com.phenikaa.jobhuntly.enums.ApplicationStatus;
import com.phenikaa.jobhuntly.repository.ApplicationRepository;
import com.phenikaa.jobhuntly.s3.S3ImageUploader;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class JobMapper {

    @Autowired
    private S3ImageUploader s3ImageUploader;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Mapping(target = "categories", source = "categories", qualifiedByName = "mapCategories")
    @Mapping(source = "company.logo", target = "company.logo", qualifiedByName = "convertLogoInJobToUrl")
    @Mapping(target = "numberOfHired", expression = "java(determineHiredApplicant(job))")
    public abstract JobDTO.JobResponse toJobResponse(Job job);

    @Mapping(target = "categories", ignore = true)
    public abstract Job toJob(JobDTO.JobRequest jobRequest);


    @Named("mapCategories")
    List<String> mapCategories(Set<JobCategory> categories) {
        return categories.stream().map(
            jobCategory -> jobCategory.getCategory().getName()
        ).toList();
    }

    @Named("convertLogoInJobToUrl")
    String convertLogoToUrl(String logo) {  // Removed @Context annotation
        if (logo != null) {
            return s3ImageUploader.getImageUrl(logo);  // Assuming a static method
        }
        return "";
    }

    Integer determineHiredApplicant(Job job) {
        return applicationRepository.findByJobAndStatus(job, ApplicationStatus.HIRED).size();
    }

}
