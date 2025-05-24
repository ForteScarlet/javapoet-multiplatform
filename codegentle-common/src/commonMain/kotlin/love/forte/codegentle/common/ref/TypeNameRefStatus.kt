package love.forte.codegentle.common.ref

/**
 * A reference status.
 *
 * @author ForteScarlet
 */
@SubclassOptInRequired(CodeGentleRefImplementation::class)
public interface TypeNameRefStatus

public object EmptyTypeNameRefStatus : TypeNameRefStatus

// TODO Kotlin ref

// Builders

@SubclassOptInRequired(CodeGentleRefImplementation::class)
public interface TypeNameRefStatusBuilder<out T : TypeNameRefStatus> {
    public fun build(): T
}


@SubclassOptInRequired(CodeGentleRefImplementation::class)
public interface TypeNameRefStatusBuilderFactory<out T : TypeNameRefStatus, out B : TypeNameRefStatusBuilder<T>> {
    public fun createBuilder(): B
}
