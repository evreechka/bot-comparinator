package ru.itmo.botcomparinator.bot.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.itmo.botcomparinator.bot.keyboard.ActionKeyboardMarker;
import ru.itmo.botcomparinator.bot.keyboard.InlineKeyboardMarker;
import ru.itmo.botcomparinator.model.enums.BotButton;
import ru.itmo.botcomparinator.model.enums.BotMessage;
import ru.itmo.botcomparinator.model.enums.ErrorMessage;
import ru.itmo.botcomparinator.service.BotServiceProducer;
import ru.itmo.botcomparinator.service.CategoryService;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageHandler {
    private final ActionKeyboardMarker actionKeyboardMarker;
    private final InlineKeyboardMarker inlineKeyboardMarker;
    private final BotServiceProducer botServiceProducer;
    private final CategoryService categoryService;

    public SendMessage answerMessage(Message message) {
        String chatId = message.getChatId().toString();

        if (message.hasDocument()) {
            if (!isValidFileFormat(message.getDocument())) {
                return new SendMessage(chatId, ErrorMessage.INCORRECT_DOC_FORMAT_MESSAGE.getMessage());
            }
            if (categoryService.isCategorySelected(chatId)) {
                return sendPhoto(chatId, message.getDocument().getFileId());
            }
            return new SendMessage(chatId, ErrorMessage.CATEGORY_IS_NOT_SELECTED.getMessage());
        }

        String inputText = message.getText();

        if (inputText == null) {
            return new SendMessage(chatId, ErrorMessage.EMPTY_INPUT_TEXT_MESSAGE.getMessage());
        } else if (inputText.equals("/start")) {
            return getStartMessage(chatId);
        } else if (inputText.equals(BotButton.CHOOSE_CATEGORY_BUTTON.getName())) {
            return chooseCategory(chatId);
        } else if (inputText.equals(BotButton.HELP_BUTTON.getName()) || inputText.equals("/help")) {
            SendMessage sendMessage = new SendMessage(chatId, BotMessage.HELP_MESSAGE.getText());
            sendMessage.enableMarkdown(true);
            return sendMessage;
        } else {
            return new SendMessage(chatId, BotMessage.NON_COMMAND_MESSAGE.getText());
        }
    }

    private SendMessage getStartMessage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, BotMessage.START_MESSAGE.getText());
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(actionKeyboardMarker.getMainMenuKeyboard());
        return sendMessage;
    }

    private SendMessage chooseCategory(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, BotMessage.CHOOSE_CATEGORY_MESSAGE.getText());
        sendMessage.setReplyMarkup(inlineKeyboardMarker.getInlineMessageButtonsWithTemplate());
        return sendMessage;
    }

    private SendMessage sendPhoto(String chatId, String photoId) {
        try {
            botServiceProducer.sendPhoto(chatId, photoId, categoryService.getCategory(chatId));
            categoryService.deleteCategory(chatId);
            return new SendMessage(chatId, BotMessage.WAIT_MESSAGE.getText());
        } catch (Exception e) {
            return new SendMessage(chatId, ErrorMessage.ERROR_MESSAGE.getMessage());
        }
    }

    private boolean isValidFileFormat(Document document) {
        return document.getFileName().endsWith(".jpg")
                || document.getFileName().endsWith(".jpeg")
                || document.getFileName().endsWith(".png");
    }
}
