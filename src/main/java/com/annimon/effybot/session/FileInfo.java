package com.annimon.effybot.session;

import org.apache.commons.io.FilenameUtils;
import java.util.Locale;

public record FileInfo(FileType fileType, String fileId, String filename,
                       Long fileSize, Integer duration, Integer width, Integer height) {
    public FileInfo(FileType fileType, String fileId, String filename, Long fileSize, Integer duration) {
        this(fileType, fileId, filename, fileSize, duration, null, null);
    }

    public int getDuration() {
        return duration != null ? duration : 0;
    }

    public String getExtension() {
        if (filename == null) return "";
        return FilenameUtils.getExtension(filename).toLowerCase(Locale.ROOT);
    }
}
