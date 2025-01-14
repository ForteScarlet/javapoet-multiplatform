import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.withType
import org.gradle.process.CommandLineArgumentProvider

inline fun Project.configJavaCompile(
    compatibility: String = "11",
    crossinline block: JavaCompile.() -> Unit = {}
) {
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = compatibility
        targetCompatibility = compatibility
        block()
    }
}

fun JavaCompile.configJavaModule(moduleName: String, path: String) {
    // see https://kotlinlang.org/docs/gradle-configure-project.html#configure-with-java-modules-jpms-enabled
    options.compilerArgumentProviders.add(
        CommandLineArgumentProvider {
            // Provide compiled Kotlin classes to javac – needed for Java/Kotlin mixed sources to work
            listOf("--patch-module", "$moduleName=$path")
        }
    )
}

// @PublishedApi
// internal val Project.sourceSets: SourceSetContainer
//     get() = extensions.getByName<SourceSetContainer>("sourceSets")
