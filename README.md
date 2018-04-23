# Neptunes Pride _0.3_
Pulls from the Neptune Pride's API to gather simple game information and tries to display it in a simple UI

## Dependencies
- JDK 1.8
- [kotlin-stdlib-jdk: 1.2.40](https://kotlinlang.org)
- [tornadofx: RELEASE {1.7.15}](https://github.com/edvin/tornadofx)
- [kotson: RELEASE {2.5.0}](https://github.com/SalomonBrys/Kotson)
- [console: RELEASE {1.0}](https://github.com/Macro303/Console)

## Install
1. Clone **Console**
2. `mvn clean install` on the **Console** project
3. Clone **Neptunes Pride**
4. `mvn clean package` on the **Neptunes Pride** project
5. Move the `neptunes-pride-{version}-jfx.jar` and `lib` folder to desired install location
6. Create `bin` folder
7. Run `neptunes-pride-{version}-jfx.jar`

## Notes
Console is not available from maven repo. Cloning from github is required.
