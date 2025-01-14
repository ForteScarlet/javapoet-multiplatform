import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

configJavaCompileWithModule("love.forte.codepoet.common")

kotlin {
    explicitApi()
    jvmToolchain(11)

    jvm {
        withJava()

        compilerOptions {
            javaParameters = true
            jvmTarget = JvmTarget.JVM_11
            freeCompilerArgs.addAll("-Xjvm-default=all", "-Xjsr305=strict")
        }

        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    js {
        nodejs()
        binaries.library()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(kotlin("reflect"))
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        jvmTest.dependencies {
            implementation(kotlin("test-junit5"))
        }
    }
}
