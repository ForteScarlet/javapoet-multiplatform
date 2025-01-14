plugins {
    idea
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlinxBinaryCompatibilityValidator)
}

version = "0.0.1"
group = "love.forte.codepoet"
description = "A Kotlin multiplatform API for generating Java source code"

allprojects {
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
