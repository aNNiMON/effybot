package com.annimon.effybot.parameters;

import com.annimon.effybot.commands.ffmpeg.Visitor;
import java.util.List;

public class Gamma extends StringParameter {
    public static final String ID = "gamma";
    private static final List<String> VALUES = List.of(
            "0.2", "0.4", "0.6", "0.8",
            "1", "1.2", "1.4", "1.6",
            "1.8", "2", "2.2", "2.4"
    );

    public Gamma() {
        super(ID, "Gamma", VALUES, "1");
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
