package ru.itmo.botcomparinator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.ApiResponse;
import ru.itmo.botcomparinator.config.TelegramConfigProperties;
import ru.itmo.botcomparinator.model.PhotoDto;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.Objects;

@Service
@Slf4j
public class BotServiceProducer {
    private final String telegramUrl;
    private final String telegramBotToken;
    private final RestTemplate restTemplate;
    private final KafkaTemplate<String, PhotoDto> kafkaTemplate;


    BotServiceProducer(TelegramConfigProperties telegramConfigProperties,
                       KafkaTemplate<String, PhotoDto> kafkaYandexWorkerTemplate) {
        this.telegramUrl = telegramConfigProperties.getUrl();
        this.telegramBotToken = telegramConfigProperties.getBotToken();
        this.kafkaTemplate = kafkaYandexWorkerTemplate;
        this.restTemplate = new RestTemplate();
    }

    public void sendPhoto(String chatId, String photoId) {
        System.out.println("Send to kafka queue photoDto");
        PhotoDto photoDto = new PhotoDto(chatId, getDocumentFile(photoId), chatId);
        kafkaTemplate.send("request_compare_topic", photoDto);
    }

    private byte[] getDocumentFile(String fileId) {
        return restTemplate.execute(
                Objects.requireNonNull(getDocumentTelegramFileUrl(fileId)),
                HttpMethod.GET,
                null,
                clientHttpResponse -> {
                    ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
                    StreamUtils.copy(clientHttpResponse.getBody(), byteArrayInputStream);
                    return byteArrayInputStream.toByteArray();
                });
    }

    private String getDocumentTelegramFileUrl(String fileId) {
        ResponseEntity<ApiResponse<org.telegram.telegrambots.meta.api.objects.File>> response = restTemplate.exchange(
                MessageFormat.format("{0}bot{1}/getFile?file_id={2}", telegramUrl, telegramBotToken, fileId),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        return Objects.requireNonNull(response.getBody()).getResult().getFileUrl(this.telegramBotToken);
    }
}
