package com.boot.backend.Sweet.Shop.Management.System.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SweetResponse {
    private Long id;
    private String name;
    private String category;
    private double price;
    private int quantity;
}