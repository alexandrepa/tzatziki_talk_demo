package com.oms.demo.tzatziki_quickstart.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "default-properties")
public class AppConfig {
    String gatewayUrl;
    String kafkaBootstrapUrl;
    String kafkaSchemaRegistryUrl;
}
