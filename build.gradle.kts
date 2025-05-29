plugins {
    // Apply Kotlin plugins to the root project with 'apply false'
    // This makes the plugins available to subprojects without applying them to the root project
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.ksp) apply false
}

allprojects {
    version = "0.0.1"
    group = "love.forte.codegentle"
    description = "A Kotlin multiplatform API for generating Java source code"
    repositories {
        mavenCentral()
    }
}
