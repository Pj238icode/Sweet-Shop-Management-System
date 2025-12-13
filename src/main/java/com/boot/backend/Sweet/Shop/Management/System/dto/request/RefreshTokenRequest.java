package com.boot.backend.Sweet.Shop.Management.System.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class RefreshTokenRequest {
    private String email;
    private String refreshToken;
}