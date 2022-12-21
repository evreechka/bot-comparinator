package ru.itmo.botcomparinator.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.itmo.botcomparinator.model.PhotoDto;
import ru.itmo.botcomparinator.model.ResultDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {
    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "rc1a-8or9ad4qd22jf1gu.mdb.yandexcloud.net:9091");
        configProps.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        configProps.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "photo-bot");
        configProps.put("security.protocol", "SASL_PLAINTEXT");
        configProps.put("sasl.mechanisms", "SCRAM-SHA-512");
        configProps.put("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"user_mari\" password=\"qwerty12345\";");
        return configProps;
    }

    @Bean
    public ConsumerFactory<String, ResultDto> botConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public ConsumerFactory<String, PhotoDto> yandexWorkerConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

//    @Bean(name = "kafkaListenerYandexWorkerContainerFactory")
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PhotoDto> kafkaListenerYandexWorkerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PhotoDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(yandexWorkerConsumerFactory());
        return factory;
    }

//    @Bean(name = "kafkaListenerBotContainerFactory")
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ResultDto> kafkaListenerBotContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ResultDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(botConsumerFactory());
        return factory;
    }
}
