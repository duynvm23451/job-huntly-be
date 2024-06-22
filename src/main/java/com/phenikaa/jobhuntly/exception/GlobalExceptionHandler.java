package com.phenikaa.jobhuntly.exception;

import com.phenikaa.jobhuntly.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
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

}
