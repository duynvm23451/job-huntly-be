package com.phenikaa.jobhuntly.mapper;

import com.phenikaa.jobhuntly.auth.AuthDTO;
import com.phenikaa.jobhuntly.entity.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    User toUser(AuthDTO.RegisterRequest request);

    AuthDTO.UserLoginResponse toUserLoginResponse(User user);
}
