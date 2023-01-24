package com.oms.demo.tzatziki_quickstart;

import com.oms.demo.tzatziki_quickstart.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppConfig.class)
public class TzatzikiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TzatzikiApplication.class, args);
    }

}
