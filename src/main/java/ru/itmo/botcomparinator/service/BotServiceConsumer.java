package ru.itmo.botcomparinator.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import ru.itmo.botcomparinator.PhotoBot;
import ru.itmo.botcomparinator.model.ResultDto;

import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
public class BotServiceConsumer {
    private final PhotoBot photoBot;
    @KafkaListener(groupId = "photo-bot", topics = "response_compare_topic")
    @SneakyThrows
    public void consumePhotoResponse(ResultDto resultDto) {
        SendPhoto photo = new SendPhoto();
        photo.setPhoto(new InputFile(new ByteArrayInputStream(resultDto.getResponsePhoto()), "photo"));
        photo.setChatId(resultDto.getChatId());
        photo.setCaption(resultDto.getCaption());
        photoBot.sendPhoto(photo);
    }
}
