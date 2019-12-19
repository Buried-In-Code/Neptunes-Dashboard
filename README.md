<img src="https://github.com/Macro303/Neptunes-Dashboard/blob/master/logo.png" align="left" width="128" height="128" alt="Neptune's Dashboard Logo"/>

# Neptune's Dashboard
[![Version](https://img.shields.io/github/tag-pre/Macro303/Neptunes-Dashboard.svg?label=version)](https://github.com/Macro303/Neptunes-Dashboard/releases)
[![Issues](https://img.shields.io/github/issues/Macro303/Neptunes-Dashboard.svg?label=issues)](https://github.com/Macro303/Neptunes-Dashboard/issues)
[![Contributors](https://img.shields.io/github/contributors/Macro303/Neptunes-Dashboard.svg?label=contributors)](https://github.com/Macro303/Neptunes-Dashboard/graphs/contributors)
[![License](https://img.shields.io/github/license/Macro303/Neptunes-Dashboard.svg?=label=license)](https://raw.githubusercontent.com/Macro303/Neptunes-Dashboard/master/LICENSE)

Pulls game information for Neptune's Pride and attempts to display it in a simple web interface and REST endpoints.

_Currently only supports **Proteus**_

## Built Using
 - JDK: 11
 - [Gradle: 6.0.1](https://gradle.org/)
 - [kotlin-stdlib-jdk8: 1.3.61](https://kotlinlang.org/)
 - [snakeyaml: 1.25](https://bitbucket.org/asomov/snakeyaml)
 - [unirest-java: 3.3.00](https://github.com/Kong/unirest-java)
 - [ktor-server-netty: 1.3.0-rc](https://ktor.io/)
 - [ktor-gson: 1.3.0-rc](https://ktor.io/)
 - [ktor-freemarker: 1.3.0-rc](https://ktor.io/)
 - [exposed-core: 0.18.1](https://github.com/JetBrains/Exposed)
 - [exposed-dao: 0.18.1](https://github.com/JetBrains/Exposed)
 - [exposed-jdbc: 0.18.1](https://github.com/JetBrains/Exposed)
 - [exposed-java-time: 0.18.1](https://github.com/JetBrains/Exposed)
 - [log4j-api: 2.13.0](https://logging.apache.org/log4j/2.x/)
 - [log4j-slf4j-impl: 2.13.0 (Runtime)](https://logging.apache.org/log4j/2.x/)
 - [sqlite-jdbc: 3.28.0 (Runtime)](https://github.com/xerial/sqlite-jdbc)
 
## Running
**To run from source:**
```bash
$ gradle clean run
```
_You can change basic proxy settings, server settings and the selected game in the generated `config.yaml`_