package com.annimon.ffmpegbot.commands.ytdlp;

import com.annimon.tgbotsmodule.commands.context.CallbackQueryContext;
import com.annimon.tgbotsmodule.commands.context.RegexMessageContext;
import com.annimon.tgbotsmodule.services.CommonAbsSender;
import org.telegram.telegrambots.meta.api.objects.message.Message;

sealed interface YtDlpSessionContext permits CallbackYtDlpSessionContext, RegexYtDlpSessionContext {
    Message message();

    CommonAbsSender sender();

    void alert(String error);
}

record CallbackYtDlpSessionContext(CallbackQueryContext ctx) implements YtDlpSessionContext {
    @Override
    public Message message() {
        return ctx.message();
    }

    @Override
    public CommonAbsSender sender() {
        return ctx.sender;
    }

    @Override
    public void alert(String error) {
        ctx.answerAsAlert(error).callAsync(ctx.sender);
    }
}

record RegexYtDlpSessionContext(RegexMessageContext ctx) implements YtDlpSessionContext {
    @Override
    public Message message() {
        return ctx.message();
    }

    @Override
    public CommonAbsSender sender() {
        return ctx.sender;
    }

    @Override
    public void alert(String error) {
        ctx.replyToMessage(error).callAsync(ctx.sender);
    }
}