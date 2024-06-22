package com.phenikaa.jobhuntly.mapper;

import com.phenikaa.jobhuntly.dto.IndustryDTO;
import com.phenikaa.jobhuntly.entity.Industry;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IndustryMapper {
    IndustryDTO.IndustryResponse toIndustryResponse(Industry industry);
}
