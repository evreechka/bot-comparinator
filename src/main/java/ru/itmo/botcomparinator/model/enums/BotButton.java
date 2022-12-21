package ru.itmo.botcomparinator.model.enums;

import lombok.Getter;

@Getter
public enum BotButton {
    CHOOSE_CATEGORY_BUTTON("Choose category"),
    HELP_BUTTON("Help");

    private final String name;

    BotButton(String name) {
        this.name = name;
    }
}
