import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktorVersion = "1.1.3"
val loggerVersion = "2.11.2"

plugins {
	kotlin("jvm") version "1.3.21"
	application
	id("com.github.ben-manes.versions") version "0.21.0"
}

repositories {
	mavenCentral()
	mavenLocal()
	jcenter()
}

dependencies {
	implementation(kotlin("stdlib-jdk8"))
	implementation("org.yaml:snakeyaml:1.24")
	implementation("io.ktor:ktor-server-netty:$ktorVersion")
	implementation("io.ktor:ktor-gson:$ktorVersion")
	implementation("io.ktor:ktor-freemarker:$ktorVersion")
	implementation("org.jetbrains.exposed:exposed:0.13.5")
	implementation("khttp:khttp:0.1.0")
	implementation("com.mashape.unirest:unirest-java:1.4.9")
	implementation("org.apache.logging.log4j:log4j-api:$loggerVersion")
	runtimeOnly("org.apache.logging.log4j:log4j-core:$loggerVersion")
	runtimeOnly("org.apache.logging.log4j:log4j-slf4j18-impl:$loggerVersion")
	runtimeOnly("org.apache.logging.log4j:log4j-jul:$loggerVersion")
	runtimeOnly("org.apache.logging.log4j:log4j-1.2-api:$loggerVersion")
	runtimeOnly("org.xerial:sqlite-jdbc:3.27.2.1")
}

application {
	mainClassName = "macro.dashboard.neptunes.Server"
}

java {
	sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile>().all {
	kotlinOptions.jvmTarget = "1.8"
}