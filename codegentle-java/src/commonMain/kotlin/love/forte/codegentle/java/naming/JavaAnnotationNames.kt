package love.forte.codegentle.java.naming

public object JavaAnnotationNames {
    /**
     * see `java.lang.Override`
     */
    public val Override: JavaClassName = JavaClassName("java.lang", "Override")

    /**
     * see `java.lang.Deprecated`
     */
    public val Deprecated: JavaClassName = JavaClassName("java.lang", "Deprecated")

    /**
     * see `java.lang.SuppressWarnings`
     */
    public val SuppressWarnings: JavaClassName = JavaClassName("java.lang", "SuppressWarnings")

    /**
     * see `java.lang.SafeVarargs`
     */
    public val SafeVarargs: JavaClassName = JavaClassName("java.lang", "SafeVarargs")

    /**
     * see `java.lang.annotation.Documented`
     */
    public val Documented: JavaClassName = JavaClassName("java.lang", "Documented")

    /**
     * see `java.lang.annotation.Retention`
     */
    public val Retention: JavaClassName = JavaClassName("java.lang.annotation", "Retention")

    /**
     * see `java.lang.annotation.Target`
     */
    public val Target: JavaClassName = JavaClassName("java.lang.annotation", "Target")

    /**
     * see `java.lang.annotation.Inherited`
     */
    public val Inherited: JavaClassName = JavaClassName("java.lang.annotation", "Inherited")

    /**
     * see `java.lang.annotation.Repeatable`
     */
    public val Repeatable: JavaClassName = JavaClassName("java.lang.annotation", "Repeatable")

    /**
     * see `java.lang.annotation.Native`
     *
     * since Java 1.8
     */
    public val Native: JavaClassName = JavaClassName("java.lang.annotation", "Native")

    /**
     * see `java.lang.FunctionalInterface`
     *
     * since Java 1.8
     */
    public val FunctionalInterface: JavaClassName = JavaClassName("java.lang", "FunctionalInterface")

    /**
     * see `javax.annotation.processing.Generated` in module `java.compiler` .
     *
     * since Java 9
     */
    public val Generated: JavaClassName = JavaClassName("javax.annotation.processing", "Generated")

}
