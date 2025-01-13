import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

configJavaCompileWithModule("love.forte.codepoet.java")

kotlin {
    explicitApi()
    jvmToolchain(11)

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    applyDefaultHierarchyTemplate {
        common {
            withJvm()
            group("non-jvm") {
                withNative()
                withWasmWasi()

                group("js-based") {
                    withJs()
                    withWasmJs()
                }
            }
        }
    }


    compilerOptions {
        optIn.add("love.forte.codepoet.java.InternalApi")
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    jvm {
        withJava()

        compilerOptions {
            javaParameters = true
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
            api(project(":codepoet-common"))
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
