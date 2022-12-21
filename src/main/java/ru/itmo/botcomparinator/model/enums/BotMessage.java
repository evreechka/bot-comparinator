package ru.itmo.botcomparinator.model.enums;

import lombok.Getter;

@Getter
public enum BotMessage {
    HELP_MESSAGE("/start - go to main menu\n".concat("/help - get command's descriptions")),
    SEND_PHOTO_MESSAGE("Please send your photo"),
    CHOOSE_CATEGORY_MESSAGE("Choose category"),
    START_MESSAGE("Start bot"),
    WAIT_MESSAGE("Photo was successfully sent. Please wait for the answer"),
    NON_COMMAND_MESSAGE("I don't know this command :(");
    private final String text;

    BotMessage(String text) {
        this.text = text;
    }
}
