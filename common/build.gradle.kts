plugins {
    `config-java`
}

dependencies {
    api(project(":api"))

    compileOnly(libs.bundles.adventure)

    compileOnly(libs.fusion.core)
}