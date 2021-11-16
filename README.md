<img src="./logo.png" align="left" width="128" height="128" alt="Neptune's Dashboard Logo">

# Neptune's Dashboard

![Java](https://img.shields.io/badge/Java-8-green?style=flat-square)
![Kotlin](https://img.shields.io/badge/Kotlin-1.6.0-green?style=flat-square)
![Status](https://img.shields.io/badge/Status-Beta-yellowgreen?style=flat-square)

[![Github - Version](https://img.shields.io/github/v/tag/Buried-In-Code/Neptunes-Dashboard?logo=Github&label=Version&style=flat-square)](https://github.com/Buried-In-Code/Neptunes-Dashboard/tags)
[![Github - License](https://img.shields.io/github/license/Buried-In-Code/Neptunes-Dashboard?logo=Github&label=License&style=flat-square)](https://opensource.org/licenses/GPL-3.0)
[![Github - Contributors](https://img.shields.io/github/contributors/Buried-In-Code/Neptunes-Dashboard?logo=Github&label=Contributors&style=flat-square)](https://github.com/Buried-In-Code/Neptunes-Dashboard/graphs/contributors)

[![Github Action - Code Analysis](https://img.shields.io/github/workflow/status/Buried-In-Code/Neptunes-Dashboard/Code-Analysis?logo=Github-Actions&label=Code-Analysis&style=flat-square)](https://github.com/Buried-In-Code/Neptunes-Dashboard/actions/workflows/code-analysis.yaml)

Pulls game information for Neptune's Pride and attempts to display it in a simple Web Interface with REST endpoints.

_Currently only supports **Triton** games_

## Built Using
 - [AdoptOpenJDK: 8](https://adoptium.net/)
 - [Gradle: 7.2](https://gradle.org/)
 - [kotlin-stdlib-jdk8: 1.6.0](https://kotlinlang.org/)
 - [ktor-server-netty: 1.6.5](https://github.com/ktorio/ktor)
 - [ktor-gson: 1.6.5](https://github.com/ktorio/ktor)
 - [snakeyaml: 1.29](http://www.snakeyaml.org)
 - [exposed-core: 0.36.2](https://github.com/JetBrains/Exposed)
 - [exposed-dao: 0.36.2](https://github.com/JetBrains/Exposed)
 - [exposed-jdbc: 0.36.2](https://github.com/JetBrains/Exposed)
 - [exposed-java-time: 0.36.2](https://github.com/JetBrains/Exposed)
 - [unirest-java: 3.13.3](https://github.com/Kong/unirest-java)
 - [log4j-api: 2.14.1](https://logging.apache.org/log4j/2.x/)
 - [log4j-slf4j-impl: 2.14.1 (Runtime)](https://logging.apache.org/log4j/2.x/)
 - [sqlite-jdbc: 3.36.0.3 (Runtime)](https://github.com/xerial/sqlite-jdbc)
 - [bootstrap: 4.4.1 (CSS Framework)](https://getbootstrap.com/)
 
## Execution
 - You can change basic proxy settings and server settings in the generated **config.yaml**
 - The default address is [localhost:6790](http://localhost:6790)
 - To create a game you need to make a **POST** request to `/api/games/{Game ID}?code={Game Code}`
   - The `Game ID` can be found in the Neptune's Pride URL
   - The `Game Code` is generated in the API options menu of the game
 - To update/refresh the game you need to make a **PUT** request to `/api/games/{Game ID}` or hit the **Update** button on the Website.
 - To update a player's details you need to make a **PUT** request to `/api/games/{Game ID}/players/{Player Alias}`
    - The `Game ID` can be found in the Neptune's Pride URL
    - The `Player Alias` is the name of the player you wish to update in Neptune's Pride
    - Requires the following body:
        ```json
      {
        "name": "<Player Name>",
        "team": "<Team Name>"
      }
        ```  
 
### Running from source
```bash
$ gradlew clean run
```

### Running from Jar
```bash
$ java -jar Neptunes-Dashboard.jar
```

## Socials

[![Social - Discord](https://img.shields.io/badge/Discord-The--DEV--Environment-7289DA?logo=Discord&style=flat-square)](https://discord.gg/nqGMeGg)
[![Social - Matrix](https://img.shields.io/badge/Matrix-The--DEV--Environment-informational?logo=Matrix&style=flat-square)](https://matrix.to/#/#the-dev-environment:matrix.org)

[![Social - Twitter](https://img.shields.io/badge/Twitter-@BuriedInCode-informational?logo=Twitter&style=flat-square)](https://twitter.com/BuriedInCode)
[![Social - Mastodon](https://img.shields.io/badge/Mastodon-@BuriedInCode@fosstodon.org-informational?logo=Mastodon&style=flat-square)](https://fosstodon.org/@BuriedInCode)