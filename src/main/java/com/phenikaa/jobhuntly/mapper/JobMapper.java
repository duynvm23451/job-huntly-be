package com.phenikaa.jobhuntly.mapper;

import com.phenikaa.jobhuntly.dto.JobDTO;
import com.phenikaa.jobhuntly.entity.Job;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobMapper {
    JobDTO.JobResponse toJobResponse(Job job);
}
