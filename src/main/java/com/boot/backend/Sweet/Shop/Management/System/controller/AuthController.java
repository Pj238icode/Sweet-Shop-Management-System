package com.boot.backend.Sweet.Shop.Management.System.controller;

import com.boot.backend.Sweet.Shop.Management.System.dto.request.LoginRequest;
import com.boot.backend.Sweet.Shop.Management.System.dto.request.RegisterRequest;
import com.boot.backend.Sweet.Shop.Management.System.dto.response.ApiResponse;
import com.boot.backend.Sweet.Shop.Management.System.dto.response.JwtResponse;
import com.boot.backend.Sweet.Shop.Management.System.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<JwtResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        JwtResponse response = authService.register(request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, response, LocalDateTime.now())
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        JwtResponse response = authService.login(request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, response, LocalDateTime.now())
        );
    }
}
