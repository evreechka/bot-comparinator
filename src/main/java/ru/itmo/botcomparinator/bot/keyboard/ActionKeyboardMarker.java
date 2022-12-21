package ru.itmo.botcomparinator.bot.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.itmo.botcomparinator.model.enums.BotButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class ActionKeyboardMarker {
    public ReplyKeyboardMarkup getMainMenuKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(BotButton.CHOOSE_CATEGORY_BUTTON.getName()));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(BotButton.HELP_BUTTON.getName()));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);

        final ReplyKeyboardMarkup actionKeyboardMarkup = new ReplyKeyboardMarkup();
        actionKeyboardMarkup.setKeyboard(keyboard);
        actionKeyboardMarkup.setSelective(true);
        actionKeyboardMarkup.setResizeKeyboard(true);
        actionKeyboardMarkup.setOneTimeKeyboard(false);
        return actionKeyboardMarkup;
    }
}