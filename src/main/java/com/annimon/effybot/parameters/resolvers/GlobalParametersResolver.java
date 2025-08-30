package com.annimon.effybot.parameters.resolvers;

import com.annimon.effybot.parameters.Parameters;
import com.annimon.effybot.session.FileInfo;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class GlobalParametersResolver implements ParametersResolver {
    private final List<ParametersResolver> resolvers;

    public GlobalParametersResolver() {
        resolvers = new ArrayList<>();
        resolvers.add(new AudioResolver());
        resolvers.add(new MultiAudioStreamsResolver());
        resolvers.add(new VideoResolver());
        resolvers.add(new SpeedFactorResolver());
        resolvers.add(new OutputFormatResolver());
    }

    @Override
    public void resolve(@NotNull Parameters parameters, @NotNull FileInfo fileInfo) {
        for (ParametersResolver resolver : resolvers) {
            resolver.resolve(parameters, fileInfo);
        }
    }

    @Override
    public void refine(@NotNull Parameters parameters) {
        for (ParametersResolver resolver : resolvers) {
            resolver.refine(parameters);
        }
    }
}
