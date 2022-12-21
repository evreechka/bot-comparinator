package ru.itmo.botcomparinator;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.itmo.botcomparinator.bot.handler.CallbackQueryHandler;
import ru.itmo.botcomparinator.bot.handler.MessageHandler;
import ru.itmo.botcomparinator.config.TelegramConfigProperties;
@Getter
@Setter
@Component
@Slf4j
public class PhotoBot extends TelegramLongPollingBot {
    private String botUsername;
    private String botToken;
    private MessageHandler messageHandler;
    private CallbackQueryHandler callbackQueryHandler;

        public PhotoBot(TelegramBotsApi telegramBotsApi,
                        MessageHandler messageHandler,
                        CallbackQueryHandler callbackQueryHandler,
                        TelegramConfigProperties telegramConfigProperties) throws TelegramApiException {
        this.messageHandler = messageHandler;
        this.callbackQueryHandler = callbackQueryHandler;
        this.botUsername = telegramConfigProperties.getBotName();
        this.botToken = telegramConfigProperties.getBotToken();

        telegramBotsApi.registerBot(this);
    }

    private SendMessage handleUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return callbackQueryHandler.processCallbackQuery(callbackQuery);
        } else {
            Message message = update.getMessage();
            if (message != null) {
                return messageHandler.answerMessage(update.getMessage());
            }
        }
        return null;
    }

    public void sendPhoto(SendPhoto sendPhoto) {
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            execute(handleUpdate(update));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
