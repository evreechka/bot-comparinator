package ru.itmo.botcomparinator.yandex.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    List<ImageEntity> findAllByCategory(String category);
}
