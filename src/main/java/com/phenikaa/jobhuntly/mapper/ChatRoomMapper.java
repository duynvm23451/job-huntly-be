package com.phenikaa.jobhuntly.mapper;

import com.phenikaa.jobhuntly.dto.ChatDto;
import com.phenikaa.jobhuntly.entity.ChatRoom;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatRoomMapper {
    ChatDto.ChatRoomResponse toChatRoomResponse(ChatRoom chatRoom);
}
