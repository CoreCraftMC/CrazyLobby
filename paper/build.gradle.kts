plugins {
    `config-paper`
}

project.group = "${rootProject.group}.paper"

dependencies {
    implementation(project(":common"))

    implementation(libs.fusion.paper)
}

tasks {
    shadowJar {
        archiveBaseName.set("${rootProject.name}-${rootProject.version}")

        copy {
            from(project.layout.buildDirectory.dir("libs"))
            into(rootProject.layout.buildDirectory.dir("libs"))
        }
    }

    runServer {
        jvmArgs("-Dnet.kyori.ansi.colorLevel=truecolor")

        defaultCharacterEncoding = Charsets.UTF_8.name()

        minecraftVersion(libs.versions.minecraft.get())
    }
}