import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

plugins {
    alias(libs.plugins.kotlinJvm) apply false
}

val ktjId = libs.plugins.kotlinJvm.get().pluginId

subprojects {
    apply(plugin = ktjId)

    extensions.configure<KotlinJvmProjectExtension> {
        jvmToolchain(11)
        compilerOptions {
            javaParameters = true
            freeCompilerArgs.addAll("-Xjvm-default=all", "-Xjsr305=strict")
        }
    }
}

