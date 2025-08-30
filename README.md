# effybot (formerly ffmpegbot)

Telegram Bot for re-encoding media

## Features

 - change a video resolution, bitrate, frame rate, speed
 - change an audio bitrate, volume, pitch
 - apply audio effects, compressor
 - extract or remove audio from the video
 - resend video note as a regular video, or video as an audio track
 - output audio spectrum for quick analysis

## Requirements

 - Telegram bot username and token, [@BotFather](https://t.me/BotFather)
 - JRE 17+ or JDK 17+ (for build)
 - `ffmpeg` must be installed and available in `PATH`.
 - `python3` version 3.8+ must be installed and available in `PATH`.
 - `yt-dlp` for `/dl` command.

## Installation

### Manual

```bash
# Build
./gradlew shadowJar
# Config
cp effybot.yaml.template effybot.yaml
vim effybot.yaml
mkdir {input,output}
# Run
java -jar ./build/libs/effybot-1.2-SNAPSHOT-all.jar
```


### Using Docker

Note: FFmpeg binary might be installed with limited number of filters and codecs. Some bot features might not work (Audio pitch, robot effect, etc.).

```bash
docker run -d -t -i \
  -e BOT_TOKEN='...' \
  -e APP_ID='...' \
  -e APP_HASH='...' \
  -e SUPERUSERS='12345' \
  -e ALLOWED_USERS='12346,12347' \
  --name effybot ghcr.io/annimon/effybot:latest
```

#### Environment variables

 - `BOT_TOKEN` — Telegram bot token
 - `APP_ID` — Telegram API app_id (see https://core.telegram.org/api/obtaining_api_id)
 - `APP_HASH` — Telegram API app_hash
 - `SUPERUSERS` — Comma-separated list of superusers. Superuser can execute /run command
 - `ALLOWED_USERS` — Comma-separated list of allowed user ids
