package com.annimon.effybot.parameters;

import com.annimon.effybot.commands.ffmpeg.Visitor;

import java.util.*;
import java.util.function.Predicate;

public class OutputFormat extends StringParameter {
    public static final String ID = "output";

    public static final String VIDEO = "VIDEO";
    public static final String AUDIO = "AUDIO";
    public static final String VIDEO_NOTE = "VIDEO NOTE";
    public static final String AUDIO_SPECTRUM = "AUDIO SPECTRUM";

    public OutputFormat(List<String> values, String initialValue) {
        super(ID, "➡️ Output", values, initialValue);
    }

    public OutputFormat disableFormat(String... formats) {
        if (possibleValues.size() <= 1) return this;
        final Set<String> set = Set.of(formats);
        final var values = possibleValues.stream()
                .filter(Predicate.not(set::contains))
                .map(Objects::toString)
                .toList();
        if (possibleValues.size() == values.size()) {
            return this;
        }
        return new OutputFormat(values, values.get(0));
    }

    public OutputFormat enableFormat(String... formats) {
        final Set<String> newset = new LinkedHashSet<>(possibleValues);
        if (newset.addAll(Set.of(formats))) {
            final var values = new ArrayList<>(newset);
            return new OutputFormat(new ArrayList<>(values), values.get(0));
        }
        return this;
    }

    @Override
    public <I> void accept(Visitor<I> visitor, I input) {
        visitor.visit(this, input);
    }
}
