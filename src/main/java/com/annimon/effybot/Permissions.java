package com.annimon.effybot;

import com.annimon.tgbotsmodule.commands.authority.Authority;
import com.annimon.tgbotsmodule.commands.authority.For;
import com.annimon.tgbotsmodule.services.CommonAbsSender;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.EnumSet;
import java.util.Set;

public record Permissions(Set<Long> superUsers, Set<Long> allowedUsers) implements Authority<For> {
    public static final EnumSet<For> SUPERUSERS = EnumSet.of(For.CREATOR);
    public static final EnumSet<For> ALLOWED_USERS = EnumSet.of(For.CREATOR, For.ADMIN);

    @Override
    public boolean hasRights(@NotNull CommonAbsSender sender, @NotNull Update update,
                             @NotNull User user, @NotNull EnumSet<For> roles) {
        final long userId = user.getId();
        return hasAccess(userId, roles);
    }

    boolean hasAccess(long userId, @NotNull EnumSet<For> roles) {
        if (roles.contains(For.CREATOR) && superUsers().contains(userId))
            return true;
        if (roles.contains(For.ADMIN) && isUserAllowed(userId))
            return true;
        return roles.contains(For.USER);
    }

    boolean isUserAllowed(Long userId) {
        return superUsers().contains(userId)
                || allowedUsers().contains(userId);
    }
}
