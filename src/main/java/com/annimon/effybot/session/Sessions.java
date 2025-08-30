package com.annimon.effybot.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class Sessions {
    private final Map<String, Session> sessions;

    public Sessions() {
        sessions = new ConcurrentHashMap<>();
    }

    public void clear() {
        sessions.clear();
    }

    public int getSize() {
        return sessions.size();
    }

    public Stream<Session> sessions() {
        return sessions.values().stream();
    }

    public Session get(long chatId, long messageId) {
        return sessions.get(mapKey(chatId, messageId));
    }

    public MediaSession getMediaSession(long chatId, long messageId) {
        return (MediaSession) get(chatId, messageId);
    }

    public YtDlpSession getYtDlpSession(long chatId, long messageId) {
        return (YtDlpSession) get(chatId, messageId);
    }

    public void put(Session mediaSession) {
        sessions.put(mapKey(mediaSession.getChatId(), mediaSession.getMessageId()), mediaSession);
    }

    public void put(long chatId, long messageId, Session mediaSession) {
        sessions.put(mapKey(chatId, messageId), mediaSession);
    }

    private String mapKey(long chatId, long messageId) {
        return chatId + "/" + messageId;
    }
}
