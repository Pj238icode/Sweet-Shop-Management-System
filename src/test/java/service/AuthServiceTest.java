package service;

import com.boot.backend.Sweet.Shop.Management.System.dto.request.LoginRequest;
import com.boot.backend.Sweet.Shop.Management.System.dto.request.RegisterRequest;
import com.boot.backend.Sweet.Shop.Management.System.dto.response.JwtResponse;
import com.boot.backend.Sweet.Shop.Management.System.entity.Role;
import com.boot.backend.Sweet.Shop.Management.System.entity.User;
import com.boot.backend.Sweet.Shop.Management.System.exception.InvalidCredentialsException;
import com.boot.backend.Sweet.Shop.Management.System.exception.UserAlreadyExistsException;
import com.boot.backend.Sweet.Shop.Management.System.repository.UserRepository;
import com.boot.backend.Sweet.Shop.Management.System.security.JwtHelper;
import com.boot.backend.Sweet.Shop.Management.System.service.AuthServiceImpl;
import com.boot.backend.Sweet.Shop.Management.System.service.RefreshTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private JwtHelper jwtHelper;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    // ---------------- REGISTER ----------------

    @Test
    void shouldRegisterUserSuccessfully() {
        // Arrange
        RegisterRequest request = new RegisterRequest(
                "PJ",
                "pj@test.com",
                "password"
        );

        User mappedUser = User.builder()
                .id(1L)
                .name("PJ")
                .email("pj@test.com")
                .password("password")
                .role(Role.ROLE_USER)
                .build();

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());

        when(modelMapper.map(request, User.class))
                .thenReturn(mappedUser);

        when(passwordEncoder.encode(any()))
                .thenReturn("encoded-password");

        when(jwtHelper.generateToken(any(), any()))
                .thenReturn("jwt-token");

        when(refreshTokenService.createAndReturnToken(1L))
                .thenReturn("refresh-token");

        when(userRepository.save(any(User.class)))
                .thenReturn(mappedUser);

        // Act
        JwtResponse response = authService.register(request);

        // Assert
        assertNotNull(response);
        assertEquals("pj@test.com", response.getEmail());
        assertEquals("jwt-token", response.getToken());
        assertEquals("refresh-token", response.getRefreshToken());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest(
                "PJ",
                "pj@test.com",
                "password"
        );

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class,
                () -> authService.register(request));
    }

    // ---------------- LOGIN ----------------

    @Test
    void shouldLoginSuccessfullyWithValidCredentials() {
        LoginRequest request =
                new LoginRequest("pj@test.com", "password");

        User user = User.builder()
                .id(1L)
                .email("pj@test.com")
                .password("encoded-password")
                .role(Role.ROLE_USER)
                .build();

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()))
                .thenReturn(true);

        when(jwtHelper.generateToken(any(), any()))
                .thenReturn("jwt-token");

        when(refreshTokenService.createAndReturnToken(1L))
                .thenReturn("refresh-token");

        // Act
        JwtResponse response = authService.login(request);

        // Assert
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("refresh-token", response.getRefreshToken());
    }

    @Test
    void shouldThrowExceptionWhenEmailNotFound() {
        LoginRequest request =
                new LoginRequest("wrong@test.com", "password");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class,
                () -> authService.login(request));
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsIncorrect() {
        LoginRequest request =
                new LoginRequest("pj@test.com", "wrong-password");

        User user = User.builder()
                .id(1L)
                .email("pj@test.com")
                .password("encoded-password")
                .build();

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(any(), any()))
                .thenReturn(false);

        assertThrows(InvalidCredentialsException.class,
                () -> authService.login(request));
    }
}
