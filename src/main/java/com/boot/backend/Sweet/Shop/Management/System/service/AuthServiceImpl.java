package com.boot.backend.Sweet.Shop.Management.System.service;

import com.boot.backend.Sweet.Shop.Management.System.dto.request.LoginRequest;
import com.boot.backend.Sweet.Shop.Management.System.dto.request.RegisterRequest;
import com.boot.backend.Sweet.Shop.Management.System.dto.response.JwtResponse;
import com.boot.backend.Sweet.Shop.Management.System.entity.Role;
import com.boot.backend.Sweet.Shop.Management.System.entity.User;
import com.boot.backend.Sweet.Shop.Management.System.exception.InvalidCredentialsException;
import com.boot.backend.Sweet.Shop.Management.System.exception.UserAlreadyExistsException;
import com.boot.backend.Sweet.Shop.Management.System.repository.UserRepository;
import com.boot.backend.Sweet.Shop.Management.System.security.CustomUserDetails;
import com.boot.backend.Sweet.Shop.Management.System.security.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final JwtHelper jwtHelper;
    private final ModelMapper modelMapper;

    @Override
    public JwtResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already registered!");
        }

        User user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);

        userRepository.save(user);

        CustomUserDetails userDetails = new CustomUserDetails(user);

        String token = jwtHelper.generateToken(null, userDetails);
        String refreshToken = refreshTokenService.createAndReturnToken(user.getId());

        return JwtResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public JwtResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password!");
        }

        CustomUserDetails userDetails = new CustomUserDetails(user);

        String token = jwtHelper.generateToken(null, userDetails);
        String refreshToken = refreshTokenService.createAndReturnToken(user.getId());

        return JwtResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }
}
