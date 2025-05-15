package love.forte.codegentle.java.naming

import love.forte.codegentle.common.naming.ClassName

public object JavaAnnotationNames {
    /**
     * see `java.lang.Override`
     */
    public val Override: ClassName = ClassName("java.lang", "Override")

    /**
     * see `java.lang.Deprecated`
     */
    public val Deprecated: ClassName = ClassName("java.lang", "Deprecated")

    /**
     * see `java.lang.SuppressWarnings`
     */
    public val SuppressWarnings: ClassName = ClassName("java.lang", "SuppressWarnings")

    /**
     * see `java.lang.SafeVarargs`
     */
    public val SafeVarargs: ClassName = ClassName("java.lang", "SafeVarargs")

    /**
     * see `java.lang.annotation.Documented`
     */
    public val Documented: ClassName = ClassName("java.lang", "Documented")

    /**
     * see `java.lang.annotation.Retention`
     */
    public val Retention: ClassName = ClassName("java.lang.annotation", "Retention")

    /**
     * see `java.lang.annotation.Target`
     */
    public val Target: ClassName = ClassName("java.lang.annotation", "Target")

    /**
     * see `java.lang.annotation.Inherited`
     */
    public val Inherited: ClassName = ClassName("java.lang.annotation", "Inherited")

    /**
     * see `java.lang.annotation.Repeatable`
     */
    public val Repeatable: ClassName = ClassName("java.lang.annotation", "Repeatable")

    /**
     * see `java.lang.annotation.Native`
     *
     * since Java 1.8
     */
    public val Native: ClassName = ClassName("java.lang.annotation", "Native")

    /**
     * see `java.lang.FunctionalInterface`
     *
     * since Java 1.8
     */
    public val FunctionalInterface: ClassName = ClassName("java.lang", "FunctionalInterface")

    /**
     * see `javax.annotation.processing.Generated` in module `java.compiler` .
     *
     * since Java 9
     */
    public val Generated: ClassName = ClassName("javax.annotation.processing", "Generated")

}
