package com.phenikaa.jobhuntly.mapper;

import com.phenikaa.jobhuntly.dto.ApplicationDto;
import com.phenikaa.jobhuntly.entity.Application;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {
    ApplicationDto.ApplicationResponse toApplicationResponse(Application application);
}
