package com.phenikaa.jobhuntly.mapper;

import com.phenikaa.jobhuntly.dto.ApplicationDto;
import com.phenikaa.jobhuntly.entity.Application;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = JobMapper.class)
public interface ApplicationMapper {
    ApplicationDto.ApplicationResponse toApplicationResponse(Application application);
}
