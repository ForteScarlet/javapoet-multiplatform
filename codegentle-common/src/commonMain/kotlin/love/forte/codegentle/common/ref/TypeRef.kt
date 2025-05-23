package love.forte.codegentle.common.ref

import love.forte.codegentle.common.BuilderDsl
import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.common.ref.internal.TypeRefImpl

/**
 * A reference to a [love.forte.codegentle.common.naming.TypeName].
 *
 * @author ForteScarlet
 */
@SubclassOptInRequired(CodeGentleRefImplementation::class)
public interface TypeRef<out T : TypeName> {
    public val typeName: T
    public val status: TypeNameRefStatus
}

public typealias TypeRefBuilderDsl<T, S, B> = TypeRefBuilder<T, S, B>.() -> Unit

/**
 * Create a [TypeRef] with [T] status [S].
 *
 * ```Kotlin
 * // create a java ref, same as `typeName.javaRef`
 * typeName.ref(JavaRefStatus) { build... }
 * ```
 *
 * @see TypeRef
 */
public inline fun <T : TypeName, S : TypeNameRefStatus, B : TypeNameRefStatusBuilder<S>> T.ref(
    statusBuilderFactory: TypeNameRefStatusBuilderFactory<S, B>,
    block: TypeRefBuilderDsl<T, S, B> = {}
): TypeRef<T> = TypeRefBuilder(this, statusBuilderFactory).also(block).build()

/**
 * Builder for [TypeRef].
 */
public class TypeRefBuilder<T : TypeName, S : TypeNameRefStatus, B : TypeNameRefStatusBuilder<S>> @PublishedApi internal constructor(
    public val typeName: T, builderFactory: TypeNameRefStatusBuilderFactory<S, B>
) : BuilderDsl {
    // AnnotationRefCollectable<TypeRefBuilder<T, S, B>>,
    public val status: B = builderFactory.createBuilder()

    public fun build(): TypeRef<T> {
        return TypeRefImpl(
            typeName = typeName, status = status.build()
        )
    }
}

/**
 * ```Kotlin
 * val ref = name.ref(statusFactory) {
 *     status {
 *         // ...
 *     }
 * }
 */
public inline fun <T : TypeName, S : TypeNameRefStatus, B : TypeNameRefStatusBuilder<S>> TypeRefBuilder<T, S, B>.status(
    block: B.() -> Unit = {}
): TypeRefBuilder<T, S, B> = apply { status.block() }
