package com.annimon.effybot.parameters.resolvers;

import com.annimon.effybot.parameters.*;
import com.annimon.effybot.session.FileInfo;
import com.annimon.effybot.session.FileType;
import org.jetbrains.annotations.NotNull;

public class MultiAudioStreamsResolver implements ParametersResolver {

    @Override
    public void resolve(@NotNull Parameters parameters, @NotNull FileInfo fileInfo) {
        // TODO: ffprobe check for actual codec
        if (!fileInfo.fileType().equals(FileType.VIDEO)) {
            return;
        }

        if (fileInfo.getExtension().equals("mkv")) {
            parameters.add(new AudioStreamByLanguage());
        }
    }

    @Override
    public void refine(@NotNull Parameters parameters) {
        parameters.findById(DisableAudio.ID, DisableAudio.class)
                .ifPresent(p -> {
                    if (p.getValueAsPrimitive()) {
                        parameters.disable(AudioStreamByLanguage.ID);
                    } else {
                        parameters.enable(AudioStreamByLanguage.ID);
                    }
                });
    }
}
