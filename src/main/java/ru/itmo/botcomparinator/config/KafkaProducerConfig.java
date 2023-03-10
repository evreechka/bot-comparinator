package ru.itmo.botcomparinator.config;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.itmo.botcomparinator.model.PhotoDto;
import ru.itmo.botcomparinator.model.ResultDto;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class KafkaProducerConfig {
    public Map<String, Object> producerConfigs() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "rc1a-8or9ad4qd22jf1gu.mdb.yandexcloud.net:9092");
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        configProps.put(SaslConfigs.SASL_MECHANISM, "SCRAM-SHA-512");
        configProps.put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"user_mari\" password=\"qwerty12345\";");
        return configProps;
    }

    @Bean(name = "yandexWorkerProducerFactory")
    public ProducerFactory<String, ResultDto> yandexWorkerProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean(name = "kafkaYandexWorkerTemplate")
    public KafkaTemplate<String, ResultDto> kafkaYandexWorkerTemplate() {
        return new KafkaTemplate<>(yandexWorkerProducerFactory());
    }

    @Bean(name = "botProducerFactory")
    public ProducerFactory<String, PhotoDto> botProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean(name = "kafkaBotTemplate")
    public KafkaTemplate<String, PhotoDto> kafkaBotTemplate() {
        return new KafkaTemplate<>(botProducerFactory());
    }
}
