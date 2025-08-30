package com.annimon.effybot.parameters.resolvers;

import com.annimon.effybot.parameters.*;
import com.annimon.effybot.session.FileInfo;
import com.annimon.effybot.session.FileType;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class AudioResolver implements ParametersResolver {

    @Override
    public void resolve(@NotNull Parameters parameters, @NotNull FileInfo fileInfo) {
        final boolean hasAudio = switch (fileInfo.fileType()) {
            case PHOTO, ANIMATION -> false;
            case AUDIO, VOICE -> true;
            case VIDEO, VIDEO_NOTE -> true; // TODO: add actual ffprobe check for audio
        };

        if (hasAudio) {
            disableAudioParam(parameters, fileInfo.fileType());
            parameters.addAll(List.of(
                    new AudioBitrate(),
                    new AudioCompressor(),
                    new AudioCrystalizer(),
                    new AudioEffect(),
                    new AudioPitch(),
                    new AudioVolume()
            ));
        }
    }

    @Override
    public void refine(@NotNull Parameters parameters) {
        parameters.findById(DisableAudio.ID, DisableAudio.class)
                .ifPresent(p -> {
                    final Set<String> parameterIds = Set.of(
                            AudioBitrate.ID,
                            AudioCompressor.ID,
                            AudioCrystalizer.ID,
                            AudioEffect.ID,
                            AudioPitch.ID,
                            AudioVolume.ID
                    );
                    Optional<OutputFormat> outputFormat = parameters.findById(OutputFormat.ID, OutputFormat.class);
                    if (p.getValueAsPrimitive()) {
                        parameters.disableAll(parameterIds);
                        outputFormat.ifPresent(par -> parameters.add(par.disableFormat(OutputFormat.AUDIO, OutputFormat.AUDIO_SPECTRUM)));
                    } else {
                        parameters.enableAll(parameterIds);
                        outputFormat.ifPresent(par -> parameters.add(par.enableFormat(OutputFormat.AUDIO, OutputFormat.AUDIO_SPECTRUM)));
                    }
                });
        parameters.findById(OutputFormat.ID, OutputFormat.class)
                .map(Parameter::getValue)
                .ifPresent(format -> {
                    // Audio spectrum ignores audio commands (bitrate)
                    if (format.equals(OutputFormat.AUDIO_SPECTRUM)) {
                        parameters.disable(AudioBitrate.ID);
                    } else {
                        parameters.enable(AudioBitrate.ID);
                    }
                });
    }

    private void disableAudioParam(@NotNull Parameters parameters, @NotNull FileType fileType) {
        final boolean canAudioBeDisabled = switch (fileType) {
            case ANIMATION, AUDIO, VOICE -> false;
            case PHOTO, VIDEO, VIDEO_NOTE -> true;
        };
        if (canAudioBeDisabled) {
            parameters.add(new DisableAudio());
        }
    }
}
