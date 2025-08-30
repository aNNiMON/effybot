package com.annimon.effybot.parameters;

import com.annimon.effybot.commands.ffmpeg.Visitor;

public class DisableAudio extends BooleanParameter {
    public static final String ID = "noaud";

    public DisableAudio() {
        super(ID, "Disable audio", false);
    }

    @Override
    public <I> void accept(Visitor<I> visitor, I input) {
        visitor.visit(this, input);
    }
}
