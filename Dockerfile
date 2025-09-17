FROM --platform=$BUILDPLATFORM gradle:jdk21 AS builder

WORKDIR /data
COPY . /data/
RUN gradle build



FROM --platform=$TARGETPLATFORM eclipse-temurin:21.0.7_6-jre

WORKDIR /app
COPY --from=builder /data/build/libs/Neptunes-Dashboard-4.1.0-all.jar /app/Neptunes-Dashboard.jar
ENV XDG_CACHE_HOME=/app/cache \
    XDG_CONFIG_HOME=/app/config \
    XDG_DATA_HOME=/app/data
RUN mkdir -p $XDG_CONFIG_HOME/neptunes-dashboard $XDG_DATA_HOME/neptunes-dashboard

EXPOSE 25710
CMD ["java", "-jar", "Neptunes-Dashboard.jar"]
