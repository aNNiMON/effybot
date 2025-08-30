package com.annimon.effybot.parameters;

import com.annimon.effybot.commands.ffmpeg.Visitor;

import java.util.List;

public class AudioBitrate extends StringParameter {
    public static final String ID = "abitrate";
    private static final List<String> VALUES = List.of(
            "4k", "16k", "32k", "", "64k", "128k",
            "256k", "320k", "512k"
    );

    public AudioBitrate() {
        super(ID, "\uD83D\uDD08 Bitrate", VALUES, "");
    }

    @Override
    public String describeValue(String value) {
        if (value.isEmpty()) {
            return "AUTO";
        } else {
            return super.describeValue(value);
        }
    }

    @Override
    public int defaultColumnsCount() {
        return 3;
    }

    @Override
    public <I> void accept(Visitor<I> visitor, I input) {
        visitor.visit(this, input);
    }
}
