<img src="https://github.com/Macro303/Neptunes-Dashboard/blob/master/logo.png" align="left" width="128" height="128" alt="Neptune's Dashboard Logo"/>

# Neptune's Dashboard
[![Version](https://img.shields.io/github/tag-pre/Macro303/Neptunes-Dashboard.svg?label=version)](https://github.com/Macro303/Neptunes-Dashboard/releases)
[![Issues](https://img.shields.io/github/issues/Macro303/Neptunes-Dashboard.svg?label=issues)](https://github.com/Macro303/Neptunes-Dashboard/issues)
[![Contributors](https://img.shields.io/github/contributors/Macro303/Neptunes-Dashboard.svg?label=contributors)](https://github.com/Macro303/Neptunes-Dashboard/graphs/contributors)
[![License](https://img.shields.io/github/license/Macro303/Neptunes-Dashboard.svg?=label=license)](https://raw.githubusercontent.com/Macro303/Neptunes-Dashboard/master/LICENSE)

Pulls game information for Neptune's Pride and attempts to display it in a simple web interface and REST endpoints.

_Currently only supports **Proteus**_

## Built Using
 - [AdoptOpenJDK: 8](https://adoptopenjdk.net/)
 - [Gradle: 6.0.1](https://gradle.org/)
 - [kotlin-stdlib-jdk8: 1.3.61](https://kotlinlang.org/)
 - [ktor-server-netty: 1.3.1](https://github.com/ktorio/ktor)
 - [ktor-gson: 1.3.1](https://github.com/ktorio/ktor)
 - [snakeyaml: 1.25](http://www.snakeyaml.org)
 - [exposed-core: 0.21.1](https://github.com/JetBrains/Exposed)
 - [exposed-dao: 0.21.1](https://github.com/JetBrains/Exposed)
 - [exposed-jdbc: 0.21.1](https://github.com/JetBrains/Exposed)
 - [exposed-java-time: 0.21.1](https://github.com/JetBrains/Exposed)
 - [unirest-java: 3.5.00](https://github.com/Kong/unirest-java)
 - [log4j-api: 2.13.0](https://logging.apache.org/log4j/2.x/)
 - [log4j-slf4j-impl: 2.13.0 (Runtime)](https://logging.apache.org/log4j/2.x/)
 - [sqlite-jdbc: 3.30.1 (Runtime)](https://github.com/xerial/sqlite-jdbc)
 - [bootstrap: 4.4.1 (CSS Framework)](https://getbootstrap.com/)
 
## Running
**To run from source:**
```bash
$ gradle clean run
```
_You can change basic proxy settings and server settings in the generated `config.yaml`_