package com.annimon.effybot;

import com.annimon.tgbotsmodule.BotHandler;
import com.annimon.tgbotsmodule.BotModule;
import com.annimon.tgbotsmodule.Runner;
import com.annimon.tgbotsmodule.beans.Config;
import com.annimon.tgbotsmodule.services.YamlConfigLoaderService;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.GetMe;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class Main implements BotModule {
    public static void main(String[] args) {
        final var profile = (args.length >= 1 && !args[0].isEmpty()) ? args[0] : "";
        Runner.run(profile, List.of(new Main()));
    }

    @Override
    public @NotNull BotHandler botHandler(@NotNull Config config) {
        final var configLoader = new YamlConfigLoaderService();
        final var configFile = configLoader.configFile("effybot", config.getProfile());
        final var botConfig = configLoader.loadFile(configFile, BotConfig.class);
        try {
            final var telegramClient = new OkHttpTelegramClient(botConfig.botToken());
            final User bot = telegramClient.execute(new GetMe());
            return new MainBotHandler(botConfig, bot.getUserName());
        } catch (TelegramApiException e) {
            throw new IllegalStateException("Unable to get information about bot", e);
        }
    }
}
