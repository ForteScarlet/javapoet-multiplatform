package love.forte.codegentle.common.writer

public sealed class StandardAnnotationRefEmitOption : AnnotationRefEmitOption {
    public data object Inline : StandardAnnotationRefEmitOption()

}
