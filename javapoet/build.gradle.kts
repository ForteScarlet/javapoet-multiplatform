plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlinxBinaryCompatibilityValidator)
}

kotlin {
    explicitApi()

    compilerOptions {
        jvmToolchain(8) // TODO 8, or 11?

        optIn.add("love.forte.javapoet.InternalApi")
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
    }
}
