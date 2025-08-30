package com.annimon.effybot.parameters;

import com.annimon.effybot.commands.ffmpeg.Visitor;
import java.util.List;

public class AudioCrystalizer extends StringParameter {
    public static final String ID = "crystalizer";
    private static final List<String> VALUES = List.of(
            "-8", "-4", "-2", "0", "2", "4", "8"
    );

    public AudioCrystalizer() {
        super(ID, "\uD83D\uDD08 Crystalizer", VALUES, "0");
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
