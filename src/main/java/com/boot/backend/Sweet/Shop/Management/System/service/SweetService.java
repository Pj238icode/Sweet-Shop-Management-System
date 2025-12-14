package com.boot.backend.Sweet.Shop.Management.System.service;

import com.boot.backend.Sweet.Shop.Management.System.dto.request.SweetRequest;
import com.boot.backend.Sweet.Shop.Management.System.dto.response.SweetResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SweetService {

    SweetResponse addSweet(SweetRequest request, MultipartFile image);

    Page<SweetResponse> getAllSweets(int page, int size, String sortBy, String direction);

    SweetResponse updateSweet(Long id, SweetRequest request, MultipartFile image);


    void deleteSweet(Long id);

    List<SweetResponse> searchSweets(String name, String category, Double minPrice, Double maxPrice);
}
