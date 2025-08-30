package com.annimon.effybot.commands;

import com.annimon.effybot.Permissions;
import com.annimon.tgbotsmodule.commands.TextCommand;
import com.annimon.tgbotsmodule.commands.authority.For;
import com.annimon.tgbotsmodule.commands.context.MessageContext;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Set;

public class HelpCommand implements TextCommand {
    private final Permissions permissions;

    public HelpCommand(Permissions permissions) {
        this.permissions = permissions;
    }

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public Set<String> aliases() {
        return Set.of("/start");
    }

    @SuppressWarnings("unchecked")
    @Override
    public EnumSet<For> authority() {
        return For.all();
    }

    @Override
    public void accept(@NotNull MessageContext ctx) {
        boolean hasRights = permissions.hasRights(ctx.sender, ctx.update(), ctx.user(), EnumSet.of(For.USER));
        String rightsText = "";
        if (!hasRights) {
            rightsText = """
                    <i>Note: You are not in the list of allowed users.
                    This bot is for personal use only, consider setting up your own instance:</i>
                        https://github.com/aNNiMON/effybot
                    """.stripIndent() + "\n\n";
        }
        ctx.replyToMessage("""
                %s<b>Media processing</b>
                Send any media to start processing.
                
                <b>Input parameters</b> (in reply to media processing message)
                /ss — set media start position
                /to — set media end position
                /t — set media duration
                
                <b>yt-dlp</b>
                /dl link [format] — download a media using yt-dlp
                  <code>link</code> — a link to download (it must be supported by yt-dlp)
                  <code>format</code> — (optional) a download format. Can be: best, audio, 240, 360, 480, 720 or 1080. Default: 1080
                """.stripIndent().formatted(rightsText)).enableHtml().disableWebPagePreview().callAsync(ctx.sender);
    }
}
