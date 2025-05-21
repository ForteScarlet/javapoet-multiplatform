package love.forte.codegentle.common.code.internal

import love.forte.codegentle.common.code.CodePart
import love.forte.codegentle.common.code.CodeValue

/**
 *
 * @author ForteScarlet
 */
internal data class CodeValueImpl(override val parts: List<CodePart>) : CodeValue
