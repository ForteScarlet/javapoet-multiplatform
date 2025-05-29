package love.forte.codegentle.java.spec

import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.common.spec.NamedSpec
import love.forte.codegentle.java.spec.internal.JavaRecordTypeSpecImpl

/**
 * A generated `record` type.
 *
 * ```java
 * public record RecordClass(int value) {
 * }
 * ```
 */
@SubclassOptInRequired(CodeGentleJavaSpecImplementation::class)
public interface JavaRecordTypeSpec : NamedSpec, JavaTypeSpec {
    override val name: String

    override val superclass: TypeName?
        get() = null

    public val mainConstructorParameters: List<JavaParameterSpec>

    /*
     * Init constructor:
     *
     * ```java
     * public record Student(String name, int age) {
     *   // initializerBlock
     *   public Student {
     *     require(age > 0, "...")
     *   }
     *
     *   static {  }
     *
     *   // Other constructor
     *   public Student(String name) {
     *     this(name, 24)
     *   }
     * }
     * ```
     */

}

public class JavaRecordTypeSpecBuilder @PublishedApi internal constructor(
    name: String,
) : JavaTypeSpecBuilder<JavaRecordTypeSpecBuilder, JavaRecordTypeSpec>(
    kind = JavaTypeSpec.Kind.RECORD,
    name = name
) {
    override val self: JavaRecordTypeSpecBuilder
        get() = this

    private var mainConstructorParameters = mutableListOf<JavaParameterSpec>()

    public fun addMainConstructorParameter(mainConstructorParameter: JavaParameterSpec): JavaRecordTypeSpecBuilder =
        apply {
            mainConstructorParameters.add(mainConstructorParameter)
        }

    public fun addMainConstructorParameters(vararg mainConstructorParams: JavaParameterSpec): JavaRecordTypeSpecBuilder =
        apply {
            mainConstructorParameters.addAll(mainConstructorParams)
        }

    public fun addMainConstructorParameters(mainConstructorParams: Iterable<JavaParameterSpec>): JavaRecordTypeSpecBuilder =
        apply {
            mainConstructorParameters.addAll(mainConstructorParams)
        }

    override fun build(): JavaRecordTypeSpec {
        return JavaRecordTypeSpecImpl(
            name = name!!,
            kind = kind,
            mainConstructorParameters = mainConstructorParameters.toList(),
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

public inline fun JavaRecordTypeSpec(
    name: String,
    block: JavaRecordTypeSpecBuilder.() -> Unit = {},
): JavaRecordTypeSpec {
    return JavaRecordTypeSpecBuilder(name).also(block).build()
}

public inline fun JavaRecordTypeSpecBuilder.addMainConstructorParameter(
    type: TypeRef<*>,
    name: String,
    block: JavaParameterSpecBuilder.() -> Unit = {}
) {
    addMainConstructorParameter(
        JavaParameterSpec(type, name, block)
    )
}
