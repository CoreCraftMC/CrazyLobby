plugins {
    `config-paper`
}

project.group = "${rootProject.group}.paper"

dependencies {
    implementation(project(":common"))

    implementation(libs.fusion.paper)
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    runServer {
        jvmArgs("-Dnet.kyori.ansi.colorLevel=truecolor")

        defaultCharacterEncoding = Charsets.UTF_8.name()

        minecraftVersion(libs.versions.minecraft.get())
    }
}