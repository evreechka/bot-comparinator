package ru.itmo.botcomparinator.bot.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.itmo.botcomparinator.model.enums.BotMessage;
import ru.itmo.botcomparinator.service.CategoryService;

@Component
@RequiredArgsConstructor
public class CallbackQueryHandler {
    private final CategoryService categoryService;

    public SendMessage processCallbackQuery(CallbackQuery buttonQuery) {
        final String chatId = buttonQuery.getMessage().getChatId().toString();

        String data = buttonQuery.getData();
        categoryService.saveCategory(chatId, data);
        return new SendMessage(chatId, BotMessage.SEND_PHOTO_MESSAGE.getText());
    }
}