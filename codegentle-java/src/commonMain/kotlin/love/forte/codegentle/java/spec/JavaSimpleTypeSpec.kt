package love.forte.codegentle.java.spec

import love.forte.codegentle.common.spec.NamedSpec
import love.forte.codegentle.java.spec.internal.JavaSimpleTypeSpecImpl

/**
 * A generated `class` or `interface`.
 */
@SubclassOptInRequired(CodeGentleJavaSpecImplementation::class)
public interface JavaSimpleTypeSpec : NamedSpec, JavaTypeSpec {
    override val name: String
}

/**
 * A builder for [JavaSimpleTypeSpec].
 */
public class JavaSimpleTypeSpecBuilder @PublishedApi internal constructor(
    kind: JavaTypeSpec.Kind,
    name: String,
) : JavaTypeSpecBuilder<JavaSimpleTypeSpecBuilder, JavaSimpleTypeSpec>(kind, name) {
    init {
        require(kind == JavaTypeSpec.Kind.CLASS || kind == JavaTypeSpec.Kind.INTERFACE) {
            "Invalid simple type `kind`: $kind"
        }
    }

    override val self: JavaSimpleTypeSpecBuilder
        get() = this


    override fun build(): JavaSimpleTypeSpec {
        return JavaSimpleTypeSpecImpl(
            name = name!!,
            kind = kind,
            javadoc = javadoc.build(),
            annotations = annotationRefs.toList(),
            modifiers = modifierSet.immutable(),
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

/**
 * @param kind allows [JavaTypeSpec.Kind.CLASS], [JavaTypeSpec.Kind.INTERFACE]
 */
public inline fun JavaSimpleTypeSpec(
    kind: JavaTypeSpec.Kind,
    name: String,
    block: JavaSimpleTypeSpecBuilder.() -> Unit = {}
): JavaSimpleTypeSpec {
    return JavaSimpleTypeSpecBuilder(kind, name).also(block).build()
}
