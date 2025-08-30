package com.annimon.effybot.file;

import com.annimon.effybot.session.FileType;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class FilePath {

    public static String inputDir() {
        return "input";
    }

    public static String outputDir() {
        return "output";
    }

    public static File inputFile(String filename) {
        return new File(inputDir(), filename);
    }

    public static File outputFile(String filename) {
        return new File(outputDir(), filename);
    }

    public static String generateFilename(String fileId, String filename) {
        final var ext = FilenameUtils.getExtension(filename);
        return "%d_%d.%s".formatted(System.currentTimeMillis(), Math.abs(fileId.hashCode()), ext);
    }

    public static String defaultFilename(@NotNull FileType fileType) {
        return "file." + switch (fileType) {
            case ANIMATION, VIDEO, VIDEO_NOTE -> "mp4";
            case AUDIO -> "mp3";
            case VOICE -> "ogg";
            case PHOTO -> "jpg";
        };
    }
}
