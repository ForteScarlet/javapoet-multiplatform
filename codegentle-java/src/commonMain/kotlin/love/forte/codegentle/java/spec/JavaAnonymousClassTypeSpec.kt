package love.forte.codegentle.java.spec

import love.forte.codegentle.java.JavaCodeValue
import love.forte.codegentle.java.JavaModifier
import love.forte.codegentle.java.spec.internal.JavaAnonymousClassTypeSpecImpl
import love.forte.codegentle.java.writer.JavaCodeWriter

/**
 * A generated anonymous class.
 * ```java
 * new java.lang.Object() {
 * }
 * ////
 * new HashMap<String, String>(1) {
 * // `anonymousTypeArguments` 👆
 * }
 * ```
 *
 * Also used in enum constants, see [JavaEnumTypeSpec.enumConstants].
 *
 */
public interface JavaAnonymousClassTypeSpec : JavaTypeSpec {
    override val name: String?
        get() = null

    public val anonymousTypeArguments: JavaCodeValue

    override fun emit(codeWriter: JavaCodeWriter, implicitModifiers: Set<JavaModifier>) {
        emit(codeWriter, null, implicitModifiers)
    }

    public fun emit(codeWriter: JavaCodeWriter, enumName: String? = null, implicitModifiers: Set<JavaModifier> = emptySet())
}

public class JavaAnonymousClassTypeSpecBuilder @PublishedApi internal constructor(
    public val anonymousTypeArguments: JavaCodeValue,
) : JavaTypeSpecBuilder<JavaAnonymousClassTypeSpecBuilder, JavaAnonymousClassTypeSpec>(
    kind = JavaTypeSpec.Kind.CLASS,
    name = null
) {
    override val self: JavaAnonymousClassTypeSpecBuilder
        get() = this

    override fun build(): JavaAnonymousClassTypeSpec {
        return JavaAnonymousClassTypeSpecImpl(
            kind = kind,
            anonymousTypeArguments = anonymousTypeArguments,
            javadoc = javadoc.build(),
            annotations = annotationRefs.toList(),
            modifiers = modifiers.copy(),
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


public inline fun JavaAnonymousClassTypeSpec(
    anonymousTypeArguments: JavaCodeValue,
    block: JavaAnonymousClassTypeSpecBuilder.() -> Unit = {},
): JavaAnonymousClassTypeSpec {
    return JavaAnonymousClassTypeSpecBuilder(anonymousTypeArguments).also(block).build()
}
