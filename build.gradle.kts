plugins {
  alias(libs.plugins.jte)
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlinx.serialization)
  alias(libs.plugins.shadow)
  alias(libs.plugins.spotless)
  alias(libs.plugins.versions)
  application
}

group = "github.buriedincode"

version = "4.1.0"

println("Kotlin v${KotlinVersion.CURRENT}")

println("Java v${System.getProperty("java.version")}")

println("Arch: ${System.getProperty("os.arch")}")

repositories {
  mavenLocal()
  mavenCentral()
}

dependencies {
  implementation(libs.bundles.exposed)
  implementation(libs.bundles.hoplite)
  implementation(libs.bundles.jackson)
  implementation(libs.bundles.javalin)
  implementation(libs.bundles.jte)
  implementation(libs.kotlin.logging)
  implementation(libs.kotlinx.datetime)
  implementation(libs.kotlinx.serialization)

  runtimeOnly(libs.log4j2.slf4j2)
  runtimeOnly(libs.sqlite.jdbc)
}

kotlin { jvmToolchain(21) }

java { toolchain { languageVersion.set(JavaLanguageVersion.of(21)) } }

application {
  mainClass.set("github.buriedincode.AppKt")
  applicationName = "Neptunes-Dashboard"
}

jte {
  precompile()
  kotlinCompileArgs.set(arrayOf("-jvm-target", "21"))
}

tasks.jar {
  dependsOn(tasks.precompileJte)
  from(
    fileTree("jte-classes") {
      include("**/*.class")
      include("**/*.bin") // Only required if you use binary templates
    }
  )
  manifest.attributes["Main-Class"] = "github.buriedincode.AppKt"
}

tasks.shadowJar {
  dependsOn(tasks.precompileJte)
  from(
    fileTree("jte-classes") {
      include("**/*.class")
      include("**/*.bin") // Only required if you use binary templates
    }
  )
  manifest.attributes["Main-Class"] = "github.buriedincode.AppKt"
  mergeServiceFiles()
}

spotless {
  kotlin {
    ktfmt().kotlinlangStyle().configure {
      it.setMaxWidth(120)
      it.setBlockIndent(2)
      it.setContinuationIndent(2)
      it.setRemoveUnusedImports(true)
      it.setManageTrailingCommas(true)
    }
  }
  kotlinGradle {
    ktfmt().kotlinlangStyle().configure {
      it.setMaxWidth(120)
      it.setBlockIndent(2)
      it.setContinuationIndent(2)
      it.setRemoveUnusedImports(true)
      it.setManageTrailingCommas(true)
    }
  }
}

fun isNonStable(version: String): Boolean {
  val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
  val regex = "^[0-9,.v-]+(-r)?$".toRegex()
  val isStable = stableKeyword || regex.matches(version)
  return isStable.not()
}

tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
  resolutionStrategy {
    componentSelection {
      all {
        if (isNonStable(candidate.version) && !isNonStable(currentVersion)) {
          reject("Release candidate")
        }
      }
    }
  }
}
