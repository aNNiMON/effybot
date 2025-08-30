package com.annimon.effybot.commands.ffmpeg;

import com.annimon.effybot.file.FileDownloadException;
import com.annimon.effybot.file.FileDownloader;
import com.annimon.effybot.file.FilePath;
import com.annimon.effybot.parameters.Parameters;
import com.annimon.effybot.parameters.resolvers.GlobalParametersResolver;
import com.annimon.effybot.parameters.resolvers.ParametersResolver;
import com.annimon.effybot.session.MediaSession;
import com.annimon.effybot.session.Resolver;
import com.annimon.effybot.session.Sessions;
import com.annimon.tgbotsmodule.api.methods.Methods;
import com.annimon.tgbotsmodule.commands.CommandBundle;
import com.annimon.tgbotsmodule.commands.CommandRegistry;
import com.annimon.tgbotsmodule.commands.SimpleCallbackQueryCommand;
import com.annimon.tgbotsmodule.commands.authority.For;
import com.annimon.tgbotsmodule.commands.context.CallbackQueryContext;
import com.annimon.tgbotsmodule.services.CommonAbsSender;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.annimon.effybot.Permissions.ALLOWED_USERS;
import static com.annimon.effybot.commands.ffmpeg.CallbackQueryCommands.*;
import static com.annimon.effybot.commands.ffmpeg.MediaProcessingKeyboard.createKeyboard;

public class MediaProcessingBundle implements CommandBundle<For> {

    private final Sessions sessions;
    private final FileDownloader fileDownloader;
    private final ParametersResolver parametersResolver;

    public MediaProcessingBundle(Sessions sessions, FileDownloader fileDownloader) {
        this.sessions = sessions;
        this.fileDownloader = fileDownloader;
        this.parametersResolver = new GlobalParametersResolver();
    }

    @Override
    public void register(@NotNull CommandRegistry commands) {
        commands.register(new SimpleCallbackQueryCommand(PARAMETER, ALLOWED_USERS, sessionCommand(this::parameter)));
        commands.register(new SimpleCallbackQueryCommand(PROCESS, ALLOWED_USERS, sessionCommand(this::process)));
    }

    public void handleMessage(final @NotNull CommonAbsSender sender, final @NotNull Message message) {
        final var fileInfo = Resolver.resolveFileInfo(message);
        if (fileInfo == null) return;

        final var session = new MediaSession();
        session.setChatId(message.getChatId());
        session.fromFileInfo(fileInfo);

        final var params = new Parameters();
        parametersResolver.resolve(params, fileInfo);
        session.setParams(params);

        final var result = Methods.sendMessage()
                .setChatId(message.getChatId())
                .setText(session.toString())
                .enableHtml()
                .setReplyToMessageId(message.getMessageId())
                .call(sender);

        session.setMessageId(result.getMessageId());
        sessions.put(session);

        Methods.editMessageReplyMarkup(result.getChatId(), result.getMessageId())
                .setReplyMarkup(createKeyboard(session))
                .call(sender);
    }

    private void parameter(final CallbackQueryContext ctx, final MediaSession session) {
        if (ctx.argumentsLength() == 0) {
            session.setSelectedParam(null);
        } else {
            final String id = ctx.argument(0);
            final var param = session.getParams().findById(id);
            session.setSelectedParam(param);

            if (param != null && ctx.argumentsLength() == 2) {
                final int index = Integer.parseInt(ctx.argument(1));
                param.select(index);
                parametersResolver.refine(session.getParams());
                session.setSelectedParam(null);
            }
        }
        editMessage(ctx, session);
    }

    private void process(final CallbackQueryContext ctx, final MediaSession session) {
        if (!session.isDownloaded()) {
            sendAction(ctx, session);
            download(ctx, session);
        }
        if (!session.isDownloaded()) {
            session.setStatus("The file is not downloaded yet");
            editMessage(ctx, session);
            return;
        }

        sendAction(ctx, session);
        CompletableFuture.runAsync(() -> new FFmpegTask().process(session))
                .thenRunAsync(() -> {
                    editMessage(ctx, session);
                    Resolver.resolveMethod(session.getFileType())
                            .setChatId(session.getChatId())
                            .setFile(session.getOutputFile())
                            .call(ctx.sender);
                })
                .exceptionallyAsync(throwable -> {
                    editMessage(ctx, session);
                    return null;
                });
    }

    private void download(final CallbackQueryContext ctx, final MediaSession session) {
        try {
            final String defaultFilename = FilePath.defaultFilename(session.getFileType());
            final var file = fileDownloader.downloadFile(ctx.sender, session.getFileId(), defaultFilename);
            final var filename = FilePath.generateFilename(session.getFileId(), file.getName());
            session.setInputFile(FilePath.inputFile(filename));
            file.renameTo(session.getInputFile());
            session.setOutputFile(FilePath.outputFile(filename));
        } catch (FileDownloadException e) {
            session.setStatus("Unable to download due to " + e.getMessage());
            editMessage(ctx, session);
        }
    }

    private static void editMessage(CallbackQueryContext ctx, MediaSession session) {
        ctx.editMessage(session.toString())
                .enableHtml()
                .setReplyMarkup(createKeyboard(session))
                .callAsync(ctx.sender);
    }

    private static void sendAction(CallbackQueryContext ctx, MediaSession session) {
        Methods.sendChatAction(session.getChatId(), Resolver.resolveAction(session.getFileType()))
                .callAsync(ctx.sender);
    }

    private Consumer<CallbackQueryContext> sessionCommand(BiConsumer<CallbackQueryContext, MediaSession> consumer) {
        return ctx -> {
            final var msg = ctx.message();
            if (msg == null) return;

            final var session = sessions.getMediaSession(msg.getChatId(), msg.getMessageId());
            if (session == null) return;

            consumer.accept(ctx, session);
        };
    }
}
