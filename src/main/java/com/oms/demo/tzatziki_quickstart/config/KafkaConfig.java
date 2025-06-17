package com.oms.demo.tzatziki_quickstart.config;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.RequiredArgsConstructor;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {
    private final AppConfig appConfig;

    @Bean
    public KafkaTemplate<String, GenericRecord> avroKafkaTemplate() {
        Map<String, Object> props = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, appConfig.getKafkaBootstrapUrl(),
                "schema.registry.url", appConfig.getKafkaSchemaRegistryUrl(),
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class
        );

        DefaultKafkaProducerFactory<String, GenericRecord> defaultKafkaProducerFactory = new DefaultKafkaProducerFactory<>(props);
        return new KafkaTemplate<>(defaultKafkaProducerFactory);
    }
}
