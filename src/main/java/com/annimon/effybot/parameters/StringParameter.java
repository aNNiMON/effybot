package com.annimon.effybot.parameters;

import java.util.List;

public abstract class StringParameter extends Parameter<String> {
    protected StringParameter(String id, String name, List<String> possibleValues, String value) {
        super(id, name, possibleValues, value);
    }
}
