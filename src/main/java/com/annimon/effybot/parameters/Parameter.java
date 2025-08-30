package com.annimon.effybot.parameters;

import com.annimon.effybot.commands.ffmpeg.Visitor;

import java.util.List;
import java.util.Objects;

public abstract class Parameter<T> {
    protected final String id;
    protected final String displayName;
    protected final List<? extends T> possibleValues;
    protected T value;
    private boolean enabled;

    protected Parameter(String id, String displayName, List<? extends T> values, T value) {
        this.id = id;
        this.displayName = displayName;
        this.possibleValues = values;
        this.value = value;
        this.enabled = true;
        if (values.isEmpty())
            throw new IllegalArgumentException("Possible values cannot be empty");
        if (!values.contains(value))
            throw new IllegalArgumentException("Possible values " + values + " must contain " + value);
    }

    public String getId() {
        return id;
    }

    public T getValue() {
        return value;
    }

    public int getPossibleValuesSize() {
        return possibleValues.size();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public abstract <I> void accept(Visitor<I> visitor, I input);

    public final String describe() {
        return displayName + ": " + describeValue(value);
    }

    public final String describeValueByIndex(int index) {
        return describeValue(safeGet(index));
    }

    protected String describeValue(T valueToDescribe) {
        return Objects.toString(valueToDescribe);
    }

    public void select(int index) {
        value = safeGet(index);
    }

    public int defaultColumnsCount() {
        return 2;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + displayName + ": " + value;
    }

    private T safeGet(int index) {
        final int size = possibleValues.size();
        if (index >= size) index = size - 1;
        else if (index < 0) index = 0;
        return possibleValues.get(index);
    }
}
