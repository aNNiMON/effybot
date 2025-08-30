package com.annimon.effybot.file;

import com.annimon.tgbotsmodule.services.CommonAbsSender;

import java.io.File;

public class FallbackFileDownloader implements FileDownloader {
    private final FileDownloader defaultDownloader;
    private final FileDownloader fallbackDownloader;

    public FallbackFileDownloader(FileDownloader defaultDownloader, FileDownloader fallbackDownloader) {
        this.defaultDownloader = defaultDownloader;
        this.fallbackDownloader = fallbackDownloader;
    }

    @Override
    public File downloadFile(CommonAbsSender sender, String fileId, String defaultFilename) {
        try {
            return defaultDownloader.downloadFile(sender, fileId, defaultFilename);
        } catch (FileDownloadException e) {
            return fallbackDownloader.downloadFile(sender, fileId, defaultFilename);
        }
    }
}
