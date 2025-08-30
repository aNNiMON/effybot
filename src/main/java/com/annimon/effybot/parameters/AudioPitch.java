package com.annimon.effybot.parameters;

import com.annimon.effybot.commands.ffmpeg.Visitor;

import java.util.List;

public class AudioPitch extends StringParameter {
    public static final String ID = "apitch";
    private static final List<String> VALUES = List.of(
            "0.6", "0.8", "0.9",
            "1",
            "1.15", "1.25", "1.5", "2"
    );

    public AudioPitch() {
        super(ID, "\uD83D\uDD08 Pitch", VALUES, "1");
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
