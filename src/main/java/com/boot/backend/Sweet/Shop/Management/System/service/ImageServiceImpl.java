package com.boot.backend.Sweet.Shop.Management.System.service;

import com.boot.backend.Sweet.Shop.Management.System.config.AwsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final S3Client s3Client;

    private final AwsProperties awsProperties;

    private static final String SWEET_FOLDER = "Sweets/";

    @Override
    public String uploadImage(MultipartFile file) {

        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String key = SWEET_FOLDER + fileName;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(awsProperties.getBucket())
                    .key(key)
                    .contentType(file.getContentType())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(
                            file.getInputStream(),
                            file.getSize()
                    )
            );

            // Public URL
            return "https://" + awsProperties.getBucket() +
                    ".s3.ap-south-1.amazonaws.com/" + key;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload image to S3");
        }
    }
}
