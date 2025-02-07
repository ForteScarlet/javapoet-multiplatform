plugins {
    alias(libs.plugins.kotlinMultiplatform)
    // alias(libs.plugins.kotlinxBinaryCompatibilityValidator)
}

// tasks.withType<JavaCompile> {
//     options.encoding = "UTF-8"
//     sourceCompatibility = "1.8"
//     targetCompatibility = "1.8"
//
//     val moduleName = "love.forte.codepoet.java"
//
//     // options.compilerArgumentProviders.add(
//     //     CommandLineArgumentProvider {
//     //         // Provide compiled Kotlin classes to javac – needed for Java/Kotlin mixed sources to work
//     //         listOf("--patch-module", "$moduleName=${sourceSets["main"].output.asPath}")
//     //     }
//     // )
// }

// apiValidation {
//     nonPublicMarkers.addAll(
//         listOf("love.forte.codepoet.java.InternalApi")
//     )
// }


kotlin {
    explicitApi()
    applyDefaultHierarchyTemplate()

    compilerOptions {
        optIn.add("love.forte.codepoet.java.InternalApi")
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    // @OptIn(ExperimentalKotlinGradlePluginApi::class)
    // applyDefaultHierarchyTemplate {
    //     withCompilations { true }
    //
    //     common {
    //         withJvm()
    //         group("non-jvm") {
    //             withNative()
    //
    //             group("js-based") {
    //                 withJs()
    //                 withWasmJs()
    //             }
    //         }
    //     }
    // }

    jvmToolchain(8)
    jvm {
        // withJava()
        compilerOptions {
            // javaParameters = true
            // freeCompilerArgs.addAll("-Xjvm-default=all", "-Xjsr305=strict")
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

        jvmTest {
            dependencies {
                implementation(kotlin("test-junit5"))
            }
        }
    }
}

// tasks.withType<Test> {
//     useJUnitPlatform()
// }
