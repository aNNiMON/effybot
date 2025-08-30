package com.annimon.effybot.file;

import com.annimon.tgbotsmodule.services.CommonAbsSender;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TelegramClientFileDownloader implements FileDownloader {
    private final String scriptPath;
    private final String appId;
    private final String appHash;
    private final String botToken;
    private final String botUsername;

    public TelegramClientFileDownloader(String scriptPath, String appId, String appHash, String botToken, String botUsername) {
        this.scriptPath = scriptPath;
        this.appId = appId;
        this.appHash = appHash;
        this.botToken = botToken;
        this.botUsername = botUsername;
    }

    @Override
    public File downloadFile(CommonAbsSender sender, String fileId, String defaultFilename) {
        try {
            final var tempFile = File.createTempFile("tmp", defaultFilename);
            final ProcessBuilder pb = new ProcessBuilder(buildCommand(fileId, tempFile));
            final Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new FileDownloadException("Downloader process finished with the exit code " + exitCode);
            }
            return tempFile;
        } catch (InterruptedException | IOException e) {
            throw new FileDownloadException("Downloader process failed", e);
        }
    }

    private List<String> buildCommand(String fileId, File destFile) {
        final var commands = new ArrayList<String>();
        commands.addAll(List.of("python3", scriptPath));
        commands.addAll(List.of("--api_id", appId));
        commands.addAll(List.of("--api_hash", appHash));
        commands.addAll(List.of("--bot_token", botToken));
        commands.addAll(List.of("--bot_username", botUsername));
        commands.add("get");
        commands.addAll(List.of("--file_id", fileId));
        commands.addAll(List.of("-o", destFile.getPath()));
        return commands;
    }
}
