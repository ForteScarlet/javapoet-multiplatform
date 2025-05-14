plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    api(project(":codegentle-java"))
}

kotlin {
    jvmToolchain(11)
    compilerOptions {
        javaParameters = true
        freeCompilerArgs.addAll(
            "-Xjvm-default=all",
            "-Xjsr305=strict",
        )
    }
}
