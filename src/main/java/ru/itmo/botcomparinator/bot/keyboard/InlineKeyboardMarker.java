package ru.itmo.botcomparinator.bot.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.itmo.botcomparinator.model.enums.Category;

import java.util.ArrayList;
import java.util.List;

@Component
public class InlineKeyboardMarker {

    public InlineKeyboardMarkup getInlineMessageButtonsWithTemplate() {
        return getInlineMessageButtons();
    }

    public InlineKeyboardMarkup getInlineMessageButtons() {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (Category category : Category.values()) {
            rowList.add(getButton(
                    category.getCategoryName(),
                    category.getCategoryName()
            ));
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    private List<InlineKeyboardButton> getButton(String buttonName, String buttonCallBackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonName);
        button.setCallbackData(buttonCallBackData);

        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(button);
        return keyboardButtonsRow;
    }
}