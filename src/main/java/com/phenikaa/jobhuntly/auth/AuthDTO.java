package com.phenikaa.jobhuntly.auth;

import com.phenikaa.jobhuntly.validation.PasswordMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class AuthDTO {
    public record LoginRequest(String email, String password) {
    }

    public record RegisterRequest(
            @Email(message = "Email không nhập đúng định dạng")
            String email,
            @NotNull(message = "Username không được để trống")
            String username,
            @NotNull(message = "Mật khẩu không được để trôống")
            String password) {}

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