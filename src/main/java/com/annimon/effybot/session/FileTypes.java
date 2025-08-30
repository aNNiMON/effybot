package com.annimon.effybot.session;

public class FileTypes {
    public static boolean canContainAudio(FileType type) {
        return switch (type) {
            case AUDIO, VIDEO, VIDEO_NOTE, VOICE -> true;
            default -> false;
        };
    }

    public static boolean canContainVideo(FileType type) {
        return switch (type) {
            case ANIMATION, VIDEO, VIDEO_NOTE -> true;
            default -> false;
        };
    }
}
