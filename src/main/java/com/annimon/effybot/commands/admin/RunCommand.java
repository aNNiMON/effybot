package com.annimon.effybot.commands.admin;

import com.annimon.effybot.Permissions;
import com.annimon.tgbotsmodule.commands.TextCommand;
import com.annimon.tgbotsmodule.commands.authority.For;
import com.annimon.tgbotsmodule.commands.context.MessageContext;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

public class RunCommand implements TextCommand {

    @Override
    public String command() {
        return "/run";
    }

    @SuppressWarnings("unchecked")
    @Override
    public EnumSet<For> authority() {
        return Permissions.SUPERUSERS;
    }

    @Override
    public void accept(@NotNull MessageContext ctx) {
        final String command = ctx.argumentsAsString();
        runCommand(ctx, command);
    }

    private void runCommand(@NotNull final MessageContext ctx, @NotNull final String command) {
        if (command.isBlank()) return;
        CompletableFuture.completedFuture(command)
                .thenApplyAsync(this::run)
                .handleAsync((output, throwable) -> {
                    final String text;
                    if (throwable != null) {
                        text = "Error " + throwable.getMessage();
                    } else if (output.isBlank()) {
                        text = "Empty output";
                    } else {
                        text = output;
                    }
                    return ctx.replyToMessage(text).call(ctx.sender);
                });
    }

    private String run(String command) {
        try {
            final ProcessBuilder pb = new ProcessBuilder(toCommands(command));
            pb.redirectErrorStream(true);
            final Process process = pb.start();
            final String output;
            try (final var reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                output = IOUtils.toString(reader);
            }
            process.waitFor();
            return output;
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> toCommands(String command) {
        final var commands = new ArrayList<String>();
        final var m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(command);
        while (m.find()) {
            commands.add(m.group(1));
        }
        return commands;
    }
}
