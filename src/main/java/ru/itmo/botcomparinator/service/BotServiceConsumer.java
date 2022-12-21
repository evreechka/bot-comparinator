package ru.itmo.botcomparinator.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import ru.itmo.botcomparinator.PhotoBot;
import ru.itmo.botcomparinator.model.ResultDto;

import java.io.File;

@Service
@RequiredArgsConstructor
public class BotServiceConsumer {

    private final PhotoBot photoBot;
    @KafkaListener(groupId = "photo-bot", topics = "response_compare_topic")
    @SneakyThrows
    public void consumePhotoResponse(ResultDto resultDto) {
        System.out.println("Receive from kafka result");
        System.out.println(resultDto.getChatId());
        System.out.println(resultDto.getMessage());
        File dir = new File("./uploads");
        dir.mkdirs();
        File newFile = new File("./uploads/photo.jpg");
        newFile.canWrite();
        newFile.canRead();
        FileUtils.writeByteArrayToFile(newFile, resultDto.getResponsePhoto());
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(resultDto.getChatId());
        sendDocument.setDocument(new InputFile("./uploads/photo.jpg"));
        sendDocument.setCaption(resultDto.getMessage());

        photoBot.sendPhoto(sendDocument);
    }
}
