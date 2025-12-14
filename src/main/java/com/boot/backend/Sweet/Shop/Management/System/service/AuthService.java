package com.boot.backend.Sweet.Shop.Management.System.service;

import com.boot.backend.Sweet.Shop.Management.System.dto.request.LoginRequest;
import com.boot.backend.Sweet.Shop.Management.System.dto.request.RegisterRequest;
import com.boot.backend.Sweet.Shop.Management.System.dto.response.JwtResponse;

public interface AuthService {

    JwtResponse register(RegisterRequest request);

    JwtResponse login(LoginRequest request);
}
