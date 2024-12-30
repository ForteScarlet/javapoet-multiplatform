plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlinxBinaryCompatibilityValidator)
}

kotlin {
    compilerOptions {
        jvmToolchain(8)
    }

    jvm {

    }

    js {
        nodejs()
        binaries.library()
    }

}
