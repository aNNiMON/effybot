package com.annimon.effybot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BotConfig(String appId, String appHash,
                        String botToken,
                        String downloaderScript,
                        String superUsers, String allowedUsers) {

    Set<Long> superUserIds() {
        return toLongs(superUsers);
    }

    Set<Long> allowedUserIds() {
        return toLongs(allowedUsers);
    }

    private static Set<Long> toLongs(String input) {
        if (input == null || input.isEmpty()) {
            return Collections.emptySet();
        }
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(str -> str.matches("^\\d{1,15}$"))
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }
}
