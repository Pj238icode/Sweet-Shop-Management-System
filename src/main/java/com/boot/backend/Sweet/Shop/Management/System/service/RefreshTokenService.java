package com.boot.backend.Sweet.Shop.Management.System.service;

import com.boot.backend.Sweet.Shop.Management.System.dto.request.RefreshTokenRequest;
import com.boot.backend.Sweet.Shop.Management.System.dto.response.RefreshTokenResponse;
import com.boot.backend.Sweet.Shop.Management.System.entity.RefreshToken;
import com.boot.backend.Sweet.Shop.Management.System.entity.User;
import com.boot.backend.Sweet.Shop.Management.System.exception.RefreshTokenExpiredException;
import com.boot.backend.Sweet.Shop.Management.System.exception.RefreshTokenNotFoundException;
import com.boot.backend.Sweet.Shop.Management.System.exception.UserNotFoundException;
import com.boot.backend.Sweet.Shop.Management.System.repository.RefreshTokenRepository;
import com.boot.backend.Sweet.Shop.Management.System.repository.UserRepository;
import com.boot.backend.Sweet.Shop.Management.System.security.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;

    @Value("${security.jwt.refresh-token-expiration}")
    private long refreshTokenDurationMs;


    // CREATE REFRESH TOKEN
    public String createAndReturnToken(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        refreshTokenRepository.deleteByUserIdNative(userId);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();

        return refreshTokenRepository.save(refreshToken).getToken();
    }


    // PROCESS REFRESH REQUEST
    public RefreshTokenResponse processRefreshToken(RefreshTokenRequest request) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RefreshTokenNotFoundException("Invalid refresh token!"));


        if (!refreshToken.getUser().getEmail().equals(request.getEmail())) {
            throw new RefreshTokenNotFoundException("Refresh token does not belong to this user!");
        }

        RefreshToken verifiedToken = verifyExpiration(refreshToken);

        User user = verifiedToken.getUser();

        // Generate new access token
        String newAccessToken = jwtHelper.generateToken(
                null,
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        user.isAdmin()
                                ? java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_ADMIN"))
                                : java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER"))
                )
        );


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
    public void deleteForUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        refreshTokenRepository.deleteByUserIdNative(userId);
    }
}
