package com.phenikaa.jobhuntly.exception;

import com.phenikaa.jobhuntly.dto.ResponseDTO;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseDTO handleObjectNotFoundException(ObjectNotFoundException e) {
        return ResponseDTO.builder()
                .success(false)
                .message(e.getMessage())
                .code(HttpStatus.NOT_FOUND.value())
                .build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ResponseDTO handleBadRequestException(Exception e) {
        return ResponseDTO.builder()
                .success(false)
                .message("Email hoặc mật khẩu không chính xác")
                .code(HttpStatus.UNAUTHORIZED.value())
                .data(e.getMessage())
                .build();
    }

    @ExceptionHandler(AccountStatusException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ResponseDTO handleAccountStatusException(Exception e) {
        return ResponseDTO.builder()
                .success(false)
                .message("Tài khoản của bạn chưa được kích hoạt")
                .code(HttpStatus.UNAUTHORIZED.value())
                .data(e.getMessage())
                .build();
    }

    @ExceptionHandler(InvalidBearerTokenException.class)
    ResponseDTO handleInvalidBearerTokenException(InvalidBearerTokenException e) {
        return ResponseDTO.builder()
                .success(false)
                .message("Token của bạn đã hết hợp, bị thu hồi, không đúng định dạng hoặc vô hiệu vì một lý do khác")
                .code(HttpStatus.UNAUTHORIZED.value())
                .data(e.getMessage())
                .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ResponseDTO handleAccessDeniedException(AccessDeniedException e) {
        return ResponseDTO.builder()
                .success(false)
                .message("Bạn không có quyền truy cập vào đường link này")
                .code(HttpStatus.FORBIDDEN.value())
                .data(e.getMessage())
                .build();
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ResponseDTO handleInsufficientAuthenticationException(InsufficientAuthenticationException e) {
        return ResponseDTO.builder()
                .success(false)
                .message("Vui lòng cung cấp token")
                .code(HttpStatus.FORBIDDEN.value())
                .data(e.getMessage())
                .build();
    }

}
