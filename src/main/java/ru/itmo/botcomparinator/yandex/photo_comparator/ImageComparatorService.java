package ru.itmo.botcomparinator.yandex.photo_comparator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.botcomparinator.yandex.db.ImageEntity;
import ru.itmo.botcomparinator.yandex.db.ImageRepository;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageComparatorService {
    private static final String HEXES = "0123456789ABCDEF";
    private final ImageRepository imageRepository;

    public static String getHexString(byte[] raw) {
        if (raw == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for (final byte b : raw) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4))
                    .append(HEXES.charAt((b & 0x0F)));
        }
        System.out.println(hex);
        return hex.toString();
    }

    public BigInteger compareImage(byte[] photoData, BigInteger currentPhotoHash) throws IOException, NoSuchAlgorithmException {
        return getImageHash(photoData).subtract(currentPhotoHash).abs();
    }

    public BigInteger getImageHash(byte[] photoData) throws IOException, NoSuchAlgorithmException {

        System.out.println("Start MD5 Digest");
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(photoData);
        byte[] hash = md.digest();
        return new BigInteger(getHexString(hash), 16);
    }                                       // Belongs to main class

    public List<ImageEntity> getPhotosByCategory(String category) {
        return imageRepository.findAllByCategory(category);
    }
}
