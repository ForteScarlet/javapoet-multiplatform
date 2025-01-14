import jdk.tools.jlink.resources.plugins
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
}

kotlin {
    explicitApi()

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


configJavaCompile {
    configJavaModule("love.forte.codepoet.common", sourceSets["main"].output.asPath)
}
