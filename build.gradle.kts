plugins {
    idea
    alias(libs.plugins.kotlinxBinaryCompatibilityValidator)
}

version = "0.0.1"
group = "love.forte.codepoet"
description = "A Kotlin multiplatform API for generating Java source code"

repositories {
    mavenCentral()
}

subprojects {
    version = "0.0.1"
    group = "love.forte.codepoet"
    description = "A Kotlin multiplatform API for generating Java source code"
    repositories {
        mavenCentral()
    }
}

apiValidation {
    nonPublicMarkers.addAll(
        listOf(
            "love.forte.codepoet.java.InternalApi",
        ),
    )
}

idea {
    module {
        isDownloadSources = true
    }
}
