package com.annimon.effybot.session;

import org.apache.commons.io.FilenameUtils;

import java.util.StringJoiner;

public final class YtDlpSession extends Session {
    private final String url;
    private final String downloadOption;
    private final FileType fileType;
    private YtDlpInfo additionalInfo;
    private String outputFilename;

    public YtDlpSession(String url, String downloadOption, FileType fileType) {
        this.url = url;
        this.downloadOption = downloadOption;
        this.fileType = fileType;
    }

    public String getUrl() {
        return url;
    }

    public String getDownloadOption() {
        return downloadOption;
    }

    public FileType getFileType() {
        return fileType;
    }

    public String getOutputFilename() {
        return outputFilename;
    }

    public void setOutputFilename(String outputFilename) {
        this.outputFilename = outputFilename;
    }

    public String getJsonInfoFilename() {
        return FilenameUtils.getBaseName(outputFilename) + ".info.json";
    }

    public boolean hasAdditionalInfo() {
        return additionalInfo != null;
    }

    public void setAdditionalInfo(YtDlpInfo additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @Override
    public StringJoiner describe() {
        final var joiner = new StringJoiner("\n");
        joiner.add("URL: <code>%s</code>".formatted(url));
        joiner.add("Type: <code>%s</code>".formatted(fileType));
        joiner.merge(inputParams.describe());
        if (hasAdditionalInfo()) {
            joiner.merge(additionalInfo.describe());
        }
        return joiner;
    }

    @Override
    public String toString() {
        return describe().toString();
    }
}
