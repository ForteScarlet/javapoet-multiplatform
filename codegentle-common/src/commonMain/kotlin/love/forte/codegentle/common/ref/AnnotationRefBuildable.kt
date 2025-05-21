package love.forte.codegentle.common.ref

import love.forte.codegentle.common.BuilderDsl
import love.forte.codegentle.common.code.CodeArgumentPart
import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.code.CodeValueSingleFormatBuilderDsl

/**
 *
 * @author ForteScarlet
 */
@SubclassOptInRequired(CodeGentleRefImplementation::class)
public interface AnnotationRefBuildable<B : AnnotationRefBuildable<B>> : BuilderDsl {
    // members: MutableMap<String, MutableList<JavaCodeValue>>

    public fun addMember(name: String, codeValue: CodeValue): B

    public fun addMember(name: String, format: String, vararg argumentParts: CodeArgumentPart): B =
        addMember(name, CodeValue(format, *argumentParts))

}

public inline fun <B : AnnotationRefBuildable<B>> B.addMember(
    name: String,
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): B = addMember(name, CodeValue(format, block))
