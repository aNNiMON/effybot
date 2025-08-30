package com.annimon.effybot.session;

import com.annimon.effybot.parameters.Parameter;
import com.annimon.effybot.parameters.Parameters;

import java.io.File;
import java.util.StringJoiner;

import static com.annimon.effybot.TextUtils.*;

public final class MediaSession extends Session {
    // Media info
    private FileType fileType;
    private String fileId;
    private String originalFilename;
    private Long fileSize;
    private Integer duration;
    private String resolution;
    // Parameters
    private Parameter<?> selectedParam;
    private Parameters params;
    // Files
    private File inputFile;
    private File outputFile;
    // Status
    private String status;

    public void fromFileInfo(FileInfo fileInfo) {
        this.setFileId(fileInfo.fileId());
        this.setFileType(fileInfo.fileType());
        this.setFileSize(fileInfo.fileSize());
        this.setDuration(fileInfo.duration());
        this.setResolution(fileInfo.width(), fileInfo.height());
        this.setOriginalFilename(fileInfo.filename());
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setResolution(Integer width, Integer height) {
        if (width == null && height == null) {
            this.resolution = null;
        } else {
            this.resolution = (width != null ? width : "?") + "x" + (height != null ? height : "?");
        }
    }

    public Parameters getParams() {
        return params;
    }

    public void setParams(Parameters params) {
        this.params = params;
    }

    public void setSelectedParam(Parameter<?> selectedParam) {
        this.selectedParam = selectedParam;
    }

    public Parameter<?> getSelectedParam() {
        return selectedParam;
    }

    public boolean isDownloaded() {
        return (inputFile != null) && (inputFile.canRead());
    }

    public File getInputFile() {
        return inputFile;
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public StringJoiner describe() {
        final var joiner = new StringJoiner("\n");
        joiner.add("Type: <code>%s</code>".formatted(fileType));
        if (fileSize != null && fileSize > 0) {
            joiner.add("File size: <code>%s</code>".formatted(readableFileSize(fileSize)));
        }
        if (duration != null && duration > 0) {
            joiner.add("Duration: <code>%s</code>".formatted(readableDuration(duration)));
        }
        if (resolution != null) {
            joiner.add("Resolution: <code>%s</code>".formatted(resolution));
        }
        joiner.merge(inputParams.describe());
        if (originalFilename != null) {
            joiner.add("Filename: <code>%s</code>".formatted(safeHtml(originalFilename)));
        }
        if (inputFile != null && inputFile.canRead()) {
            joiner.add("Input file: <code>%s</code>".formatted(safeHtml(inputFile.getName())));
            joiner.add("Input Size: <code>%s</code>".formatted(readableFileSize(inputFile.length())));
        }
        if (outputFile != null && outputFile.canRead()) {
            joiner.add("Output size: <code>%s</code>".formatted(readableFileSize(outputFile.length())));
        }
        if (status != null) {
            joiner.add("<i>" + safeHtml(status) + "</i>");
        }
        if (selectedParam != null) {
            joiner.add(safeHtml(selectedParam.describe()));
        }
        return joiner;
    }

    @Override
    public String toString() {
        return describe().toString();
    }
}
