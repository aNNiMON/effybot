package com.annimon.effybot.commands.admin;

import com.annimon.effybot.session.Sessions;
import com.annimon.tgbotsmodule.commands.CommandBundle;
import com.annimon.tgbotsmodule.commands.CommandRegistry;
import com.annimon.tgbotsmodule.commands.authority.For;
import org.jetbrains.annotations.NotNull;

public class AdminCommandBundle implements CommandBundle<For> {

    private final Sessions sessions;

    public AdminCommandBundle(Sessions sessions) {
        this.sessions = sessions;
    }

    @Override
    public void register(@NotNull CommandRegistry commands) {
        commands.register(new RunCommand());
        commands.register(new ClearCommand());
        commands.register(new SessionsCommand(sessions));
        commands.register(new SessionsClearCommand(sessions));
    }
}
