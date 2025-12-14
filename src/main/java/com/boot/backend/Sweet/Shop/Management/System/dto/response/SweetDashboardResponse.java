package com.boot.backend.Sweet.Shop.Management.System.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SweetDashboardResponse {

    private long totalSweets;
    private long totalStock;
    private long lowStockItems;
}