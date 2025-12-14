package com.boot.backend.Sweet.Shop.Management.System.controller;

import com.boot.backend.Sweet.Shop.Management.System.dto.request.InventoryRequest;
import com.boot.backend.Sweet.Shop.Management.System.dto.request.SweetRequest;
import com.boot.backend.Sweet.Shop.Management.System.dto.response.ApiResponse;
import com.boot.backend.Sweet.Shop.Management.System.dto.response.SweetResponse;
import com.boot.backend.Sweet.Shop.Management.System.service.InventoryService;
import com.boot.backend.Sweet.Shop.Management.System.service.SweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sweets")
@RequiredArgsConstructor
public class SweetController {

    private final SweetService sweetService;
    private final InventoryService inventoryService;


    // ADD SWEET (ADMIN ONLY)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SweetResponse>> addSweet(
            @Valid @RequestBody SweetRequest request) {

        SweetResponse response = sweetService.addSweet(request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, response, LocalDateTime.now())
        );
    }


    // GET ALL SWEETS
    @GetMapping
    public ResponseEntity<ApiResponse<Page<SweetResponse>>> getAllSweets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        direction = direction.equalsIgnoreCase("desc") ? "desc" : "asc";


        Page<SweetResponse> sweets =
                sweetService.getAllSweets(page, size, sortBy, direction);

        return ResponseEntity.ok(
                new ApiResponse<>(true, sweets, LocalDateTime.now())
        );
    }



    // SEARCH SWEETS
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<SweetResponse>>> searchSweets(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {

        List<SweetResponse> result =
                sweetService.searchSweets(name, category, minPrice, maxPrice);

        return ResponseEntity.ok(
                new ApiResponse<>(true, result, LocalDateTime.now())
        );
    }


    // UPDATE SWEET (ADMIN ONLY)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SweetResponse>> updateSweet(
            @PathVariable Long id,
            @Valid @RequestBody SweetRequest request) {

        SweetResponse response = sweetService.updateSweet(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, response, LocalDateTime.now())
        );
    }


    // DELETE SWEET (ADMIN ONLY)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteSweet(
            @PathVariable Long id) {

        sweetService.deleteSweet(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Sweet deleted successfully", LocalDateTime.now())
        );
    }


    // PURCHASE SWEET
    @PostMapping("/{id}/purchase")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<String>> purchaseSweet(
            @PathVariable Long id,
            @Valid @RequestBody InventoryRequest request) {

        inventoryService.purchaseSweet(id, request.getQuantity());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Purchase successful", LocalDateTime.now())
        );
    }

    // RESTOCK SWEET (ADMIN ONLY)
    @PostMapping("/{id}/restock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> restockSweet(
            @PathVariable Long id,
            @Valid  @RequestBody InventoryRequest request) {

        inventoryService.restockSweet(id, request.getQuantity());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Restock successful", LocalDateTime.now())
        );
    }
}
