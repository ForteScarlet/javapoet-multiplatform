package love.forte.codepoet.java.ref

import love.forte.codepoet.common.BuilderDsl
import love.forte.codepoet.common.code.CodeArgumentPart
import love.forte.codepoet.java.CodeValueSingleFormatBuilderDsl
import love.forte.codepoet.java.JavaCodeValue

/**
 *
 * @author ForteScarlet
 */
public interface JavaAnnotationRefBuildable<B : JavaAnnotationRefBuildable<B>> : BuilderDsl {
    // members: MutableMap<String, MutableList<JavaCodeValue>>

    public fun addMember(name: String, codeValue: JavaCodeValue): B

    public fun addMember(name: String, format: String, vararg argumentParts: CodeArgumentPart): B =
        addMember(name, JavaCodeValue(format, *argumentParts))

}

public inline fun <B : JavaAnnotationRefBuildable<B>> B.addMember(
    name: String,
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): B = addMember(name, JavaCodeValue(format, block))
