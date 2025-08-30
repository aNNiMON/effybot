package com.annimon.effybot;

import java.text.DecimalFormat;
import java.util.StringJoiner;

public class TextUtils {
    private TextUtils() {}

    public static boolean isNotEmpty(String str) {
        return (str != null) && (!str.isEmpty());
    }

    public static String safeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">" ,"&gt;");
    }

    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[] { "Bi", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#")
                .format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static String readableDuration(int value) {
        if (value <= 0) return "0";
        if (value < 60) return value + "s";
        final var joiner = new StringJoiner(":");
        final int hours = value / 3600;
        final int minutes = (value % 3600) / 60;
        final int seconds = value % 60;
        if (hours > 0) joiner.add("%02d".formatted(hours));
        joiner.add("%02d".formatted(minutes));
        joiner.add("%02d".formatted(seconds));
        return joiner.toString();
    }
}
