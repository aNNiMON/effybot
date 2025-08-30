package com.annimon.effybot.commands.admin;

import java.util.function.Function;

public interface ThrowableFunction<T, R> {

    R apply(T t) throws Exception;

    static <I, O> Function<I, O> safeFunction(ThrowableFunction<I, O> func) {
        return value -> {
            try {
                return func.apply(value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}