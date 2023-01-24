package com.oms.demo.tzatziki_quickstart.services;

import com.oms.demo.tzatziki_quickstart.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
public class PriceApiCallService {
    private final RestTemplate priceApiRestTemplate;
    private final AppConfig appConfig;

    public PriceApiCallService(RestTemplateBuilder restTemplateBuilder, AppConfig appConfig) {
        this.priceApiRestTemplate = restTemplateBuilder.build();
        this.appConfig = appConfig;
    }

    public Double getPrice(String warehouse, String itemId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("warehouse", warehouse);
        try {

            return priceApiRestTemplate.exchange(
                    appConfig.getGatewayUrl() + "/price-api/items/{itemId}",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    Double.class,
                    Map.of("itemId", itemId)
            ).getBody();
        } catch (RestClientException e) {
            log.warn("Had an exception while trying to get pricing", e);
            return -1d;
        }
    }
}
