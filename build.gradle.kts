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
	implementation("org.yaml:snakeyaml:1+")
	implementation("io.ktor:ktor-server-netty:1.1+")
	implementation("io.ktor:ktor-gson:1.1+")
	implementation("io.ktor:ktor-freemarker:1.1+")
	implementation("org.jetbrains.exposed:exposed:0.12+")
	implementation("khttp:khttp:0.1+")
	implementation("com.mashape.unirest:unirest-java:1.4.9")
	implementation("org.json:json:20140107")
	implementation("org.apache.logging.log4j:log4j-api:2.11+")
	runtimeOnly("org.apache.logging.log4j:log4j-core:2.11+")
	runtimeOnly("org.apache.logging.log4j:log4j-slf4j18-impl:2.11+")
	runtimeOnly("org.apache.logging.log4j:log4j-jul:2.11+")
	runtimeOnly("org.apache.logging.log4j:log4j-1.2-api:2.11+")
	runtimeOnly("org.xerial:sqlite-jdbc:3.25+")
	testImplementation("org.jetbrains.kotlin:kotlin-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
	mainClassName = "macro.dashboard.neptunes.Server"
}

tasks.withType<Test> {
	useJUnitPlatform()
	testLogging {
		events("passed", "skipped", "failed")
	}
}