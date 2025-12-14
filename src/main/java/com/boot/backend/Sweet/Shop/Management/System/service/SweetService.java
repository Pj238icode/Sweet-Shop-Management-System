package com.boot.backend.Sweet.Shop.Management.System.service;

import com.boot.backend.Sweet.Shop.Management.System.dto.request.SweetRequest;
import com.boot.backend.Sweet.Shop.Management.System.dto.response.SweetResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SweetService {

    SweetResponse addSweet(SweetRequest request);

    Page<SweetResponse> getAllSweets(int page, int size, String sortBy, String direction);

    SweetResponse updateSweet(Long id, SweetRequest request);

    void deleteSweet(Long id);

    List<SweetResponse> searchSweets(String name, String category, Double minPrice, Double maxPrice);
}
