package com.phenikaa.jobhuntly.s3;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageUploader {

    String upload(MultipartFile image);

    List<String> allFiles();

    String preSignedUrl(String fileName);
}
