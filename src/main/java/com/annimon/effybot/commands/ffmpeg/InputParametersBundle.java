package com.annimon.effybot.commands.ffmpeg;

import com.annimon.effybot.session.Session;
import com.annimon.effybot.session.Sessions;
import com.annimon.tgbotsmodule.api.methods.Methods;
import com.annimon.tgbotsmodule.commands.CommandBundle;
import com.annimon.tgbotsmodule.commands.CommandRegistry;
import com.annimon.tgbotsmodule.commands.SimpleRegexCommand;
import com.annimon.tgbotsmodule.commands.authority.For;
import com.annimon.tgbotsmodule.commands.context.MessageContext;
import com.annimon.tgbotsmodule.commands.context.RegexMessageContext;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class InputParametersBundle implements CommandBundle<For> {

    private final Sessions sessions;

    public InputParametersBundle(Sessions sessions) {
        this.sessions = sessions;
    }

    @Override
    public void register(@NotNull CommandRegistry commands) {
        commands.register(new SimpleRegexCommand(
                Pattern.compile("^/(ss|to?) ?(\\d+|\\d{2}:\\d{2}:\\d{2})?$"),
                For.ADMIN,
                sessionCommand(this::cutCommand)));
    }

    private void cutCommand(RegexMessageContext ctx, Session session) {
        final var arg = ctx.group(2);
        final var inputParams = session.getInputParams();
        switch (ctx.group(1)) {
            case "ss" -> inputParams.setStartPosition(arg);
            case "to" -> inputParams.setEndPosition(arg);
            case "t" -> inputParams.setDuration(arg);
        }
        editMessage(ctx, session);
    }

    private void editMessage(MessageContext ctx, Session session) {
        Methods.editMessageText()
                .setChatId(session.getChatId())
                .setMessageId(session.getMessageId())
                .setText(session.toString())
                .enableHtml()
                .setReplyMarkup(ctx.message().getReplyToMessage().getReplyMarkup())
                .callAsync(ctx.sender);
    }

    private Consumer<RegexMessageContext> sessionCommand(BiConsumer<RegexMessageContext, Session> consumer) {
        return ctx -> {
            final var msg = ctx.message().getReplyToMessage();
            if (msg == null) return;

            final var session = sessions.get(msg.getChatId(), msg.getMessageId());
            if (session == null) return;

            consumer.accept(ctx, session);
        };
    }
}
