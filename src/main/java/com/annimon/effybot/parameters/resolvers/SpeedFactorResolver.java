package com.annimon.effybot.parameters.resolvers;

import com.annimon.effybot.parameters.Parameters;
import com.annimon.effybot.parameters.SpeedFactor;
import com.annimon.effybot.session.FileInfo;
import org.jetbrains.annotations.NotNull;

public class SpeedFactorResolver implements ParametersResolver {

    @Override
    public void resolve(@NotNull Parameters parameters, @NotNull FileInfo fileInfo) {
        // For anything except static images
        parameters.add(new SpeedFactor());
    }
}
