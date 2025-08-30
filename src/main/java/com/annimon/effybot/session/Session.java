package com.annimon.effybot.session;

import com.annimon.effybot.parameters.InputParameters;

import java.time.Instant;
import java.util.StringJoiner;

public abstract sealed class Session permits MediaSession, YtDlpSession {
    // Session key
    protected long chatId;
    protected int messageId;
    // Parameters
    protected final InputParameters inputParams = new InputParameters();
    // Meta
    private final Instant instant = Instant.now();

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public InputParameters getInputParams() {
        return inputParams;
    }

    public Instant getInstant() {
        return instant;
    }

    abstract StringJoiner describe();
}
