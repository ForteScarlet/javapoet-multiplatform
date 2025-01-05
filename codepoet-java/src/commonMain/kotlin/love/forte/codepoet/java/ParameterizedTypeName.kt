package love.forte.codepoet.java

import love.forte.codepoet.java.internal.ParameterizedTypeNameImpl


public interface ParameterizedTypeName : TypeName {
    // private val enclosingType: ParameterizedTypeName

    public val rawType: ClassName

    public val typeArguments: List<TypeName>

    /**
     * Returns a new [ParameterizedTypeName] instance for the specified [name] as nested inside this class.
     */
    public fun nestedClass(name: String): ParameterizedTypeName

    /**
     * Returns a new [ParameterizedTypeName] instance for the specified [name] as nested
     * inside this class, with the specified [typeArguments].
     */
    public fun nestedClass(name: String, typeArguments: List<TypeName>): ParameterizedTypeName

    /**
     * Returns a new [ParameterizedTypeName] instance for the specified [name] as nested
     * inside this class, with the specified [typeArguments].
     */
    public fun nestedClass(name: String, vararg typeArguments: TypeName): ParameterizedTypeName =
        nestedClass(name, typeArguments.asList())

    override fun annotated(annotations: List<AnnotationSpec>): ParameterizedTypeName

    override fun annotated(vararg annotations: AnnotationSpec): ParameterizedTypeName {
        return annotated(annotations.asList())
    }

    override fun withoutAnnotations(): ParameterizedTypeName

    override val isPrimitive: Boolean
        get() = false

    public companion object
}

/*
  TODO
  /** Returns a parameterized type, applying {@code typeArguments} to {@code rawType}. */
  public static ParameterizedTypeName get(Class<?> rawType, Type... typeArguments) {
    return new ParameterizedTypeName(null, ClassName.get(rawType), list(typeArguments));
  }

  /** Returns a parameterized type equivalent to {@code type}. */
  public static ParameterizedTypeName get(ParameterizedType type) {
    return get(type, new LinkedHashMap<>());
  }
 */

/** Returns a parameterized type, applying [typeArguments] to [rawType]. */
public fun ParameterizedTypeName(rawType: ClassName, vararg typeArguments: TypeName): ParameterizedTypeName {
    return ParameterizedTypeNameImpl(null, rawType, typeArguments.asList())
}
