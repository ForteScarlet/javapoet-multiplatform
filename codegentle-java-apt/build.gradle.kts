plugins {
    alias(libs.plugins.kotlin.jvm)
}

configJavaCompileWithModule("love.forte.codepoet.java.apt")

kotlin {
    explicitApi()
    jvmToolchain(11)

    compilerOptions {
        javaParameters = true
        optIn.add("love.forte.codepoet.java.InternalApi")

        freeCompilerArgs.addAll("-Xjvm-default=all", "-Xjsr305=strict")
    }

    dependencies {
        api(project(":codepoet-java"))

        testImplementation(kotlin("test-junit5"))
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
