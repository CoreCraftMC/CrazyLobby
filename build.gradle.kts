plugins {
    alias(libs.plugins.paperweight)
    alias(libs.plugins.runPaper)
    alias(libs.plugins.shadow)

    `java-library`
}

rootProject.description = "The lobby plugin for play.corecraft.me!"
rootProject.version = "1.0.0"
rootProject.group = "me.corecraft.paper"

repositories {
    maven("https://repo.papermc.io/repository/maven-public")

    maven("https://repo.codemc.io/repository/maven-public")

    maven("https://repo.crazycrew.us/libraries")
    maven("https://repo.crazycrew.us/releases")

    maven("https://jitpack.io")

    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper.get())

    compileOnly(libs.bundles.cloud.paper)
    compileOnly(libs.fusion.paper)

    implementation(project(":api"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))

    withSourcesJar()
    withJavadocJar()
}

tasks {
    runServer {
        jvmArgs("-Dnet.kyori.ansi.colorLevel=truecolor")

        defaultCharacterEncoding = Charsets.UTF_8.name()

        minecraftVersion(libs.versions.minecraft.get())
    }

    processResources {
        inputs.properties(
            "name" to rootProject.name,
            "version" to rootProject.version,
            "description" to rootProject.description,
            "minecraft" to libs.versions.minecraft.get(),
            "group" to rootProject.group
        )

        with(copySpec {
            from("src/main/resources/paper-plugin.yml") {
                expand(inputs.properties)
            }
        })
    }

    shadowJar {
        archiveClassifier.set("")
        archiveVersion.set("")

        exclude("META-INF/**")
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()

        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }
}