package com.boot.backend.Sweet.Shop.Management.System.controller;

import com.boot.backend.Sweet.Shop.Management.System.dto.response.ApiResponse;
import com.boot.backend.Sweet.Shop.Management.System.dto.response.SweetDashboardResponse;
import com.boot.backend.Sweet.Shop.Management.System.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/sweets")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SweetDashboardResponse>> getSweetDashboard() {

        SweetDashboardResponse data =
                dashboardService.getSweetDashboardStats();

        return ResponseEntity.ok(
                new ApiResponse<>(true, data, LocalDateTime.now())
        );
    }
}
