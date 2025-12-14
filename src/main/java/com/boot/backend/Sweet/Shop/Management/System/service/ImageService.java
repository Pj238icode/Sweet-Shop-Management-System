package com.boot.backend.Sweet.Shop.Management.System.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String uploadImage(MultipartFile file);
}
