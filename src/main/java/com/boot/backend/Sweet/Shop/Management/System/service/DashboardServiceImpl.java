package com.boot.backend.Sweet.Shop.Management.System.service;

import com.boot.backend.Sweet.Shop.Management.System.dto.response.SweetDashboardResponse;
import com.boot.backend.Sweet.Shop.Management.System.repository.SweetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final SweetRepository sweetRepository;

    private static final int LOW_STOCK_THRESHOLD = 5;

    @Override
    public SweetDashboardResponse getSweetDashboardStats() {

        return SweetDashboardResponse.builder()
                .totalSweets(sweetRepository.countTotalSweets())
                .totalStock(sweetRepository.sumTotalStock())
                .lowStockItems(
                        sweetRepository.countLowStock(LOW_STOCK_THRESHOLD)
                )
                .build();
    }
}
