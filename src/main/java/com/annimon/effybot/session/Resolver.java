package com.annimon.effybot.session;

import com.annimon.tgbotsmodule.api.methods.Methods;
import com.annimon.tgbotsmodule.api.methods.interfaces.MediaMessageMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class Resolver {
    public static @Nullable FileInfo resolveFileInfo(@NotNull Message message) {
        if (message.hasAnimation()) {
            final var att = message.getAnimation();
            return new FileInfo(FileType.ANIMATION, att.getFileId(), att.getFileName(),
                    att.getFileSize(), att.getDuration(), att.getWidth(), att.getHeight());
        } else if (message.hasAudio()) {
            final var att = message.getAudio();
            return new FileInfo(FileType.AUDIO, att.getFileId(), att.getFileName(), att.getFileSize(), att.getDuration());
        } else if (message.hasVideo()) {
            final var att = message.getVideo();
            return new FileInfo(FileType.VIDEO, att.getFileId(), att.getFileName(),
                    att.getFileSize(), att.getDuration(), att.getWidth(), att.getHeight());
        } else if (message.hasVideoNote()) {
            final var att = message.getVideoNote();
            final Long fileSize = att.getFileSize() != null ? (Long.valueOf(att.getFileSize())) : null;
            return new FileInfo(FileType.VIDEO_NOTE, att.getFileId(), null,
                    fileSize, att.getDuration(), att.getLength(), att.getLength());
        } else if (message.hasVoice()) {
            final var att = message.getVoice();
            return new FileInfo(FileType.VOICE, att.getFileId(), null, att.getFileSize(), att.getDuration());
        } else if (message.hasDocument()) {
            final var att = message.getDocument();
            final var mimeType = att.getMimeType();
            if (mimeType == null || att.getFileSize() == null || att.getFileSize() == 0) {
                return null;
            } else if (mimeType.startsWith("video/")) {
                return new FileInfo(FileType.VIDEO, att.getFileId(), att.getFileName(),
                        att.getFileSize(), null);
            } else if (mimeType.startsWith("audio/")) {
                return new FileInfo(FileType.AUDIO, att.getFileId(), att.getFileName(),
                        att.getFileSize(), null);
            }
            return null;
        } else if (message.hasSticker()) {
            final var att = message.getSticker();
            if (att.getIsVideo()) {
                long fileSize = att.getFileSize() != null ? att.getFileSize().longValue() : 0L;
                return new FileInfo(FileType.ANIMATION, att.getFileId(), "sticker.webm", fileSize, null);
            }
            return null;
        } else {
            return null;
        }
    }

    public static MediaMessageMethod<? extends MediaMessageMethod<?, ?>, ?> resolveMethod(@NotNull FileType fileType) {
        return switch (fileType) {
            case ANIMATION -> Methods.sendAnimation();
            case AUDIO -> Methods.sendAudio();
            case PHOTO -> Methods.sendPhoto();
            case VIDEO -> Methods.sendVideo();
            case VIDEO_NOTE -> Methods.sendVideoNote();
            case VOICE -> Methods.sendVoice();
        };
    }

    public static ActionType resolveAction(@NotNull FileType fileType) {
        return switch (fileType) {
            case PHOTO -> ActionType.UPLOAD_PHOTO;
            case VIDEO -> ActionType.UPLOAD_VIDEO;
            case VIDEO_NOTE -> ActionType.UPLOAD_VIDEO_NOTE;
            case VOICE -> ActionType.UPLOAD_VOICE;
            default -> ActionType.TYPING;
        };
    }
}
