package com.phenikaa.jobhuntly.controller;

import com.phenikaa.jobhuntly.dto.ResponseDTO;
import com.phenikaa.jobhuntly.dto.UserDTO;
import com.phenikaa.jobhuntly.entity.User;
import com.phenikaa.jobhuntly.mapper.UserMapper;
import com.phenikaa.jobhuntly.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping("/users/company/{companyId}")
    public ResponseDTO findByCompanyId(@PathVariable Integer companyId) {
        return ResponseDTO.builder()
                .success(true)
                .message("Tìm kiếm người dùng theo Id của công ty thành công")
                .build();
    }

    @GetMapping("/getMyInfo")
    public ResponseDTO getMyInfo(@AuthenticationPrincipal Jwt jwt) {
        User user = userService.getUser(jwt.getClaim("email"));
        UserDTO.UserResponse response = userMapper.toUserResponse(user);
        return ResponseDTO.builder()
                .success(true)
                .message("Lấy thông tin người dùng thành công")
                .data(response)
                .build();
    }
}
