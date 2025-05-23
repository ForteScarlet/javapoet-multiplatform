package love.forte.codegentle.java.spec

import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.common.spec.NamedSpec
import love.forte.codegentle.java.spec.internal.JavaSealedTypeSpecImpl

/**
 * A generated `sealed class/interface`.
 */
@SubclassOptInRequired(CodeGentleJavaSpecImplementation::class)
public interface JavaSealedTypeSpec : NamedSpec, JavaTypeSpec {
    override val name: String

    // sealed class, sealed interface
    public val permits: List<TypeName>

    public class JavaSealedTypeSpecBuilder @PublishedApi internal constructor(
        kind: JavaTypeSpec.Kind,
        name: String,
    ) : JavaTypeSpecBuilder<JavaSealedTypeSpecBuilder, JavaSealedTypeSpec>(
        kind = kind,
        name = name
    ) {
        init {
            check(kind == JavaTypeSpec.Kind.SEALED_CLASS || kind == JavaTypeSpec.Kind.SEALED_INTERFACE) {
                "Invalid sealed `kind`: $kind"
            }
        }

        override val self: JavaSealedTypeSpecBuilder
            get() = this

        private val permits = mutableListOf<TypeName>()

        public fun addPermits(permits: Iterable<TypeName>): JavaSealedTypeSpecBuilder = apply {
            this.permits.addAll(permits)
        }

        public fun addPermits(vararg permits: TypeName): JavaSealedTypeSpecBuilder = apply {
            this.permits.addAll(permits)
        }

        public fun addPermit(permit: TypeName): JavaSealedTypeSpecBuilder = apply {
            permits.add(permit)
        }

        override fun build(): JavaSealedTypeSpec {
            return JavaSealedTypeSpecImpl(
                name = name!!,
                kind = kind,
                permits = permits.toList(),
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
}


public inline fun JavaSealedTypeSpec(
    kind: JavaTypeSpec.Kind,
    name: String,
    block: JavaSealedTypeSpec.JavaSealedTypeSpecBuilder.() -> Unit = {},
): JavaSealedTypeSpec {
    return JavaSealedTypeSpec.JavaSealedTypeSpecBuilder(kind, name).also(block).build()
}
