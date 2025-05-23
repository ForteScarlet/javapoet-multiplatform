package love.forte.codegentle.common.writer

/**
 * Option for [CodeWriter].emit(s).
 *
 * @author ForteScarlet
 */
@SubclassOptInRequired(CodeGentleCodeWriterImplementation::class)
public interface EmitOption

@SubclassOptInRequired(CodeGentleCodeWriterImplementation::class)
public interface TypeNameEmitOption : EmitOption

@SubclassOptInRequired(CodeGentleCodeWriterImplementation::class)
public interface TypeRefEmitOption : EmitOption

@SubclassOptInRequired(CodeGentleCodeWriterImplementation::class)
public interface AnnotationRefEmitOption : EmitOption

@SubclassOptInRequired(CodeGentleCodeWriterImplementation::class)
public interface CodeValueEmitOption : EmitOption
