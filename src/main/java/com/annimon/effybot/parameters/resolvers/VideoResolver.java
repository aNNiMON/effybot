package com.annimon.effybot.parameters.resolvers;

import com.annimon.effybot.parameters.*;
import com.annimon.effybot.session.FileInfo;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Set;

public class VideoResolver implements ParametersResolver {

    @Override
    public void resolve(@NotNull Parameters parameters, @NotNull FileInfo fileInfo) {
        final boolean hasVideo = switch (fileInfo.fileType()) {
            case ANIMATION, VIDEO, VIDEO_NOTE -> true;
            default -> false;
        };

        if (hasVideo) {
            parameters.addAll(List.of(
                    new Contrast(),
                    new Gamma(),
                    new Saturation(),
                    new VideoBitrate(),
                    new VideoScale(),
                    new VideoFrameRate()
            ));
        }
    }

    @Override
    public void refine(@NotNull Parameters parameters) {
        parameters.findById(OutputFormat.ID, OutputFormat.class)
                .map(Parameter::getValue)
                .ifPresent(format -> {
                    final Set<String> parameterIds = Set.of(
                            Contrast.ID,
                            Gamma.ID,
                            Saturation.ID,
                            VideoBitrate.ID,
                            VideoScale.ID,
                            VideoFrameRate.ID
                    );
                    Set<String> audioOnly = Set.of(OutputFormat.AUDIO, OutputFormat.AUDIO_SPECTRUM);
                    if (audioOnly.contains(format)) {
                        parameters.disableAll(parameterIds);
                    } else {
                        parameters.enableAll(parameterIds);
                    }
                });
    }
}
