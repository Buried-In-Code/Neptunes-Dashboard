<img src="https://github.com/Macro303/Neptunes-Dashboard/blob/master/logo.png" align="left" width="128" height="128" alt="Neptune's Dashboard Logo"/>

# Neptune's Dashboard
[![Version](https://img.shields.io/github/tag-pre/Macro303/Neptunes-Dashboard.svg?label=version&style=flat-square)](https://github.com/Macro303/Neptunes-Dashboard/releases)
[![Issues](https://img.shields.io/github/issues/Macro303/Neptunes-Dashboard.svg?style=flat-square)](https://github.com/Macro303/Neptunes-Dashboard/issues)
[![Contributors](https://img.shields.io/github/contributors/Macro303/Neptunes-Dashboard.svg?style=flat-square)](https://github.com/Macro303/Neptunes-Dashboard/graphs/contributors)
[![License](https://img.shields.io/github/license/Macro303/Neptunes-Dashboard.svg?style=flat-square)](https://opensource.org/licenses/MIT)

Pulls game information for Neptune's Pride and attempts to display it in a simple Web Interface with REST endpoints.

_Currently only supports **Triton** games_

## Built Using
 - [AdoptOpenJDK: 8](https://adoptopenjdk.net/)
 - [Gradle: 6.3](https://gradle.org/)
 - [kotlin-stdlib-jdk8: 1.3.71](https://kotlinlang.org/)
 - [ktor-server-netty: 1.3.2](https://github.com/ktorio/ktor)
 - [ktor-gson: 1.3.2](https://github.com/ktorio/ktor)
 - [snakeyaml: 1.26](http://www.snakeyaml.org)
 - [exposed-core: 0.22.1](https://github.com/JetBrains/Exposed)
 - [exposed-dao: 0.22.1](https://github.com/JetBrains/Exposed)
 - [exposed-jdbc: 0.22.1](https://github.com/JetBrains/Exposed)
 - [exposed-java-time: 0.22.1](https://github.com/JetBrains/Exposed)
 - [unirest-java: 3.7.00](https://github.com/Kong/unirest-java)
 - [log4j-api: 2.13.1](https://logging.apache.org/log4j/2.x/)
 - [log4j-slf4j-impl: 2.13.1 (Runtime)](https://logging.apache.org/log4j/2.x/)
 - [sqlite-jdbc: 3.30.1 (Runtime)](https://github.com/xerial/sqlite-jdbc)
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