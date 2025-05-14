package love.forte.codegentle.java

import love.forte.codegentle.common.code.CodeArgumentPart
import love.forte.codegentle.common.code.CodePart
import love.forte.codegentle.common.code.CodePartFactory
import love.forte.codegentle.java.naming.toJavaClassName
import love.forte.codegentle.java.naming.toJavaTypeName
import javax.lang.model.type.TypeMirror
import kotlin.reflect.KClass

@CodePartFactory
public fun CodePart.Companion.type(typeMirror: TypeMirror): CodeArgumentPart {
    return type(typeMirror.toJavaTypeName())
}

@CodePartFactory
public fun CodePart.Companion.type(clz: Class<*>): CodeArgumentPart {
    return type(clz.toJavaTypeName())
}

@CodePartFactory
public fun CodePart.Companion.type(clz: KClass<*>): CodeArgumentPart {
    return type(clz.toJavaClassName())
}

