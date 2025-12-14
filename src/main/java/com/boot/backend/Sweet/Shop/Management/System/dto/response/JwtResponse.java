package com.boot.backend.Sweet.Shop.Management.System.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {

    private Long id;
    private String name;
    private String email;
    private String role;
    private String token;
    private String refreshToken;


}
