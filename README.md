<img src="./src/main/resources/static/img/logo.png" align="left" width="128" height="128" alt="Budget Logo"/>

# Budget

![Java Version](https://img.shields.io/badge/Temurin-17-green?style=flat-square&logo=eclipse-adoptium)
![Kotlin Version](https://img.shields.io/badge/Kotlin-1.9.22-green?style=flat-square&logo=kotlin)
![Status](https://img.shields.io/badge/Status-Alpha-yellow?style=flat-square)

[![Gradle](https://img.shields.io/badge/Gradle-8.5-informational?style=flat-square&logo=gradle)](https://github.com/gradle/gradle)
[![Ktlint](https://img.shields.io/badge/Ktlint-1.1.0-informational?style=flat-square)](https://github.com/pinterest/ktlint)
[![Javalin](https://img.shields.io/badge/Javalin-5.6.3-informational?style=flat-square)](https://github.com/javalin/javalin)
[![Bulma](https://img.shields.io/badge/Bulma-0.9.4-informational?style=flat-square)](https://github.com/jgthms/bulma)

[![Github - Version](https://img.shields.io/github/v/tag/Buried-In-Code/Neptunes-Dashboard?logo=Github&label=Version&style=flat-square)](https://github.com/Buried-In-Code/Neptunes-Dashboard/tags)
[![Github - License](https://img.shields.io/github/license/Buried-In-Code/Neptunes-Dashboard?logo=Github&label=License&style=flat-square)](https://opensource.org/licenses/MIT)
[![Github - Contributors](https://img.shields.io/github/contributors/Buried-In-Code/Neptunes-Dashboard?logo=Github&label=Contributors&style=flat-square)](https://github.com/Buried-In-Code/Neptunes-Dashboard/graphs/contributors)

Pulls game information for Neptune's Pride and attempts to display it in a simple Web Interface with REST endpoints.

_Currently only supports **Triton** games_

## Usage

### via Github

1. Make sure you have a supported version of [Java](https://adoptium.net/en-GB/temurin/releases/) installed: `java --version`
2. Clone the repo: `git clone https://github.com/Buried-In-Code/Neptunes-Dashboard`
3. Build using: `./gradlew build`
4. Run using: `java -jar /app/build/libs/Neptunes-Dashboard-fatJar.jar`

### via Gradle

1. Make sure you have a supported version of [Java](https://adoptium.net/en-GB/temurin/releases/) installed: `java --version`
2. Clone the repo: `git clone https://github.com/Buried-In-Code/Neptunes-Dashboard`
3. Run using: `./gradlew build run`

### via Docker-Compose

1. Make sure you have [Docker](https://www.docker.com/) installed: `docker --version`
2. Make sure you have [Docker-Compose](https://github.com/docker/compose) installed: `docker-compose --version`
3. Create a `docker-compose.yaml` file, _an example:_

```yaml
version: '3'

services:
  budget:
    image: 'ghcr.io/buried-in-code/neptunes-dashboard:latest'
    container_name: 'Neptunes Dashboard'
    environment:
      TZ: 'Pacific/Auckland'
    ports:
      - '25710:25710'
    volumes:
      - './config:/app/config'
      - './data:/app/data'
```

4. Run using: `docker-compose up -d`

## Socials

[![Social - Fosstodon](https://img.shields.io/badge/%40BuriedInCode-teal?label=Fosstodon&logo=mastodon&style=for-the-badge)](https://fosstodon.org/@BuriedInCode)\
[![Social - Matrix](https://img.shields.io/badge/%23The--Dev--Environment-teal?label=Matrix&logo=matrix&style=for-the-badge)](https://matrix.to/#/#The-Dev-Environment:matrix.org)
