package love.forte.codegentle.kotlin.spec

/**
 * The Kotlin getter/setter spec.
 *
 * @author ForteScarlet
 */
public sealed interface KotlinPropertyAccessorSpec : KotlinCallableSpec {
    public enum class Kind { GETTER, SETTER }

    public val kind: Kind

    /**
     * Accessor's parameters are always empty.
     */
    override val parameters: List<KotlinValueParameterSpec>
        get() = emptyList()

    public interface Builder<S : KotlinPropertyAccessorSpec> : KotlinCallableSpec.Builder<S>

    public companion object {
        public fun getterBuilder(): KotlinGetterSpec.Builder {
            TODO()
        }

        public fun setterBuilder(parameterName: String): KotlinGetterSpec.Builder {
            TODO()
        }
    }
}

public var name: String = ""
    set(value) {

        TODO()
    }

/**
 * The Kotlin property's getter.
 */
@SubclassOptInRequired(CodeGentleKotlinSpecImplementation::class)
public interface KotlinGetterSpec : KotlinPropertyAccessorSpec {

    public interface Builder : KotlinPropertyAccessorSpec.Builder<KotlinGetterSpec> {
        // TODO
    }
}

/**
 * The Kotlin property's setter.
 */
@SubclassOptInRequired(CodeGentleKotlinSpecImplementation::class)
public interface KotlinSetterSpec : KotlinPropertyAccessorSpec {

    public val parameterName: String

    public interface Builder : KotlinPropertyAccessorSpec.Builder<KotlinGetterSpec> {
        // TODO
    }
}
