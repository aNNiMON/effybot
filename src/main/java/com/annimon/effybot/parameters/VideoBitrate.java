package com.annimon.effybot.parameters;

import com.annimon.effybot.commands.ffmpeg.Visitor;

import java.util.List;

public class VideoBitrate extends StringParameter {
    public static final String ID = "vbitrate";
    private static final List<String> VALUES = List.of(
            "16k", "32k", "", "64k", "128k", "256k",
            "512k", "1M", "2M", "4M", "8M", "16M"
    );

    public VideoBitrate() {
        super(ID, "Video bitrate", VALUES, "");
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
        return 4;
    }

    @Override
    public <I> void accept(Visitor<I> visitor, I input) {
        visitor.visit(this, input);
    }
}
