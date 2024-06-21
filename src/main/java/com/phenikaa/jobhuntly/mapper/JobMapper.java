package com.phenikaa.jobhuntly.mapper;

import com.phenikaa.jobhuntly.dto.JobDTO;
import com.phenikaa.jobhuntly.entity.Job;
import com.phenikaa.jobhuntly.entity.JobCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface JobMapper {
    @Mapping(target = "categories", source = "categories", qualifiedByName = "mapCategories")
    JobDTO.JobResponse toJobResponse(Job job);

    @Named("mapCategories")
    default List<String> mapCategories(Set<JobCategory> categories) {
        return categories.stream().map(
            jobCategory -> jobCategory.getCategory().getName()
        ).toList();
    }
}
