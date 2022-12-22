package ru.itmo.botcomparinator.yandex.db;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "photo_storage")
public class ImageEntity {
    @Id
    @GeneratedValue
    @Column(name = "photo_id")
    private long photoId;

    @Column(name = "photo_category")
    private String category;

    @Lob
    @Column(name = "photo")
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] photo;
}
