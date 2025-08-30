package com.annimon.effybot.parameters;

import com.annimon.effybot.commands.ffmpeg.Visitor;

import java.util.List;

public class AudioStreamByLanguage extends StringParameter {
    public static final String ID = "asellang";
    private static final List<String> VALUES = List.of(
            "", "eng", "fra", "ita", "ukr", "spa"
    );

    public AudioStreamByLanguage() {
        super(ID, "\uD83D\uDD08 Track", VALUES, "");
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
        return 3;
    }

    @Override
    public <I> void accept(Visitor<I> visitor, I input) {
        visitor.visit(this, input);
    }
}