plugins {
    `java-library`
}

project.description = "The api of the lobby plugin for play.corecraft.me!"
project.group = "${rootProject.group}.api"

repositories {
    maven("https://repo.codemc.io/repository/maven-public")

    maven("https://repo.crazycrew.us/libraries")
    maven("https://repo.crazycrew.us/releases")

    maven("https://jitpack.io")

    mavenCentral()
}

dependencies {
    compileOnly(libs.bundles.adventure)

    compileOnly(libs.fusion.core)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))

    withSourcesJar()
    withJavadocJar()
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }
}