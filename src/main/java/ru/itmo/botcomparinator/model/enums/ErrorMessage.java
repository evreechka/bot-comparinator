package ru.itmo.botcomparinator.model.enums;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    ERROR_MESSAGE("Service is unavailable. Try again later"),
    EMPTY_INPUT_TEXT_MESSAGE("Incorrect message!"),
    INCORRECT_DOC_FORMAT_MESSAGE("You can send only: .jpg, .jpeg, .png"),
    CATEGORY_IS_NOT_SELECTED("Category is not selected!");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
