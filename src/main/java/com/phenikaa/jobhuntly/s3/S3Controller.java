package com.phenikaa.jobhuntly.s3;

import com.phenikaa.jobhuntly.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final ImageUploader uploader;

    @PostMapping
    public ResponseDTO uploadImage(@RequestParam MultipartFile file) {
        return ResponseDTO.builder()
                .success(true)
                .message("Tải ảnh thành công")
                .data(uploader.upload(file))
                .build();
    }
}
