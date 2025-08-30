package com.annimon.effybot.parameters;

import com.annimon.effybot.commands.ffmpeg.Visitor;
import java.util.List;

public class Saturation extends StringParameter {
    public static final String ID = "saturation";
    private static final List<String> VALUES = List.of(
            "0", "0.25", "0.5", "0.75",
            "1", "1.25", "1.5", "1.75",
            "2", "2.25", "2.5", "3"
    );

    public Saturation() {
        super(ID, "Saturation", VALUES, "1");
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
