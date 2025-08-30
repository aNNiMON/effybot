package com.annimon.effybot.file;

import com.annimon.effybot.TelegramRuntimeException;
import com.annimon.tgbotsmodule.api.methods.Methods;
import com.annimon.tgbotsmodule.services.CommonAbsSender;
import org.apache.commons.io.FilenameUtils;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;

public class TelegramFileDownloader implements FileDownloader {

    @Override
    public File downloadFile(CommonAbsSender sender, String fileId, String defaultFilename) {
        try {
            final var tgFile = Methods.getFile(fileId).call(sender);
            final var extension = FilenameUtils.getExtension(tgFile.getFilePath());
            File defaultFile = sender.downloadFile(tgFile);
            File dest = File.createTempFile("tmp", "file." + extension);
            return defaultFile.renameTo(dest) ? dest : defaultFile;
        } catch (IOException | TelegramApiException | TelegramRuntimeException e) {
            throw new FileDownloadException(e.getMessage(), e);
        }
    }
}
