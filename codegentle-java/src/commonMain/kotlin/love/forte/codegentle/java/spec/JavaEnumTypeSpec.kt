package love.forte.codegentle.java.spec

import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.common.spec.NamedSpec
import love.forte.codegentle.java.spec.internal.JavaEnumTypeSpecImpl

/**
 * A generated `enum` type.
 *
 * ```java
 * public enum EnumType {
 * }
 * ```
 *
 */
@SubclassOptInRequired(CodeGentleJavaSpecImplementation::class)
public interface JavaEnumTypeSpec : NamedSpec, JavaTypeSpec {
    override val name: String

    override val superclass: TypeName?
        get() = null

    public val enumConstants: Map<String, JavaTypeSpec>

}

public class JavaEnumTypeSpecBuilder @PublishedApi internal constructor(
    name: String?,
) : JavaTypeSpecBuilder<JavaEnumTypeSpecBuilder, JavaEnumTypeSpec>(
    kind = JavaTypeSpec.Kind.ENUM,
    name = name
) {
    override val self: JavaEnumTypeSpecBuilder
        get() = this

    // TODO superclass must be null

    private val enumConstants = linkedMapOf<String, JavaAnonymousClassTypeSpec>()

    public fun addEnumConstant(name: String, type: JavaAnonymousClassTypeSpec): JavaEnumTypeSpecBuilder = apply {
        enumConstants[name] = type
    }

    override fun build(): JavaEnumTypeSpec {
        return JavaEnumTypeSpecImpl(
            name = name!!,
            kind = kind,
            enumConstants = enumConstants.toMap(linkedMapOf()),
            javadoc = javadoc.build(),
            annotations = annotationRefs.toList(),
            modifiers = modifierSet.copy(),
            typeVariables = typeVariableRefs.toList(),
            superinterfaces = superinterfaces.toList(),
            fields = fields.toList(),
            staticBlock = staticBlock.build(),
            initializerBlock = initializerBlock.build(),
            methods = methods,
            subtypes = subtypes.toList(),
        )
    }
}

public inline fun JavaEnumTypeSpec(
    name: String?,
    block: JavaEnumTypeSpecBuilder.() -> Unit = {},
): JavaEnumTypeSpec {
    return JavaEnumTypeSpecBuilder(name).also(block).build()
}


public inline fun JavaEnumTypeSpecBuilder.addEnumConstant(
    name: String,
    anonymousTypeArguments: CodeValue,
    block: JavaAnonymousClassTypeSpecBuilder.() -> Unit = {}
) {
    addEnumConstant(
        name,
        JavaAnonymousClassTypeSpec(anonymousTypeArguments, block)
    )
}
