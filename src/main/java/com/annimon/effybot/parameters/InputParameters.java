package com.annimon.effybot.parameters;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Stream;

public class InputParameters {
    private String startPosition = "";
    private String duration = "";
    private String endPosition = "";

    public String getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(String ss) {
        this.startPosition = ss;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
        this.endPosition = "";
    }

    public String getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(String endPosition) {
        this.endPosition = endPosition;
        this.duration = "";
    }

    public boolean isEmpty() {
        return Stream.of(startPosition, endPosition, duration).allMatch(String::isEmpty);
    }

    public List<String> asFFmpegCommands() {
        final var commands = new ArrayList<String>();
        if (!startPosition.isEmpty()) {
            commands.add("-ss");
            commands.add(startPosition);
        }
        if (!endPosition.isEmpty()) {
            commands.add("-to");
            commands.add(endPosition);
        }
        if (!duration.isEmpty()) {
            commands.add("-t");
            commands.add(duration);
        }
        return commands;
    }

    public StringJoiner describe() {
        final var joiner = new StringJoiner("\n");
        if (!startPosition.isEmpty()) {
            joiner.add("Start: <code>%s</code>".formatted(startPosition));
        }
        if (!endPosition.isEmpty()) {
            joiner.add("End: <code>%s</code>".formatted(endPosition));
        }
        if (!duration.isEmpty()) {
            joiner.add("Duration: <code>%s</code>".formatted(duration));
        }
        return joiner;
    }
}
