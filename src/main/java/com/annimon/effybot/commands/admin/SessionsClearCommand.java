package com.annimon.effybot.commands.admin;

import com.annimon.effybot.Permissions;
import com.annimon.effybot.session.Sessions;
import com.annimon.tgbotsmodule.commands.CallbackQueryCommand;
import com.annimon.tgbotsmodule.commands.authority.For;
import com.annimon.tgbotsmodule.commands.context.CallbackQueryContext;
import org.jetbrains.annotations.NotNull;
import java.util.EnumSet;

public class SessionsClearCommand implements CallbackQueryCommand {
    static final String CALLBACK_NAME = "sessions_clear";
    private final Sessions sessions;

    public SessionsClearCommand(Sessions sessions) {
        this.sessions = sessions;
    }

    @Override
    public String command() {
        return CALLBACK_NAME;
    }

    @SuppressWarnings("unchecked")
    @Override
    public EnumSet<For> authority() {
        return Permissions.SUPERUSERS;
    }

    @Override
    public void accept(@NotNull CallbackQueryContext ctx) {
        final int size = sessions.getSize();
        sessions.clear();
        ctx.editMessage("%d sessions cleared".formatted(size))
                .setReplyMarkup(null)
                .callAsync(ctx.sender);
    }
}
