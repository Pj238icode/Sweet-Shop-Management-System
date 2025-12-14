package com.boot.backend.Sweet.Shop.Management.System.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "aws")
public class AwsProperties {

    private String accessKey;
    private String secretKey;
    private String region;
    private String bucket;
}
