package com.phenikaa.jobhuntly.controller;

import com.phenikaa.jobhuntly.dto.ResponseDTO;
import com.phenikaa.jobhuntly.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ConfigController {
    private final ConfigService configService;

    @GetMapping("/config")
    public ResponseDTO getConfigs() {
        Map<String, List<String>> response = configService.getConfigurations();
        return ResponseDTO.builder()
                .success(true)
                .message("Lấy thông tin thiết lập thành công")
                .code(HttpStatus.OK.value())
                .data(response)
                .build();
    }
}
