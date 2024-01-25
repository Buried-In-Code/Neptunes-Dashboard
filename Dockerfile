FROM eclipse-temurin:17-jre

WORKDIR /app

COPY build/libs/Neptunes-Dashboard-fatJar.jar /app/Neptunes-Dashboard.jar

ENV XDG_CACHE_HOME /app/cache
ENV XDG_CONFIG_HOME /app/config
RUN mkdir -p $XDG_CONFIG_HOME/neptunes-dashboard
ENV XDG_DATA_HOME /app/data
RUN mkdir -p $XDG_DATA_HOME/neptunes-dashboard

EXPOSE 25710

CMD ["java", "-jar", "Neptunes-Dashboard.jar"]
