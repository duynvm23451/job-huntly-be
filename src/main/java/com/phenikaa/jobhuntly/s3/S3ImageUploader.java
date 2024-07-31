package com.phenikaa.jobhuntly.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.phenikaa.jobhuntly.exception.SharedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ImageUploader implements ImageUploader {

    private final AmazonS3 s3;

    @Value("${app.s3.bucket}")
    private String bucketName;

    @Override
    public String upload(MultipartFile image) {
        if (image == null) {
            throw new SharedException("không có ảnh nào được tải lên!!!");
        }

        String actualFilename = image.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + actualFilename.substring(actualFilename.lastIndexOf("."));

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(image.getSize());
        try {
            PutObjectResult putObjectResult = s3.putObject(
                    new PutObjectRequest(bucketName, fileName, image.getInputStream(), metadata)
            );
            return fileName;
        } catch (IOException e) {
            throw new SharedException("Xảy ra lỗi khi upload ảnh: " + e.getMessage());
        }
    }

    @Override
    public List<String> allFiles() {
        return List.of();
    }

    @Override
    public String preSignedUrl() {
        return "";
    }
}
