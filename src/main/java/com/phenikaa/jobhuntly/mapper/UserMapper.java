package com.phenikaa.jobhuntly.mapper;

import com.phenikaa.jobhuntly.dto.UserDTO;
import com.phenikaa.jobhuntly.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO.UserResponse toUserResponse(User user);
}
