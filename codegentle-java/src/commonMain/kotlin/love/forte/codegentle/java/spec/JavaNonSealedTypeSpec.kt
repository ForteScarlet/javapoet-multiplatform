package love.forte.codegentle.java.spec

import love.forte.codegentle.common.spec.NamedSpec
import love.forte.codegentle.java.spec.internal.JavaNonSealedTypeSpecImpl

/**
 * A generated `non-sealed class/interface`.
 *
 * ```java
 * public non-sealed class NonSealedClass extends SealedType {
 * }
 * ```
 *
 * ```java
 * public non-sealed interface NonSealedInterface extends SealedType {
 * }
 * ```
 */
public interface JavaNonSealedTypeSpec : NamedSpec, JavaTypeSpec {
    override val name: String
}

public class JavaNonSealedTypeSpecBuilder @PublishedApi internal constructor(
    kind: JavaTypeSpec.Kind,
    name: String,
) : JavaTypeSpecBuilder<JavaNonSealedTypeSpecBuilder, JavaNonSealedTypeSpec>(
    kind = kind,
    name = name
) {
    init {
        check(kind == JavaTypeSpec.Kind.NON_SEALED_CLASS || kind == JavaTypeSpec.Kind.NON_SEALED_INTERFACE) {
            "Invalid non-sealed `kind`: $kind"
        }
    }

    override val self: JavaNonSealedTypeSpecBuilder
        get() = this

    override fun build(): JavaNonSealedTypeSpec {
        return JavaNonSealedTypeSpecImpl(
            name = name!!,
            kind = kind,
            javadoc = javadoc.build(),
            annotations = annotationRefs.toList(),
            modifiers = LinkedHashSet(modifiers),
            typeVariables = typeVariableRefs.toList(),
            superclass = superclass,
            superinterfaces = superinterfaces.toList(),
            fields = fields.toList(),
            staticBlock = staticBlock.build(),
            initializerBlock = initializerBlock.build(),
            methods = methods,
            subtypes = subtypes.toList(),
        )
    }
}


public inline fun JavaNonSealedTypeSpec(
    kind: JavaTypeSpec.Kind,
    name: String,
    block: JavaNonSealedTypeSpecBuilder.() -> Unit = {},
): JavaNonSealedTypeSpec {
    return JavaNonSealedTypeSpecBuilder(kind, name).also(block).build()
}
