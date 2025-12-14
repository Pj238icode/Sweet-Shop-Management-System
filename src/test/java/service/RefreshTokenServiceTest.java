package service;

import com.boot.backend.Sweet.Shop.Management.System.config.JwtProperties;
import com.boot.backend.Sweet.Shop.Management.System.dto.request.RefreshTokenRequest;
import com.boot.backend.Sweet.Shop.Management.System.dto.response.RefreshTokenResponse;
import com.boot.backend.Sweet.Shop.Management.System.entity.RefreshToken;
import com.boot.backend.Sweet.Shop.Management.System.entity.Role;
import com.boot.backend.Sweet.Shop.Management.System.entity.User;
import com.boot.backend.Sweet.Shop.Management.System.exception.RefreshTokenExpiredException;
import com.boot.backend.Sweet.Shop.Management.System.exception.RefreshTokenNotFoundException;
import com.boot.backend.Sweet.Shop.Management.System.exception.UserNotFoundException;
import com.boot.backend.Sweet.Shop.Management.System.repository.RefreshTokenRepository;
import com.boot.backend.Sweet.Shop.Management.System.repository.UserRepository;
import com.boot.backend.Sweet.Shop.Management.System.security.JwtHelper;
import com.boot.backend.Sweet.Shop.Management.System.service.RefreshTokenServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceImplTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtHelper jwtHelper;

    @Mock
    private JwtProperties jwtProperties;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    // =====================================================
    // CREATE REFRESH TOKEN
    // =====================================================

    @Test
    void shouldCreateRefreshTokenSuccessfully() {
        User user = User.builder()
                .id(1L)
                .email("pj@test.com")
                .role(Role.ROLE_USER)
                .build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(jwtProperties.getRefreshTokenExpiration())
                .thenReturn(600000L);

        when(refreshTokenRepository.save(any(RefreshToken.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        String token = refreshTokenService.createAndReturnToken(1L);

        assertNotNull(token);

        verify(refreshTokenRepository).deleteByUserIdNative(1L);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundWhileCreatingToken() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> refreshTokenService.createAndReturnToken(1L));
    }

    // =====================================================
    // PROCESS REFRESH TOKEN
    // =====================================================

    @Test
    void shouldGenerateNewAccessTokenSuccessfully() {
        User user = User.builder()
                .id(1L)
                .email("pj@test.com")
                .role(Role.ROLE_USER)
                .build();

        RefreshToken token = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(Instant.now().plusSeconds(300))
                .build();

        RefreshTokenRequest request =
                new RefreshTokenRequest("pj@test.com", token.getToken());

        when(refreshTokenRepository.findByToken(token.getToken()))
                .thenReturn(Optional.of(token));

        when(jwtHelper.generateToken(any(), any()))
                .thenReturn("new-access-token");

        RefreshTokenResponse response =
                refreshTokenService.processRefreshToken(request);

        assertEquals("new-access-token", response.getAccessToken());
        assertEquals(token.getToken(), response.getRefreshToken());
    }

    @Test
    void shouldThrowExceptionWhenRefreshTokenNotFound() {
        RefreshTokenRequest request =
                new RefreshTokenRequest("pj@test.com", "invalid");

        when(refreshTokenRepository.findByToken("invalid"))
                .thenReturn(Optional.empty());

        assertThrows(RefreshTokenNotFoundException.class,
                () -> refreshTokenService.processRefreshToken(request));
    }

    @Test
    void shouldThrowExceptionWhenEmailDoesNotMatchTokenUser() {
        User user = User.builder()
                .email("real@test.com")
                .build();

        RefreshToken token = RefreshToken.builder()
                .token("token")
                .user(user)
                .expiryDate(Instant.now().plusSeconds(300))
                .build();

        RefreshTokenRequest request =
                new RefreshTokenRequest("fake@test.com", "token");

        when(refreshTokenRepository.findByToken("token"))
                .thenReturn(Optional.of(token));

        assertThrows(RefreshTokenNotFoundException.class,
                () -> refreshTokenService.processRefreshToken(request));
    }

    @Test
    void shouldThrowExceptionWhenRefreshTokenExpired() {
        User user = User.builder()
                .email("pj@test.com")
                .build();

        RefreshToken token = RefreshToken.builder()
                .token("expired")
                .user(user)
                .expiryDate(Instant.now().minusSeconds(10))
                .build();

        RefreshTokenRequest request =
                new RefreshTokenRequest("pj@test.com", "expired");

        when(refreshTokenRepository.findByToken("expired"))
                .thenReturn(Optional.of(token));

        assertThrows(RefreshTokenExpiredException.class,
                () -> refreshTokenService.processRefreshToken(request));

        verify(refreshTokenRepository).delete(token);
    }

    // =====================================================
    // LOGOUT
    // =====================================================

    @Test
    void shouldDeleteRefreshTokenForUserSuccessfully() {
        when(userRepository.existsById(1L))
                .thenReturn(true);

        refreshTokenService.deleteForUser(1L);

        verify(refreshTokenRepository).deleteByUserIdNative(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingTokenForNonExistingUser() {
        when(userRepository.existsById(1L))
                .thenReturn(false);

        assertThrows(UserNotFoundException.class,
                () -> refreshTokenService.deleteForUser(1L));
    }

}
