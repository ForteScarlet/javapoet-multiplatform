import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.withType
import org.gradle.process.CommandLineArgumentProvider

inline fun Project.configJavaCompileWithModule(
    moduleName: String? = null,
    compatibility: String = "11",
    crossinline block: JavaCompile.() -> Unit = {}
) {
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = compatibility
        targetCompatibility = compatibility

        if (moduleName != null) {
            configJavaModule(moduleName, sourceSets)
        }

        block()
    }
}

fun JavaCompile.configJavaModule(moduleName: String, sourceSets: SourceSetContainer) {
    // see https://kotlinlang.org/docs/gradle-configure-project.html#configure-with-java-modules-jpms-enabled
    options.compilerArgumentProviders.add(
        CommandLineArgumentProvider {
            // Provide compiled Kotlin classes to javac – needed for Java/Kotlin mixed sources to work
            listOf("--patch-module", "$moduleName=${sourceSets["main"].output.asPath}")
        }
    )
}

@PublishedApi
internal val Project.sourceSets: SourceSetContainer
    get() = extensions.getByName<SourceSetContainer>("sourceSets")
