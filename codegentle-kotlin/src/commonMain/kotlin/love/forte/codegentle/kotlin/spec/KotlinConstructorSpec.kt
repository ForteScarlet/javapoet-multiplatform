package love.forte.codegentle.kotlin.spec

import love.forte.codegentle.common.code.CodeArgumentPart
import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.code.CodeValueSingleFormatBuilderDsl

/**
 *
 *
 * @author ForteScarlet
 */
public interface KotlinConstructorSpec : KotlinCallableSpec {
    // 没有名字，不能挂起，还有什么区别？

    public val constructorDelegation: ConstructorDelegation?

    // 不是 primary constructor 的话可能不是 empty 的，
    // TODO 但是如果是用作 primary constructor 则必须是 empty: 这个在 TypeSpec 那儿校验。
    override val code: CodeValue

    public companion object {
        public fun builder(): Builder {
            TODO()
        }
    }

    public interface Builder : KotlinCallableSpec.Builder<KotlinConstructorSpec> {

        // TODO 其他属性...

        public fun constructorDelegation(delegation: ConstructorDelegation?): Builder

        override fun build(): KotlinConstructorSpec
    }
}

public inline fun KotlinConstructorSpec.Builder.constructorDelegation(
    kind: ConstructorDelegation.Kind,
    block: ConstructorDelegation.Builder.() -> Unit = {}
): KotlinConstructorSpec.Builder =
    constructorDelegation(ConstructorDelegation(kind, block))

public inline fun KotlinConstructorSpec.Builder.thisConstructorDelegation(
    block: ConstructorDelegation.Builder.() -> Unit = {}
): KotlinConstructorSpec.Builder =
    constructorDelegation(ConstructorDelegation.Kind.THIS, block)

public inline fun KotlinConstructorSpec.Builder.superConstructorDelegation(
    block: ConstructorDelegation.Builder.() -> Unit = {}
): KotlinConstructorSpec.Builder =
    constructorDelegation(ConstructorDelegation.Kind.SUPER, block)

public interface ConstructorDelegation {
    public enum class Kind { THIS, SUPER }

    public val kind: Kind

    public val arguments: List<CodeValue>

    public companion object {
        public fun builder(kind: Kind): Builder {
            TODO()
        }
    }

    public interface Builder {

        public fun addArgument(argument: CodeValue): Builder

        public fun addArgument(format: String, vararg arguments: CodeArgumentPart): Builder

        public fun addArguments(vararg arguments: CodeValue): Builder

        public fun addArguments(arguments: Iterable<CodeValue>): Builder


        public fun build(): ConstructorDelegation
    }

}

public inline fun ConstructorDelegation(
    kind: ConstructorDelegation.Kind,
    block: ConstructorDelegation.Builder.() -> Unit = {}
): ConstructorDelegation = ConstructorDelegation.builder(kind).apply(block).build()

public inline fun ConstructorDelegation.Builder.addArgument(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): ConstructorDelegation.Builder =
    addArgument(CodeValue(format, block))
