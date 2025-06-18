plugins {
    `config-java`
}

dependencies {
    compileOnly(libs.bundles.adventure)

    compileOnly(libs.fusion.core)

    api(project(":api"))
}