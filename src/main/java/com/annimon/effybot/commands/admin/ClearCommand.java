package com.annimon.effybot.commands.admin;

import com.annimon.effybot.Permissions;
import com.annimon.effybot.TextUtils;
import com.annimon.effybot.file.FilePath;
import com.annimon.tgbotsmodule.commands.TextCommand;
import com.annimon.tgbotsmodule.commands.authority.For;
import com.annimon.tgbotsmodule.commands.context.MessageContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.EnumSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.annimon.effybot.commands.admin.ThrowableFunction.safeFunction;
import static com.annimon.effybot.commands.admin.ThrowablePredicate.safePredicate;

public class ClearCommand implements TextCommand {

    @Override
    public String command() {
        return "/clear";
    }

    @SuppressWarnings("unchecked")
    @Override
    public EnumSet<For> authority() {
        return Permissions.SUPERUSERS;
    }

    @Override
    public void accept(@NotNull MessageContext ctx) {
        try {
            final Path inputPath = Paths.get(FilePath.inputDir());
            final Path outputPath = Paths.get(FilePath.outputDir());
            final var oneDayAgo = Instant.now().minus(1, ChronoUnit.DAYS);
            final var oldFiles = Stream.of(inputPath, outputPath)
                    .flatMap(safeFunction(Files::list))
                    .filter(safePredicate(p -> Files.getLastModifiedTime(p).toInstant().isBefore(oneDayAgo)))
                    .collect(Collectors.toSet());
            if (oldFiles.isEmpty()) {
                ctx.replyToMessage("No files to remove").callAsync(ctx.sender);
            } else {
                final int count = oldFiles.size();
                long size = 0;
                for (Path p : oldFiles) {
                    size += Files.size(p);
                    Files.delete(p);
                }
                final String totalSize = TextUtils.readableFileSize(size);
                ctx.replyToMessage("Removed %d old files of total size %s".formatted(count, totalSize))
                        .callAsync(ctx.sender);
            }
        } catch (IOException e) {
            ctx.replyToMessage("Unable to clear directories due to " + e.getMessage()).callAsync(ctx.sender);
        }
    }
}
