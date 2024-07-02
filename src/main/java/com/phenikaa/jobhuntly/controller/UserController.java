package com.phenikaa.jobhuntly.controller;

import com.phenikaa.jobhuntly.dto.ResponseDTO;
import com.phenikaa.jobhuntly.entity.User;
import com.phenikaa.jobhuntly.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

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
}
