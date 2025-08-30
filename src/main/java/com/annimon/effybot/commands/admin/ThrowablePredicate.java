package com.annimon.effybot.commands.admin;

import java.util.function.Predicate;

public interface ThrowablePredicate<T> {

    boolean apply(T t) throws Exception;

    static <I> Predicate<I> safePredicate(ThrowablePredicate<I> func) {
        return value -> {
            try {
                return func.apply(value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}