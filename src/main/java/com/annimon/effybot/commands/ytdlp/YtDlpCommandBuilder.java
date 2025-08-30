package com.annimon.effybot.commands.ytdlp;

import com.annimon.effybot.file.FilePath;
import com.annimon.effybot.session.YtDlpSession;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class YtDlpCommandBuilder {

    public String[] buildCommand(final @NotNull YtDlpSession session) {
        final var commands = new ArrayList<String>();
        commands.add("yt-dlp");
        commands.addAll(buildFormat(session));
        // Trim
        if (!session.getInputParams().isEmpty()) {
            commands.addAll(List.of("--external-downloader", "ffmpeg"));
            final String ffmpegArgs = String.join(" ", session.getInputParams().asFFmpegCommands());
            commands.addAll(List.of("--external-downloader-args", "ffmpeg:" + ffmpegArgs));
        }
        commands.add(session.getUrl());
        commands.addAll(buildOutput(session));
        return commands.toArray(String[]::new);
    }

    public String[] buildInfoCommand(final @NotNull YtDlpSession session) {
        final var commands = new ArrayList<String>();
        commands.add("yt-dlp");
        commands.add("--write-info-json");
        commands.add("--no-download");
        // --dump-json
        commands.addAll(buildFormat(session));
        commands.add(session.getUrl());
        commands.addAll(buildOutput(session));
        return commands.toArray(String[]::new);
    }

    private static List<String> buildFormat(@NotNull YtDlpSession session) {
        final var commands = new ArrayList<String>();
        commands.add("-f");
        String downloadOption = session.getDownloadOption();
        if (downloadOption.equals("audio")) {
            commands.add("bestaudio[ext=m4a]/bestaudio");
        } else if (downloadOption.equals("best")) {
            commands.add("best");
        } else {
            final var mp4 = "bestvideo[ext=mp4][height<=%s]+bestaudio[ext=m4a]".formatted(downloadOption);
            final var any = "bestvideo[height<=%s]+bestaudio".formatted(downloadOption);
            final var other = "best";
            commands.add(String.join("/", List.of(mp4, any, other)));
        }
        return commands;
    }

    private static List<String> buildOutput(@NotNull YtDlpSession session) {
        final var targetFilename = FilePath.outputDir() + "/" + session.getOutputFilename();
        return List.of("--force-overwrites", "-o", targetFilename);
    }
}
