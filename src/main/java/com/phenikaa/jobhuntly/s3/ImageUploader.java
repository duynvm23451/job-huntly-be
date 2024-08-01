package com.phenikaa.jobhuntly.s3;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageUploader {

    String upload(MultipartFile image);

    String getImageUrl(String fileName);

    String preSignedUrl(String fileName);
}
