import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    kotlin("multiplatform")
}

kotlin {
    explicitApi()

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    applyDefaultHierarchyTemplate {
        common {
            withJvm()
            group("non-jvm") {
                withNative()

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
        nodejs()
        binaries.library()
    }

    sourceSets {
        commonMain {
            dependencies {
                // api(project(":codepoet-common"))
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

    }
}

configJavaCompile {
    configJavaModule("love.forte.codepoet.java", sourceSets["main"].output.asPath)
}
