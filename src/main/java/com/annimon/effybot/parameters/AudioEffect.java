package com.annimon.effybot.parameters;

import com.annimon.effybot.commands.ffmpeg.Visitor;

import java.util.List;

public class AudioEffect extends StringParameter {
    public static final String ID = "aeffect";

    public static final String ROBOT = "Robot";
    public static final String ECHO = "Echo";
    public static final String ECHO_2 = "Echo 2";
    public static final String NOISE_REDUCTION_5 = "Noise reduction 5dB";
    public static final String NOISE_REDUCTION_12 = "Noise reduction 12dB";
    public static final String PULSATOR = "Pulsator";
    public static final String VIBRATO = "Vibrato";

    private static final List<String> VALUES = List.of(
            "", ROBOT, ECHO, ECHO_2, NOISE_REDUCTION_5, NOISE_REDUCTION_12, PULSATOR, VIBRATO
    );

    public AudioEffect() {
        super(ID, "\uD83D\uDD08 Effect", VALUES, "");
    }

    @Override
    public String describeValue(String value) {
        if (value.isEmpty()) {
            return "NONE";
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
