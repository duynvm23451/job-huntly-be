package com.phenikaa.jobhuntly.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ResponseDTO {

    private boolean success;

    private String message;

    private Integer code;

    private Object data;
}
