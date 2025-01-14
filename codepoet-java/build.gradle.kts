import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

configJavaCompileWithModule("love.forte.codepoet.java")
// configJavaCompileWithModule(null, "1.8")

kotlin {
    explicitApi()

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

    jvmToolchain(11)
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
        nodejs {
            testTask {
                useMocha {
                    timeout = "30s"
                }
            }
        }
        binaries.library()
    }

    sourceSets {
        commonMain.dependencies {
            // api(project(":codepoet-common"))
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        jvmTest.dependencies {
            implementation(kotlin("test-junit5"))
        }
    }
}
