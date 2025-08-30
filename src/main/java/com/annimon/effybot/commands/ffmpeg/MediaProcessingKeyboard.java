package com.annimon.effybot.commands.ffmpeg;

import com.annimon.effybot.parameters.Parameter;
import com.annimon.effybot.parameters.Parameters;
import com.annimon.effybot.session.MediaSession;
import com.annimon.effybot.session.YtDlpSession;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.annimon.effybot.commands.ffmpeg.CallbackQueryCommands.*;

public class MediaProcessingKeyboard {
    private static final int BUTTON_COLUMNS = 2;

    private MediaProcessingKeyboard() { }

    public static InlineKeyboardMarkup createKeyboard(MediaSession session) {
        final var selectedParam = session.getSelectedParam();
        if (selectedParam != null) {
            return createParamKeyboard(selectedParam);
        } else {
            return createParamsListKeyboard(session.getParams());
        }
    }

    private static InlineKeyboardMarkup createParamsListKeyboard(Parameters params) {
        final var keyboard = new ArrayList<InlineKeyboardRow>();
        final var it = params.iterator();
        while (it.hasNext()) {
            final var row = new InlineKeyboardRow();
            for (int i = 0; i < BUTTON_COLUMNS; i++) {
                if (it.hasNext()) {
                    final var param = it.next();
                    final String paramId = param.getId();
                    row.add(inlineKeyboardButton(param.describe(), callbackData(PARAMETER, paramId)));
                }
            }
            keyboard.add(row);
        }
        keyboard.add(new InlineKeyboardRow(inlineKeyboardButton("Process", callbackData(PROCESS))));
        return new InlineKeyboardMarkup(keyboard);
    }

    private static InlineKeyboardMarkup createParamKeyboard(Parameter<?> param) {
        final var keyboard = new ArrayList<InlineKeyboardRow>();
        final String paramId = param.getId();
        final int maxSize = param.getPossibleValuesSize();
        int index = 0;
        final int columnsCount = param.defaultColumnsCount();
        while (index < maxSize) {
            final var row = new InlineKeyboardRow();
            for (int i = 0; i < columnsCount; i++) {
                if (index < maxSize) {
                    String value = param.describeValueByIndex(index);
                    row.add(inlineKeyboardButton(value, callbackData(PARAMETER, callbackParams(paramId, index))));
                    index++;
                }
            }
            keyboard.add(row);
        }
        keyboard.add(new InlineKeyboardRow(inlineKeyboardButton("Back", callbackData(PARAMETER))));
        return new InlineKeyboardMarkup(keyboard);
    }

    public static InlineKeyboardMarkup createKeyboard(YtDlpSession session) {
        final var keyboard = new ArrayList<InlineKeyboardRow>();
        if (!session.hasAdditionalInfo()) {
            keyboard.add(new InlineKeyboardRow(inlineKeyboardButton("Get info", callbackData(YTDLP_INFO))));
        }
        keyboard.add(new InlineKeyboardRow(inlineKeyboardButton("Start", callbackData(YTDLP_START))));
        return new InlineKeyboardMarkup(keyboard);
    }

    private static InlineKeyboardButton inlineKeyboardButton(String text, String callbackData) {
        final var button = new InlineKeyboardButton(text);
        button.setCallbackData(callbackData);
        return button;
    }

    private static String callbackData(String command) {
        return command;
    }

    @SuppressWarnings("SameParameterValue")
    private static String callbackData(String command, String data) {
        return command + ":" + data;
    }

    private static String callbackParams(Object... params) {
        return Arrays.stream(params).map(Objects::toString).collect(Collectors.joining(" "));
    }
}
