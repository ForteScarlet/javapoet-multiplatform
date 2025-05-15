@file:JvmName("TypeNames")
@file:JvmMultifileClass

package love.forte.codegentle.common.naming

import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName

/**
 * A JVM-based type name.
 *
 * @author ForteScarlet
 */
public sealed interface TypeName : Naming


public interface PlatformTypeName : TypeName
