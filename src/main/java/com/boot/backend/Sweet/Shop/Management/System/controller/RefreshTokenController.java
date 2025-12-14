package com.boot.backend.Sweet.Shop.Management.System.controller;

import com.boot.backend.Sweet.Shop.Management.System.dto.request.RefreshTokenRequest;
import com.boot.backend.Sweet.Shop.Management.System.dto.response.ApiResponse;
import com.boot.backend.Sweet.Shop.Management.System.dto.response.RefreshTokenResponse;
import com.boot.backend.Sweet.Shop.Management.System.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {

        RefreshTokenResponse response = refreshTokenService.processRefreshToken(request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, response, LocalDateTime.now())
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestParam Long userId) {

        refreshTokenService.deleteForUser(userId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Logged out successfully", LocalDateTime.now())
        );
    }
}
