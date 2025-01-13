import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlinxBinaryCompatibilityValidator)
}

configJavaCompileWithModule("love.forte.codepoet.java")

kotlin {
    explicitApi()

    compilerOptions {
        jvmToolchain(11)

        optIn.add("love.forte.codepoet.java.InternalApi")

        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

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
        nodejs {
            testTask {
                useMocha()
            }
        }
        binaries.library()
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":codepoet-common"))
            // implementation(kotlin("reflect"))
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
