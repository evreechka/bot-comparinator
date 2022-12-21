package ru.itmo.botcomparinator.model.enums;

import lombok.Getter;

@Getter
public enum Category {
    CAT("cat"),
    ANIME("anime");

    private final String categoryName;

    Category(String categoryName) {
        this.categoryName = categoryName;
    }
}
