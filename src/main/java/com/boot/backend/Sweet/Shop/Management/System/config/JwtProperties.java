package com.boot.backend.Sweet.Shop.Management.System.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
@Data
public class JwtProperties {
    private String secretKey;
    private long expiration;
    private long refreshTokenExpiration;
}
