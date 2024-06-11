package com.phenikaa.jobhuntly.auth;

import com.phenikaa.jobhuntly.dto.ResponseDTO;
import com.sun.net.httpserver.Authenticator;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseDTO login(@RequestBody AuthDTO.LoginRequest userLogin) throws IllegalAccessException {
        Map<String, Object> response = authService.login(userLogin);

        return ResponseDTO.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Đăng nhập thành công")
                .data(response)
                .build();
    }

    @PostMapping("/register")
    public ResponseDTO register(@Valid @RequestBody AuthDTO.RegisterRequest userRegister) throws IllegalAccessException {
        authService.register(userRegister);
        return ResponseDTO.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Đăng kí thành công, vui lòng kiểm tra email để xác nhận tài khoản")
                .build();

    }
}