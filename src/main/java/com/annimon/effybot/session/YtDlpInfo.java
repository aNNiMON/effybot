package com.annimon.effybot.session;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.StringJoiner;

import static com.annimon.effybot.TextUtils.isNotEmpty;
import static com.annimon.effybot.TextUtils.readableDuration;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record YtDlpInfo(
        @JsonProperty("duration_string")
        String durationString,
        Integer duration,

        String resolution,
        String fps,

        @JsonProperty("vbr")
        Double videoBitrate,
        @JsonProperty("abr")
        Double audioBitrate,

        String uploader
) {

    public StringJoiner describe() {
        final var joiner = new StringJoiner("\n");
        if (hasDuration()) {
            joiner.add("Duration: <code>%s</code>".formatted(getFormattedDuration()));
        }
        if (isNotEmpty(resolution)) {
            joiner.add("Resolution: <code>%s</code>".formatted(resolution));
        }
        if (isNotEmpty(fps)) {
            joiner.add("Frame rate: <code>%s</code>".formatted(fps));
        }
        if (hasVideoBitrate() || hasAudioBitrate()) {
            joiner.add("Bitrate: <code>%s</code>".formatted(getFormattedBitrate()));
        }
        if (isNotEmpty(uploader)) {
            joiner.add("Uploader: <code>%s</code>".formatted(uploader));
        }
        return joiner;
    }

    private boolean hasDuration() {
        return (duration != null && duration > 0) || isNotEmpty(durationString);
    }

    private String getFormattedDuration() {
        if (duration != null) return readableDuration(duration);
        return durationString;
    }

    private boolean hasVideoBitrate() {
        return (videoBitrate != null && videoBitrate > 0);
    }

    private boolean hasAudioBitrate() {
        return (audioBitrate != null && audioBitrate > 0);
    }

    private String getFormattedBitrate() {
        final var joiner = new StringJoiner(" / ");
        if (hasVideoBitrate()) {
            joiner.add("%.0f kbps video".formatted(videoBitrate));
        }
        if (hasAudioBitrate()) {
            joiner.add("%.0f kbps audio".formatted(audioBitrate));
        }
        return joiner.toString();
    }
}
