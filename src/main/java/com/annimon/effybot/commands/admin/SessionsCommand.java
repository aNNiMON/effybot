package com.annimon.effybot.commands.admin;

import com.annimon.effybot.Permissions;
import com.annimon.effybot.TextUtils;
import com.annimon.effybot.session.MediaSession;
import com.annimon.effybot.session.Session;
import com.annimon.effybot.session.Sessions;
import com.annimon.effybot.session.YtDlpSession;
import com.annimon.tgbotsmodule.commands.TextCommand;
import com.annimon.tgbotsmodule.commands.authority.For;
import com.annimon.tgbotsmodule.commands.context.MessageContext;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SessionsCommand implements TextCommand {
    private final Sessions sessions;

    public SessionsCommand(Sessions sessions) {
        this.sessions = sessions;
    }

    @Override
    public String command() {
        return "/sessions";
    }

    @SuppressWarnings("unchecked")
    @Override
    public EnumSet<For> authority() {
        return Permissions.SUPERUSERS;
    }

    @Override
    public void accept(@NotNull MessageContext ctx) {
        final int size = sessions.getSize();
        if (size == 0) {
            ctx.replyToMessage("No sessions found").callAsync(ctx.sender);
            return;
        }


        if (ctx.argument(1).equals("clear")) {
            sessions.clear();
            ctx.replyToMessage("%d sessions cleared".formatted(size)).callAsync(ctx.sender);
        } else {
            final var sessionsInfo = sessions.sessions()
                    .sorted(Comparator.comparing(Session::getInstant).reversed())
                    .map(this::formatSession)
                    .limit(30)
                    .collect(Collectors.joining("\n"));
            ctx.replyToMessage(("%d active sessions:\n%s").formatted(size, sessionsInfo))
                    .disableWebPagePreview()
                    .setSingleRowInlineKeyboard(InlineKeyboardButton.builder()
                            .text("Clear").callbackData(SessionsClearCommand.CALLBACK_NAME)
                            .build())
                    .callAsync(ctx.sender);
        }
    }

    private String formatSession(Session session) {
        long chatId = session.getChatId();
        if (session instanceof MediaSession ms) {
            return String.join(" ", str(chatId), str(ms.getOriginalFilename()),
                    str(ms.getFileSize(), TextUtils::readableFileSize),
                    str(ms.getDuration(), TextUtils::readableDuration));
        } else if (session instanceof YtDlpSession ys) {
            return String.join(" ", str(chatId), str(ys.getUrl()));
        }
        return str(chatId);
    }

    private String str(Object obj) {
        return obj == null ? "" : Objects.toString(obj);
    }

    private <T> String str(T obj, Function<? super T, String> func) {
        return obj == null ? "" : str(func.apply(obj));
    }
}
