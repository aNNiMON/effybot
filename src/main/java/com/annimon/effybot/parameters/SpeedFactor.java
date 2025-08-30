package com.annimon.effybot.parameters;

import com.annimon.effybot.commands.ffmpeg.Visitor;

import java.util.List;

public class SpeedFactor extends StringParameter {
    public static final String ID = "speed";
    private static final List<String> VALUES = List.of(
            "0.5", "0.75", "0.8", "0.9",
            "1", "1.25", "1.4", "1.5",
            "1.6", "1.8", "2", "2.5", "3"
    );

    public SpeedFactor() {
        super(ID, "Speed", VALUES, "1");
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
