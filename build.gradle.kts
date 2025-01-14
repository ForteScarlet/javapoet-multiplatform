plugins {
    idea
    alias(libs.plugins.kotlinMultiplatform) apply false
}

allprojects {
    version = "0.0.1"
    group = "love.forte.codepoet"
    description = "A Kotlin multiplatform API for generating Java source code"
    repositories {
        mavenCentral()
    }
}

idea {
    module {
        isDownloadSources = true
    }
}
