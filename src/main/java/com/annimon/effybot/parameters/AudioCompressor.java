package com.annimon.effybot.parameters;

import com.annimon.effybot.commands.ffmpeg.Visitor;
import java.util.List;

public class AudioCompressor extends StringParameter {
    public static final String ID = "acompand";

    public static final String COMP_DEFAULT = "Default";
    public static final String COMP_2_6DB = "2:1 at -6dB";
    public static final String COMP_2_12DB = "2:1 at -12dB";
    public static final String COMP_2_18DB = "2:1 at -18dB";
    public static final String COMP_3_9DB = "3:1 at -9dB";
    public static final String COMP_3_18DB = "3:1 at -18dB";
    public static final String COMP_NORMALIZE_1 = "Normalize 1";
    public static final String COMP_NORMALIZE_2 = "Normalize 2";
    public static final String COMP_NOISE_GATE_1 = "Noise Gate 1";
    public static final String COMP_NOISE_GATE_2 = "Noise Gate 2";
    public static final String COMP_HARD_NOISE_GATE_35DB = "Noise Gate 35dB";
    public static final String COMP_HARD_LIMITER_6DB = "Hard Limiter -6dB";
    public static final String COMP_HARD_LIMITER_12DB = "Hard Limiter -12dB";
    public static final String COMP_SOFT_LIMITER = "Soft Limiter";
    public static final String COMP_EXPANDER = "Expander";
    public static final String COMP_GATE = "Gate";

    private static final List<String> VALUES = List.of(
            "", COMP_DEFAULT,
            COMP_2_6DB, COMP_2_12DB, COMP_2_18DB,
            COMP_3_9DB, COMP_3_18DB,
            COMP_NORMALIZE_1, COMP_NORMALIZE_2,
            COMP_NOISE_GATE_1, COMP_NOISE_GATE_2,
            COMP_HARD_NOISE_GATE_35DB,
            COMP_HARD_LIMITER_6DB, COMP_HARD_LIMITER_12DB,
            COMP_SOFT_LIMITER,
            COMP_EXPANDER, COMP_GATE
    );

    public AudioCompressor() {
        super(ID, "\uD83D\uDD08 Compressor", VALUES, "");
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
