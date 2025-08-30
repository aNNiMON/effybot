package com.annimon.effybot.parameters;

import org.jetbrains.annotations.NotNull;
import java.util.*;
import java.util.stream.Stream;

public class Parameters extends AbstractCollection<Parameter<?>> {

    private final Map<String, Parameter<?>> parameters;

    public Parameters() {
        parameters = new LinkedHashMap<>();
    }

    @Override
    public boolean add(Parameter<?> parameter) {
        parameter.enable();
        return !Objects.equals(parameter, parameters.put(parameter.id, parameter));
    }

    public Parameter<?> findById(String id) {
        final var parameter = parameters.get(id);
        if (parameter != null && parameter.isEnabled()) {
            return parameter;
        }
        return null;
    }

    public <P extends Parameter<?>> Optional<P> findById(String id, Class<P> clazz) {
        return Optional.ofNullable(findById(id))
                .filter(clazz::isInstance)
                .map(clazz::cast);
    }

    public void disable(String id) {
        Optional.ofNullable(parameters.get(id))
                .ifPresent(Parameter::disable);
    }

    public void disableAll(Collection<String> ids) {
        ids.forEach(this::disable);
    }

    public void enable(String id) {
        Optional.ofNullable(parameters.get(id))
                .ifPresent(Parameter::enable);
    }

    public void enableAll(Collection<String> ids) {
        ids.forEach(this::enable);
    }

    @NotNull
    @Override
    public Iterator<Parameter<?>> iterator() {
        return enabledParameters().iterator();
    }

    @Override
    public int size() {
        return (int) enabledParameters().count();
    }

    private Stream<Parameter<?>> enabledParameters() {
        return parameters.values()
                .stream()
                .filter(Parameter::isEnabled);
    }
}
