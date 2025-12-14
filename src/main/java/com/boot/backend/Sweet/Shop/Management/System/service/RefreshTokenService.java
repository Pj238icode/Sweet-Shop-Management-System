package com.boot.backend.Sweet.Shop.Management.System.service;

import com.boot.backend.Sweet.Shop.Management.System.dto.request.RefreshTokenRequest;
import com.boot.backend.Sweet.Shop.Management.System.dto.response.RefreshTokenResponse;

public interface RefreshTokenService {

    // Create a new refresh token for the user
    String createAndReturnToken(Long userId);

    // Process refresh request and return a new access token
    RefreshTokenResponse processRefreshToken(RefreshTokenRequest request);

    // Delete refresh token for a specific user (logout)
    void deleteForUser(Long userId);
}
