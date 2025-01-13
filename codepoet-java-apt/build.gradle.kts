import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlinxBinaryCompatibilityValidator)
}

configJavaCompileWithModule("love.forte.codepoet.java.apt")

kotlin {
    explicitApi()

    compilerOptions {
        javaParameters = true
        jvmTarget = JvmTarget.JVM_11
        jvmToolchain(11)

        optIn.add("love.forte.codepoet.java.InternalApi")

        freeCompilerArgs.addAll("-Xjvm-default=all", "-Xjsr305=strict")
    }

    dependencies {
        api(project(":codepoet-java"))
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
