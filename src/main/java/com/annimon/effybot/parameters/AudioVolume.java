package com.annimon.effybot.parameters;

import com.annimon.effybot.commands.ffmpeg.Visitor;

import java.util.List;

public class AudioVolume extends StringParameter {
    public static final String ID = "volume";
    private static final List<String> VALUES = List.of(
            "-15dB", "-10dB", "-5dB", "-2dB",
            "", "2dB", "5dB", "10dB",
            "15dB", "30dB", "50dB"
    );

    public AudioVolume() {
        super(ID, "\uD83D\uDD08 Volume", VALUES, "");
    }

    @Override
    public String describeValue(String value) {
        if (value.isEmpty()) {
            return "ORIGINAL";
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
