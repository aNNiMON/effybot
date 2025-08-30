package com.annimon.effybot.commands.ffmpeg;

import com.annimon.effybot.parameters.Parameter;
import com.annimon.effybot.session.MediaSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringJoiner;

public class FFmpegTask {

    public void process(MediaSession session) {
        final var ffmpeg = new FFmpegCommandBuilder();
        for (Parameter<?> p : session.getParams()) {
            p.accept(ffmpeg, session);
        }

        try {
            final ProcessBuilder pb = new ProcessBuilder(ffmpeg.buildCommand(session));
            pb.redirectErrorStream(true);
            pb.inheritIO();
            session.setStatus("Starting ffmpeg");
            final var process = pb.start();
            final var lines = new StringJoiner("\n");
            try (final var reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                    session.setStatus(line);
                }
                int status = process.waitFor();
                if (status != 0) {
                    session.setStatus(lines.toString());
                    throw new RuntimeException("ffmpeg process was finished with non-zero value " + status);
                }
            }
        } catch (InterruptedException | IOException e) {
            session.setStatus("Failed due to " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
