import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    // alias(libs.plugins.kotlinxBinaryCompatibilityValidator)
}

// apiValidation {
//     nonPublicMarkers.addAll(
//         listOf("love.forte.codepoet.java.InternalApi")
//     )
// }


kotlin {
    explicitApi()

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        optIn.addAll(
            "love.forte.codegentle.common.codepoint.InternalCodePointApi",
            "love.forte.codegentle.common.InternalCommonCodeGentleApi",
            "love.forte.codegentle.common.naming.CodeGentleNamingImplementation",
            "love.forte.codegentle.common.ref.CodeGentleRefImplementation",
            "love.forte.codegentle.common.writer.CodeGentleCodeWriterImplementation",
            // java
            "love.forte.codegentle.java.InternalJavaCodeGentleApi",
            "love.forte.codegentle.java.spec.CodeGentleJavaSpecImplementation"
        )
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    applyDefaultHierarchyTemplate {
        common {
            withJvm()
            group("nonJvm") {
                withNative()

                group("jsBased") {
                    withJs()
                    withWasmJs()
                }
            }
        }
    }

    jvmToolchain(11)
    jvm {
        withJava()
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
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
                api(project(":codegentle-common"))
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        jvmTest {
            dependencies {
                implementation(kotlin("test-junit5"))
            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    sourceCompatibility = "11"
    targetCompatibility = "11"

    val moduleName = "love.forte.codegentle.java"

    options.compilerArgumentProviders.add(
        CommandLineArgumentProvider {
            // Provide compiled Kotlin classes to javac – needed for Java/Kotlin mixed sources to work
            listOf("--patch-module", "$moduleName=${sourceSets["main"].output.asPath}")
        }
    )
}
