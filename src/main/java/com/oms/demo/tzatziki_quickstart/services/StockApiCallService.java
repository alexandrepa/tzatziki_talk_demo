package com.oms.demo.tzatziki_quickstart.services;

import com.oms.demo.tzatziki_quickstart.beans.api.BookedItem;
import com.oms.demo.tzatziki_quickstart.beans.api.ItemToBook;
import com.oms.demo.tzatziki_quickstart.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class StockApiCallService {
    private final RestTemplate stockApiRestTemplate;

    private final AppConfig appConfig;

    public StockApiCallService(RestTemplateBuilder restTemplateBuilder, AppConfig appConfig) {
        this.stockApiRestTemplate = restTemplateBuilder.build();
        this.appConfig = appConfig;
    }

    public List<BookedItem> bookItems(String warehouse, List<ItemToBook> itemsToBook) {
        try {
            return stockApiRestTemplate.exchange(
                    appConfig.getGatewayUrl() + "/stock-api/{warehouse}/book",
                    HttpMethod.POST,
                    new HttpEntity<>(itemsToBook),
                    new ParameterizedTypeReference<List<BookedItem>>() {},
                    Map.of("warehouse", warehouse)
            ).getBody();
        } catch (RestClientException e) {
            log.warn("Had an exception while trying to book stock", e);
            return Collections.emptyList();
        }
    }
}
