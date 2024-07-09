package com.phenikaa.jobhuntly.auth;

import com.phenikaa.jobhuntly.enums.Role;
import com.phenikaa.jobhuntly.validation.PasswordMatch;
import com.phenikaa.jobhuntly.validation.ValidEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class AuthDTO {
    public record LoginRequest(String email, String password) {
    }
    @PasswordMatch(message = "Mật khẩu và mật khẩu xác nhận phải giống nhau")
    public record RegisterRequest(
            @Email(message = "Email không nhập đúng định dạng")
            String email,
            @NotNull(message = "Username không được để trống")
            String username,
            @NotNull(message = "Mật khẩu không được để trống")
            String password,
            @NotNull(message = "Mật khẩu xác nhận không được để trống")
            String passwordConfirmation,
            @NotNull(message = "Role không được để trống")
            @ValidEnum(enumClass = Role.class, message = "Role không hợp lệ")
            String role
            ) {}

    public record UserLoginResponse(
            String email,
            String username
    ) {}

    @PasswordMatch(message = "Mật khẩu và mật khẩu xác nhận phải giống nhau")
    public record ResetPasswordRequest(
            String password,
            String passwordConfirmation
    ) {}
}