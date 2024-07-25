package com.phenikaa.jobhuntly.mapper;

import com.phenikaa.jobhuntly.dto.ChatDto;
import com.phenikaa.jobhuntly.entity.Message;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    ChatDto.MessageResponse toMessageResponse(Message message);
}
