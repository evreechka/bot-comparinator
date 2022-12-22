package ru.itmo.botcomparinator.yandex.api;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.itmo.botcomparinator.model.PhotoDto;
import ru.itmo.botcomparinator.model.ResultDto;
import ru.itmo.botcomparinator.yandex.db.ImageEntity;
import ru.itmo.botcomparinator.yandex.photo_comparator.ImageComparatorService;

import java.math.BigInteger;
import java.util.List;

@Service
@Slf4j
public class YandexFaceWorker extends MessageListenerAdapter {
    private final ImageComparatorService imageComparatorService;
    private final KafkaTemplate<String, ResultDto> kafkaYandexWorkerTemplate;

    YandexFaceWorker(ImageComparatorService imageComparatorService,
                     KafkaTemplate<String, ResultDto> kafkaYandexWorkerTemplate) {
        this.imageComparatorService = imageComparatorService;
        this.kafkaYandexWorkerTemplate = kafkaYandexWorkerTemplate;
    }

    @SneakyThrows
    @KafkaListener(groupId = "photo-bot", topics = "request_compare_topic")
    public void consumePhotoRequest(PhotoDto photoDto) {
        BigInteger currentPhotoHash = imageComparatorService.getImageHash(photoDto.getPhotoData());
        List<ImageEntity> photosFromDb = imageComparatorService.getPhotosByCategory(photoDto.getCategory());
        BigInteger min = imageComparatorService.compareImage(photosFromDb.get(0).getPhoto(), currentPhotoHash);
        ImageEntity selectedPhoto = photosFromDb.get(0);
        for (ImageEntity imageEntity : photosFromDb) {
            BigInteger difference = imageComparatorService.compareImage(imageEntity.getPhoto(), currentPhotoHash);
            if (min.compareTo(difference) > 0) {
                min = difference;
                selectedPhoto = imageEntity;
            }
        }
        ResultDto resultDto = new ResultDto();
        resultDto.setChatId(photoDto.getChatId());
        resultDto.setCaption(selectedPhoto.getDescription());
        resultDto.setResponsePhoto(selectedPhoto.getPhoto());
        kafkaYandexWorkerTemplate.send("response_compare_topic", resultDto);
    }
}
