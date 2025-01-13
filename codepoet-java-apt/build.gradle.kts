plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlinxBinaryCompatibilityValidator)
}

kotlin {
    explicitApi()

    compilerOptions {
        jvmToolchain(8) // TODO 8, or 11?

        optIn.add("love.forte.codepoet.java.InternalApi")
    }

    jvm {
        withJava()

        compilerOptions {
            javaParameters = true
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
