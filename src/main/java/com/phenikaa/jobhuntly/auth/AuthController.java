package com.phenikaa.jobhuntly.auth;

import com.phenikaa.jobhuntly.dto.ExchangeTokenDTO;
import com.phenikaa.jobhuntly.dto.ResponseDTO;
import com.phenikaa.jobhuntly.entity.User;
import com.phenikaa.jobhuntly.enums.TokenType;
import com.phenikaa.jobhuntly.event.ForgotPasswordEvent;
import com.phenikaa.jobhuntly.event.RegistrationCompleteEvent;
import com.phenikaa.jobhuntly.service.TokenService;
import com.phenikaa.jobhuntly.service.UserService;
import com.sun.net.httpserver.Authenticator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ApplicationEventPublisher eventPublisher;
    private final TokenService tokenService;
    private final UserService userService;


    @PostMapping("/login")
    public ResponseDTO login(@RequestBody AuthDTO.LoginRequest userLogin) {
        Map<String, Object> response = authService.login(userLogin);

        return ResponseDTO.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Đăng nhập thành công")
                .data(response)
                .build();
    }

    @PostMapping("/register")
    public ResponseDTO register(@Valid @RequestBody AuthDTO.RegisterRequest userRegister) {
        User user = authService.register(userRegister);
        eventPublisher.publishEvent(new RegistrationCompleteEvent(user));
        return ResponseDTO.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Đăng kí thành công, vui lòng kiểm tra email để xác nhận tài khoản")
                .build();

    }

    @PutMapping("/verifyEmail")
    public ResponseDTO verifyEmail(@RequestParam String token) {
        tokenService.checkToken(token, TokenType.VERIFICATION_TOKEN);
        return ResponseDTO.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Xác nhận email thành công, vui lòng đăng nhập để sử dụng sản phẩm")
                .build();
    }

    @GetMapping("/forgotPassword")
    public ResponseDTO forgotPassword(@RequestParam String email) {
        User user = userService.getUser(email);
        eventPublisher.publishEvent(new ForgotPasswordEvent(user));
        return ResponseDTO.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Vui lòng kiểm tra email để thay đổi mật khẩu mới")
                .build();
    }

    @PutMapping("/resetPassword")
    public ResponseDTO resetPassword(@RequestParam String token,@RequestBody AuthDTO.ResetPasswordRequest request) {
        tokenService.resetUserPassword(request.password(), token, TokenType.RESET_PASSWORD_TOKEN);
        return ResponseDTO.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Thay đổi mật khẩu thành công")
                .build();
    }


    @PostMapping("/outbound/authentication")
    public ResponseDTO outboundAuthenticate(@RequestParam("code") String code, @RequestParam("role") String role) {
        ExchangeTokenDTO.ExchangeTokenResponse response = authService.outboundAuthenticate(code, role);
        return ResponseDTO.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Đăng nhập thành công")
                .data(response)
                .build();
    }

    @GetMapping("/resendRegisterToken")
    public ResponseDTO resendRegisterToken(@RequestParam String email) {
        User user = userService.getUser(email);
        eventPublisher.publishEvent(new RegistrationCompleteEvent(user));
        return ResponseDTO.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Đã gửi lại token, vui lòng kiểm tra email")
                .build();
    }
}