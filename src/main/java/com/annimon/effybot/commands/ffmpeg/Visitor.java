package com.annimon.effybot.commands.ffmpeg;

import com.annimon.effybot.parameters.*;

public interface Visitor<I> {
    void visit(DisableAudio p, I input);
    void visit(AudioBitrate p, I input);
    void visit(AudioCompressor p, I input);
    void visit(AudioCrystalizer p, I input);
    void visit(AudioEffect p, I input);
    void visit(AudioPitch p, I input);
    void visit(AudioVolume p, I input);
    void visit(AudioStreamByLanguage p, I input);
    void visit(Contrast p, I input);
    void visit(Gamma p, I input);
    void visit(Saturation p, I input);
    void visit(SpeedFactor p, I input);
    void visit(VideoBitrate p, I input);
    void visit(VideoScale p, I input);
    void visit(VideoFrameRate p, I input);
    void visit(OutputFormat p, I input);
}