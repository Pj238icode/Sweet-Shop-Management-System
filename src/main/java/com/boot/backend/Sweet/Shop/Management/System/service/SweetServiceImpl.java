package com.boot.backend.Sweet.Shop.Management.System.service;

import com.boot.backend.Sweet.Shop.Management.System.dto.request.SweetRequest;
import com.boot.backend.Sweet.Shop.Management.System.dto.response.SweetResponse;
import com.boot.backend.Sweet.Shop.Management.System.entity.Sweet;
import com.boot.backend.Sweet.Shop.Management.System.exception.SweetNotFoundException;
import com.boot.backend.Sweet.Shop.Management.System.repository.SweetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SweetServiceImpl implements SweetService {

    private final SweetRepository sweetRepository;
    private final ImageService imageService;

    @Override
    public SweetResponse addSweet(SweetRequest request, MultipartFile image) {

        String imageUrl = imageService.uploadImage(image);

        Sweet sweet = Sweet.builder()
                .name(request.getName())
                .category(request.getCategory())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .imageUrl(imageUrl)
                .build();

        sweetRepository.save(sweet);
        return toResponse(sweet);
    }



    @Override
    public Page<SweetResponse> getAllSweets(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        PageRequest pageable = PageRequest.of(page, size, sort);

        return sweetRepository.findAll(pageable)
                .map(this::toResponse);
    }

    @Override
    public SweetResponse updateSweet(
            Long id,
            SweetRequest request,
            MultipartFile image
    ) {

        Sweet sweet = sweetRepository.findById(id)
                .orElseThrow(() -> new SweetNotFoundException("Sweet not found!"));

        // ===== UPDATE FIELDS =====
        sweet.setName(request.getName());
        sweet.setCategory(request.getCategory());
        sweet.setPrice(request.getPrice());
        sweet.setQuantity(request.getQuantity());

        // ===== UPDATE IMAGE (OPTIONAL) =====
        if (image != null && !image.isEmpty()) {

            String imageUrl = imageService.uploadImage(image);
            sweet.setImageUrl(imageUrl);
        }

        sweetRepository.save(sweet);

        return toResponse(sweet);
    }


    @Override
    public void deleteSweet(Long id) {
        Sweet sweet = sweetRepository.findById(id)
                .orElseThrow(() -> new SweetNotFoundException("Sweet not found!"));

        sweetRepository.delete(sweet);
    }

    @Override
    public List<SweetResponse> searchSweets(String name, String category, Double minPrice, Double maxPrice) {

        if (name != null) return sweetRepository.findByNameContainingIgnoreCase(name).stream().map(this::toResponse).toList();
        if (category != null) return sweetRepository.findByCategoryIgnoreCase(category).stream().map(this::toResponse).toList();
        if (minPrice != null && maxPrice != null)
            return sweetRepository.findByPriceBetween(minPrice, maxPrice).stream().map(this::toResponse).toList();

        return List.of();
    }

    private SweetResponse toResponse(Sweet sweet) {
        return SweetResponse.builder()
                .id(sweet.getId())
                .name(sweet.getName())
                .category(sweet.getCategory())
                .price(sweet.getPrice())
                .quantity(sweet.getQuantity())
                .imageUrl(sweet.getImageUrl())
                .build();
    }
}
