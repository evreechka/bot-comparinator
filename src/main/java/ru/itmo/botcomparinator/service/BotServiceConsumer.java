package ru.itmo.botcomparinator.service;

import lombok.SneakyThrows;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import ru.itmo.botcomparinator.PhotoBot;
import ru.itmo.botcomparinator.config.TelegramConfigProperties;
import ru.itmo.botcomparinator.model.ResultDto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;

@Service
public class BotServiceConsumer {
    private final String telegramUrl;
    private final String telegramBotToken;
    private final RestTemplate restTemplate;
    private final PhotoBot photoBot;
    BotServiceConsumer(TelegramConfigProperties telegramConfigProperties,
                       PhotoBot photoBot) {
        this.telegramUrl = telegramConfigProperties.getUrl();
        this.telegramBotToken = telegramConfigProperties.getBotToken();
        this.restTemplate = new RestTemplate();
        this.photoBot = photoBot;
    }
    @KafkaListener(groupId = "photo-bot", topics = "response_compare_topic")
    @SneakyThrows
    public void consumePhotoResponse(ResultDto resultDto) {
        System.out.println("Receive from kafka result");
        System.out.println(resultDto.getChatId());
        System.out.println(Arrays.toString(resultDto.getResponsePhoto()));
//        ByteArrayResource byteArrayResource = new ByteArrayResource(resultDto.getResponsePhoto());
//        uploadFile(resultDto.getChatId(), byteArrayResource);
//        File dir = new File("./uploads");
//        dir.mkdirs();
//        File newFile = new File("./uploads/photo");
//        newFile.canWrite();
//        newFile.canRead();
//        FileUtils.writeByteArrayToFile(newFile, resultDto.getResponsePhoto());
//        SendDocument sendDocument = new SendDocument();
//        sendDocument.setChatId(resultDto.getChatId());
//        sendDocument.setDocument(new InputFile(newFile));
//        sendDocument.setCaption(resultDto.getMessage());
//        dir.delete();
//        photoBot.sendPhoto(sendDocument);
        SendPhoto photo = new SendPhoto();
        photo.setPhoto(new InputFile(new ByteArrayInputStream(resultDto.getResponsePhoto()), "photo"));
        photo.setChatId(resultDto.getChatId());
        photoBot.sendPhoto(photo);
    }

    private void uploadFile(String chatId, ByteArrayResource value) {
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("photo", value);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);

//        try {
            restTemplate.exchange(
                    MessageFormat.format("{0}bot{1}/sendPhoto?chat_id={2}", telegramUrl, telegramBotToken, chatId),
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

//        } catch (Exception e) {
//            photoBot.sendMessage(chatId, "Some errors :(");
//        }
    }

    public static ByteArrayResource createPhotoFileResource(byte[] array)
            throws IOException {
        return new ByteArrayResource(array) {
            @Override
            public String getFilename() {
                return "photo.jpg";
            }
        };
    }

//    private static Path createPhotoFile(byte[] array) throws IOException {
//        File file = File.createTempFile("photo", "jpg");
//        FileUtils.writeByteArrayToFile(file, array);
//        return file.toPath();
//    }
}
