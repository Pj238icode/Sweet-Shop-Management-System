package com.boot.backend.Sweet.Shop.Management.System.service;

import com.boot.backend.Sweet.Shop.Management.System.config.JwtProperties;
import com.boot.backend.Sweet.Shop.Management.System.dto.request.RefreshTokenRequest;
import com.boot.backend.Sweet.Shop.Management.System.dto.response.RefreshTokenResponse;
import com.boot.backend.Sweet.Shop.Management.System.entity.RefreshToken;
import com.boot.backend.Sweet.Shop.Management.System.entity.User;
import com.boot.backend.Sweet.Shop.Management.System.exception.RefreshTokenExpiredException;
import com.boot.backend.Sweet.Shop.Management.System.exception.RefreshTokenNotFoundException;
import com.boot.backend.Sweet.Shop.Management.System.exception.UserNotFoundException;
import com.boot.backend.Sweet.Shop.Management.System.repository.RefreshTokenRepository;
import com.boot.backend.Sweet.Shop.Management.System.repository.UserRepository;
import com.boot.backend.Sweet.Shop.Management.System.security.CustomUserDetails;
import com.boot.backend.Sweet.Shop.Management.System.security.JwtHelper;
import com.boot.backend.Sweet.Shop.Management.System.security.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;
    private final JwtProperties jwtProperties;

    // CREATE REFRESH TOKEN
    @Override
    public String createAndReturnToken(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        // delete existing token (one token per user)
        refreshTokenRepository.deleteByUserIdNative(userId);

        RefreshToken newToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(jwtProperties.getRefreshTokenExpiration()))
                .build();

        return refreshTokenRepository.save(newToken).getToken();
    }

    // PROCESS REFRESH TOKEN REQUEST
    @Override
    public RefreshTokenResponse processRefreshToken(RefreshTokenRequest request) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RefreshTokenNotFoundException("Invalid refresh token!"));

        // Validate user-email match
        if (!refreshToken.getUser().getEmail().equals(request.getEmail())) {
            throw new RefreshTokenNotFoundException("Refresh token does not belong to this user!");
        }

        RefreshToken verifiedToken = verifyExpiration(refreshToken);
        User user = verifiedToken.getUser();

        // build CustomUserDetails (SOLID)
        CustomUserDetails userDetails = new CustomUserDetails(user);

        // generate new access token
        String newAccessToken = jwtHelper.generateToken(null, userDetails);

        return RefreshTokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(verifiedToken.getToken())
                .build();
    }

    // VERIFY EXPIRATION
    private RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenExpiredException("Refresh token expired. Please login again!");
        }
        return token;
    }

    // LOGOUT
    @Override
    public void deleteForUser(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found!");
        }

        refreshTokenRepository.deleteByUserIdNative(userId);
    }
}
