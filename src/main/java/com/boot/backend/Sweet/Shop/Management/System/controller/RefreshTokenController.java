package com.boot.backend.Sweet.Shop.Management.System.controller;

import com.boot.backend.Sweet.Shop.Management.System.dto.request.RefreshTokenRequest;
import com.boot.backend.Sweet.Shop.Management.System.dto.response.RefreshTokenResponse;
import com.boot.backend.Sweet.Shop.Management.System.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;


    // Refresh Jwt Access Token
    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(
            @RequestBody RefreshTokenRequest request) {

        RefreshTokenResponse response = refreshTokenService.processRefreshToken(request);
        return ResponseEntity.ok(response);
    }


    // Logout User
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam Long userId) {

        refreshTokenService.deleteForUser(userId);

        return ResponseEntity.ok("Logged out successfully");
    }
}
