package com.phenikaa.jobhuntly.s3;

import com.phenikaa.jobhuntly.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final S3ImageUploader s3ImageUploader;

    @PostMapping
    public ResponseDTO uploadImage(@RequestParam MultipartFile file) {
        return ResponseDTO.builder()
                .success(true)
                .message("Tải ảnh thành công")
                .data(s3ImageUploader.upload(file))
                .build();
    }

    @GetMapping("/{fileName}")
    public ResponseDTO getImage(@PathVariable String fileName) {
        return ResponseDTO.builder()
                .success(true)
                .message("Lấy link ảnh thành công")
                .data(s3ImageUploader.getImageUrl(fileName))
                .build();
    }
}
