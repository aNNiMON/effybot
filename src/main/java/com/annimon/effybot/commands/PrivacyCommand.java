package com.annimon.effybot.commands;

import com.annimon.tgbotsmodule.commands.TextCommand;
import com.annimon.tgbotsmodule.commands.authority.For;
import com.annimon.tgbotsmodule.commands.context.MessageContext;
import org.jetbrains.annotations.NotNull;
import java.util.EnumSet;

public class PrivacyCommand implements TextCommand {

    @Override
    public String command() {
        return "/privacy";
    }

    @SuppressWarnings("unchecked")
    @Override
    public EnumSet<For> authority() {
        return For.all();
    }

    @Override
    public void accept(@NotNull MessageContext ctx) {
        ctx.replyToMessage("""
                <b>Privacy policy</b>
                
                This bot intended for personal use only.
                No data is stored if you're not allowed to use this bot.
                
                <b>Source code</b>: https://github.com/aNNiMON/effybot
                <b>Contact</b>: @annimon119
                """.stripIndent()).enableHtml().disableWebPagePreview().callAsync(ctx.sender);
    }
}
