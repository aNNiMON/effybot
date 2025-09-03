package com.annimon.effybot.commands.ffmpeg;

import com.annimon.effybot.parameters.*;
import com.annimon.effybot.file.FilePath;
import com.annimon.effybot.session.FileType;
import com.annimon.effybot.session.FileTypes;
import com.annimon.effybot.session.MediaSession;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FFmpegCommandBuilder implements Visitor<MediaSession> {

    private boolean discardAudio;
    private String recipe;
    private final List<String> audioCommands;
    private final List<String> videoCommands;
    private final List<String> audioFilters;
    private final List<String> videoFilters;
    private final List<String> eq;

    public FFmpegCommandBuilder() {
        audioCommands = new ArrayList<>();
        videoCommands = new ArrayList<>();
        audioFilters = new ArrayList<>();
        videoFilters = new ArrayList<>();
        eq = new ArrayList<>();
        recipe = "";
    }

    @Override
    public void visit(DisableAudio p, MediaSession session) {
        discardAudio = p.getValue();
        if (discardAudio) {
            audioCommands.add("-an");
        }
    }

    @Override
    public void visit(AudioBitrate p, MediaSession session) {
        if (discardAudio) return;
        if (p.getValue().isEmpty()) return;
        audioCommands.add("-b:a");
        audioCommands.add(p.getValue());
    }

    @Override
    public void visit(AudioCompressor p, MediaSession input) {
        if (discardAudio) return;
        if (p.getValue().isEmpty()) return;
        audioFilters.add(switch (p.getValue()) {
            case AudioCompressor.COMP_2_6DB -> "compand=points=-80/-80|-6/-6|0/-3.8|20/3.5";
            case AudioCompressor.COMP_2_12DB -> "compand=points=-80/-80|-12/-12|0/-6.8|20/1.9";
            case AudioCompressor.COMP_2_18DB -> "compand=points=-80/-80|-18/-18|0/-9.8|20/0.7";
            case AudioCompressor.COMP_3_9DB -> "compand=points=-80/-80|-9/-9|0/-3.8|20/-2.1";
            case AudioCompressor.COMP_3_18DB -> "compand=points=-80/-80|-18/-18|0/-6.8|20/-4.5";
            case AudioCompressor.COMP_NORMALIZE_1 -> "compand=.3|.3:1|1:-90/-60|-60/-40|-40/-30|-20/-20:6:0:-90:0.2";
            case AudioCompressor.COMP_NORMALIZE_2 -> "compand=0|0:1|1:-90/-900|-70/-70|-30/-9|0/-3:6:0:0:0";
            case AudioCompressor.COMP_NOISE_GATE_1 -> "compand=points=-80/-900|-45/-15|-27/-9|0/-7|20/-7:attacks=0";
            case AudioCompressor.COMP_NOISE_GATE_2 -> "compand=.1|.1:.2|.2:-900/-900|-50.1/-900|-50/-50:.01:0:-90:.1";
            case AudioCompressor.COMP_HARD_LIMITER_6DB -> "compand=attacks=0:points=-80/-80|-6/-6|20/-6";
            case AudioCompressor.COMP_HARD_LIMITER_12DB -> "compand=attacks=0:points=-80/-80|-12/-12|20/-12";
            case AudioCompressor.COMP_HARD_NOISE_GATE_35DB -> "compand=attacks=0:points=-80/-115|-35.1/-80|-35/-35|20/20";
            case AudioCompressor.COMP_SOFT_LIMITER -> "compand=attacks=0:points=-80/-80|-12.4/-12.4|-6/-8|0/-6.8|20/-2.8";
            case AudioCompressor.COMP_EXPANDER -> "compand=attacks=0:points=-80/-169|-54/-80|-49.5/-64.6|-41.1/-41.1|-25.8/-15|-10.8/-4.5|0/0|20/8.3";
            case AudioCompressor.COMP_GATE -> "compand=points=-80/-105|-62/-80|-15.4/-15.4|0/-12|20/-7.6";
            default -> "compand";
        });
    }

    @Override
    public void visit(AudioCrystalizer p, MediaSession input) {
        if (discardAudio) return;
        final String value = p.getValue();
        if (value.isEmpty() || value.equals("0")) return;
        audioFilters.add("crystalizer=" + value);
    }

    @Override
    public void visit(AudioEffect p, MediaSession input) {
        if (discardAudio) return;
        if (p.getValue().isEmpty()) return;
        audioFilters.add(switch (p.getValue()) {
            case AudioEffect.ECHO -> "aecho=0.8:0.9:40|50|70:0.4|0.3|0.2";
            case AudioEffect.ECHO_2 -> "aecho=0.8:0.9:500|1000:0.2|0.1";
            case AudioEffect.NOISE_REDUCTION_5 -> "afftdn=nr=5";
            case AudioEffect.NOISE_REDUCTION_12 -> "afftdn=nr=12";
            case AudioEffect.PULSATOR -> "apulsator=mode=sine:hz=0.5";
            case AudioEffect.VIBRATO -> "vibrato=f=4";
            default /* AudioEffect.ROBOT */ -> "afftfilt=" +
                    "real='hypot(re,im)*sin(0)'" +
                    ":imag='hypot(re,im)*cos(0)'" +
                    ":win_size=512:overlap=0.75";
        });
    }

    @Override
    public void visit(AudioPitch p, MediaSession input) {
        if (discardAudio) return;
        if (p.getValue().equals("1")) return;
        audioFilters.add("rubberband=pitchq=quality:pitch=" + p.getValue());
    }

    @Override
    public void visit(AudioVolume p, MediaSession session) {
        if (discardAudio) return;
        if (p.getValue().isEmpty()) return;
        audioFilters.add("volume=" + p.getValue());
    }

    @Override
    public void visit(AudioStreamByLanguage p, MediaSession input) {
        if (discardAudio) return;
        if (p.getValue().isEmpty()) return;
        audioCommands.add("-map");
        audioCommands.add("0:m:language:" + p.getValue());
    }

    @Override
    public void visit(Contrast p, MediaSession input) {
        String value = p.getValue();
        if (value.equals("1")) return;
        eq.add("contrast=" + value);
    }

    @Override
    public void visit(Gamma p, MediaSession input) {
        String value = p.getValue();
        if (value.equals("1")) return;
        eq.add("gamma=" + value);
    }

    @Override
    public void visit(Saturation p, MediaSession input) {
        String value = p.getValue();
        if (value.equals("1")) return;
        eq.add("saturation=" + value);
    }

    @Override
    public void visit(SpeedFactor p, MediaSession input) {
        if (p.getValue().equals("1")) return;
        if (!discardAudio) {
            audioFilters.add("atempo=" + p.getValue());
        }
        videoFilters.add("setpts=PTS/" + p.getValue());
    }

    @Override
    public void visit(VideoBitrate p, MediaSession session) {
        if (p.getValue().isEmpty()) return;
        videoCommands.add("-b:v");
        videoCommands.add(p.getValue());
    }

    @Override
    public void visit(VideoScale p, MediaSession session) {
        if (p.getValue().isEmpty()) return;
        videoFilters.add("scale=-2:" + p.getValue());
    }

    @Override
    public void visit(VideoFrameRate p, MediaSession input) {
        if (p.getValue().isEmpty()) return;
        videoFilters.add("fps=" + p.getValue());
    }

    @Override
    public void visit(OutputFormat p, MediaSession session) {
        final String localFilename = session.getInputFile().getName();
        String additionalExtension = "";
        recipe = "";

        switch (p.getValue()) {
            case OutputFormat.VIDEO -> {
                session.setFileType(FileType.VIDEO);
                additionalExtension = ".mp4";
            }
            case OutputFormat.VIDEO_NOTE -> {
                session.setFileType(FileType.VIDEO_NOTE);
                additionalExtension = ".mp4";
            }
            case OutputFormat.AUDIO -> {
                session.setFileType(FileType.AUDIO);
                additionalExtension = ".mp3";
            }
            case OutputFormat.AUDIO_SPECTRUM -> {
                session.setFileType(FileType.PHOTO);
                additionalExtension = ".jpg";
                recipe = OutputFormat.AUDIO_SPECTRUM;
            }
            default -> throw new IllegalStateException("Not supported output format");
        }

        if (localFilename.toLowerCase(Locale.ENGLISH).endsWith(additionalExtension)) {
            additionalExtension = "";
        }
        session.setOutputFile(FilePath.outputFile(localFilename + additionalExtension));
    }

    private List<String> visitRecipe(String recipe) {
        return switch (recipe) {
            case OutputFormat.AUDIO_SPECTRUM -> List.of(
                    "-vn",
                    "-filter_complex",
                    Stream.concat(audioFilters.stream(), Stream.of("showspectrumpic=s=1200x640:mode=separate"))
                                    .collect(Collectors.joining(",")),
                    "-frames:v", "1"
            );
            default -> List.of();
        };
    }

    public String[] buildCommand(final @NotNull MediaSession session) {
        final var commands = new ArrayList<String>();
        commands.addAll(List.of("ffmpeg", "-loglevel", "error", "-stats"));
        commands.addAll(session.getInputParams().asFFmpegCommands());
        commands.addAll(List.of("-i", FilePath.inputDir() + "/" + session.getInputFile().getName()));
        if (StringUtils.isNotEmpty(recipe)) {
            commands.addAll(visitRecipe(recipe));
        } else {
            if (FileTypes.canContainAudio(session.getFileType())) {
                commands.addAll(audioCommands);
                if (!audioFilters.isEmpty()) {
                    commands.add("-af");
                    commands.add(String.join(",", audioFilters));
                }
            }
            if (FileTypes.canContainVideo(session.getFileType())) {
                commands.addAll(videoCommands);
                if (!eq.isEmpty()) {
                    videoFilters.add("eq=" + String.join(":", eq));
                }
                if (!videoFilters.isEmpty()) {
                    commands.add("-vf");
                    commands.add(String.join(",", videoFilters));
                }
            }
        }
        commands.addAll(List.of("-y", FilePath.outputDir() + "/" + session.getOutputFile().getName()));
        return commands.toArray(String[]::new);
    }
}
