package love.forte.codepoet.java

import javax.lang.model.type.TypeMirror
import kotlin.reflect.KClass

@CodePartFactory
public fun CodePart.Companion.type(typeMirror: TypeMirror): CodeArgumentPart {
    return type(typeMirror.toTypeName())
}

@CodePartFactory
public fun CodePart.Companion.type(clz: Class<*>): CodeArgumentPart {
    return type(clz.toTypeName())
}

@CodePartFactory
public fun CodePart.Companion.type(clz: KClass<*>): CodeArgumentPart {
    return type(clz.toClassName())
}

