package com.annimon.ffmpegbot.commands.ytdlp;

import com.annimon.ffmpegbot.file.FilePath;
import com.annimon.ffmpegbot.session.YtDlpInfo;
import com.annimon.ffmpegbot.session.YtDlpSession;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class YtDlpTask {

    public void getInfo(YtDlpSession session) {
        final var commandBuilder = new YtDlpCommandBuilder();
        final var mapper = new ObjectMapper();
        try {
            run(commandBuilder.buildInfoCommand(session));
            final var jsonFile = FilePath.outputFile(session.getJsonInfoFilename());
            if (jsonFile.exists() && jsonFile.canRead()) {
                session.setAdditionalInfo(mapper.readValue(jsonFile, YtDlpInfo.class));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void process(YtDlpSession session) {
        final var commandBuilder = new YtDlpCommandBuilder();
        run(commandBuilder.buildCommand(session));
    }

    private void run(String[] args) {
        try {
            final ProcessBuilder pb = new ProcessBuilder(args);
            pb.redirectErrorStream(true);
            pb.inheritIO();
            final Process process = pb.start();
            int status = process.waitFor();
            if (status != 0) {
                throw new RuntimeException("yt-dlp process was finished with non-zero value " + status);
            }
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
