package com.annimon.effybot.parameters;

import com.annimon.effybot.commands.ffmpeg.Visitor;

import java.util.List;

public class VideoScale extends StringParameter {
    public static final String ID = "scale";
    private static final List<String> VALUES = List.of(
            "144", "240", "360", "", "480", "720", "1080"
    );

    public VideoScale() {
        super(ID, "Scale", VALUES, "");
    }

    @Override
    public String describeValue(String value) {
        if (value.isEmpty()) {
            return "ORIGINAL";
        } else {
            return value + "p";
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
