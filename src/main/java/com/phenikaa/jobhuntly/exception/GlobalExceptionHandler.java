package com.phenikaa.jobhuntly.exception;

import com.phenikaa.jobhuntly.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseDTO handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseDTO.builder()
                .success(false)
                .message("Có lỗi xảy ra với server")
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
    }

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
    ResponseDTO handleInvalidBearerTokenException(Exception e) {
        return ResponseDTO.builder()
                .success(false)
                .message("Token của bạn đã hết hợp, bị thu hồi, không đúng định dạng hoặc vô hiệu vì một lý do khác")
                .code(HttpStatus.UNAUTHORIZED.value())
                .data(e.getMessage())
                .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ResponseDTO handleAccessDeniedException(Exception e) {
        return ResponseDTO.builder()
                .success(false)
                .message("Bạn không có quyền truy cập vào đường link này")
                .code(HttpStatus.FORBIDDEN.value())
                .data(e.getMessage())
                .build();
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ResponseDTO handleInsufficientAuthenticationException(Exception e) {
        return ResponseDTO.builder()
                .success(false)
                .message("Vui lòng cung cấp token")
                .code(HttpStatus.FORBIDDEN.value())
                .data(e.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        Map<String, String> errorMap = new HashMap<>();
        errors.forEach(err -> {
            String key = ((FieldError) err).getField();
            String value = err.getDefaultMessage();
            errorMap.put(key, value);
        });
        return ResponseDTO.builder()
                .success(false)
                .message("Các đối số được cung cấp không đúng, vui lòng xem data để biết thêm chi tiết")
                .code(HttpStatus.EXPECTATION_FAILED.value())
                .data(errorMap)
                .build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseDTO handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return ResponseDTO.builder()
                .success(false)
                .message("Email này đã được sử dụng để đăng ký tài khoản rồi")
                .code(HttpStatus.BAD_REQUEST.value())
                .data(e.getMessage())
                .build();
    }

    @ExceptionHandler(SharedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseDTO handleApplicationAlreadyExistsException(SharedException e) {
        return ResponseDTO.builder()
                .success(false)
                .message(e.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .data(e.getMessage())
                .build();
    }

}
