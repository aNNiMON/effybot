FROM gradle:9.0.0-jdk17-alpine AS cache
RUN mkdir -p /home/gradle/cache_home
ENV GRADLE_USER_HOME=/home/gradle/cache_home
COPY build.gradle /home/gradle/java-code/
WORKDIR /home/gradle/java-code
RUN GRADLE_OPTS="-Xmx256m" gradle build --build-cache --stacktrace -i --no-daemon

FROM gradle:9.0.0-jdk17-alpine AS builder
COPY --from=cache /home/gradle/cache_home /home/gradle/.gradle
COPY . /usr/src/java-code
WORKDIR /usr/src/java-code
RUN GRADLE_OPTS="-Xmx256m" gradle shadowJar --build-cache --stacktrace --no-daemon

# Using 3.11 here, otherwise it requires gcc and g++ to compile TgCrypto, resulting in 2x bigger image
FROM python:3.11-alpine
ENV JAVA_HOME=/opt/java/openjdk
COPY --from=eclipse-temurin:17-jre-alpine $JAVA_HOME $JAVA_HOME
ENV PATH="${JAVA_HOME}/bin:${PATH}"
RUN apk add --no-cache py3-pip ffmpeg \
  && python3 -m pip install --break-system-packages --no-cache-dir --upgrade \
       wheel yt-dlp pyrogram TgCrypto
WORKDIR /app
COPY --from=builder /usr/src/java-code/build/libs/ffmpegbot-1.2-SNAPSHOT-all.jar ffmpegbot.jar
RUN mkdir input && mkdir output
COPY pytgfile.py .
COPY ffmpegbot-docker.yaml .
ENTRYPOINT ["java", "-jar", "/app/ffmpegbot.jar", "docker"]