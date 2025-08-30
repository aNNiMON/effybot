package com.annimon.effybot.parameters.resolvers;

import com.annimon.effybot.parameters.OutputFormat;
import com.annimon.effybot.parameters.Parameters;
import com.annimon.effybot.session.FileInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import static com.annimon.effybot.parameters.OutputFormat.*;

public class OutputFormatResolver implements ParametersResolver {

    @Override
    public void resolve(@NotNull Parameters parameters, @NotNull FileInfo fileInfo) {
        final var outputFormat = switch (fileInfo.fileType()) {
            case AUDIO, VOICE -> new OutputFormat(List.of(AUDIO, AUDIO_SPECTRUM), AUDIO);
            case VIDEO -> forVideo(fileInfo);
            case VIDEO_NOTE -> new OutputFormat(List.of(VIDEO_NOTE, VIDEO, AUDIO, AUDIO_SPECTRUM), VIDEO_NOTE);
            case ANIMATION -> forAnimation(fileInfo);
            default -> null;
        };
        if (outputFormat != null) {
            parameters.add(outputFormat);
        }
    }

    @NotNull
    private static OutputFormat forVideo(@NotNull FileInfo fileInfo) {
        final var types = new ArrayList<String>(3);
        types.add(VIDEO);
        if (fileInfo.getDuration() < 60 && fileInfo.getExtension().equals("mp4")) {
            types.add(VIDEO_NOTE);
        }
        types.add(AUDIO);
        types.add(AUDIO_SPECTRUM);
        return new OutputFormat(types, VIDEO);
    }

    @Nullable
    private static OutputFormat forAnimation(@NotNull FileInfo fileInfo) {
        if (fileInfo.getExtension().equals("mp4")) return null;
        // webm sticker:
        return new OutputFormat(List.of(VIDEO, VIDEO_NOTE), VIDEO);
    }
}
