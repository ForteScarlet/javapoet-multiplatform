package love.forte.codegentle.java.naming.internal

//
//
// /**
//  *
//  * @author ForteScarlet
//  */
// internal data class ClassNameImpl(
//     override val packageName: String?,
//     override val enclosingClassName: love.forte.codegentle.java.naming.ClassName?,
//     override val simpleName: String,
// ) : love.forte.codegentle.java.naming.ClassName {
//     override val topLevelClassName: love.forte.codegentle.java.naming.ClassName
//         get() = enclosingClassName?.topLevelClassName ?: this
//
//     override fun peerClass(name: String): love.forte.codegentle.java.naming.ClassName {
//         return ClassNameImpl(packageName, enclosingClassName, name)
//     }
//
//     override fun nestedClass(name: String): love.forte.codegentle.java.naming.ClassName {
//         return ClassNameImpl(packageName, this, name)
//     }
//
//     override fun unbox(): JavaTypeName {
//         if (packageName == JavaClassNames.JAVA_LANG_PACKAGE && enclosingClassName == null) {
//             return when (simpleName) {
//                 JavaClassNames.BOXED_VOID_SIMPLE_NAME -> JavaTypeName.Builtins.VOID
//                 JavaClassNames.BOXED_BOOLEAN_SIMPLE_NAME -> JavaTypeName.Builtins.BOOLEAN
//                 JavaClassNames.BOXED_BYTE_SIMPLE_NAME -> JavaTypeName.Builtins.BYTE
//                 JavaClassNames.BOXED_SHORT_SIMPLE_NAME -> JavaTypeName.Builtins.SHORT
//                 JavaClassNames.BOXED_INT_SIMPLE_NAME -> JavaTypeName.Builtins.INT
//                 JavaClassNames.BOXED_LONG_SIMPLE_NAME -> JavaTypeName.Builtins.LONG
//                 JavaClassNames.BOXED_CHAR_SIMPLE_NAME -> JavaTypeName.Builtins.CHAR
//                 JavaClassNames.BOXED_FLOAT_SIMPLE_NAME -> JavaTypeName.Builtins.FLOAT
//                 JavaClassNames.BOXED_DOUBLE_SIMPLE_NAME -> JavaTypeName.Builtins.DOUBLE
//                 else -> throw IllegalStateException("Can't unbox $this")
//             } // .annotated(annotations)
//         }
//
//         throw IllegalStateException("Can't unbox $this")
//     }
//
//     override fun emit(codeWriter: JavaCodeWriter) {
//         fun enclosingClasses(): List<love.forte.codegentle.java.naming.ClassName> {
//             val result = mutableListOf<love.forte.codegentle.java.naming.ClassName>()
//             var c: love.forte.codegentle.java.naming.ClassName? = this
//             while (c != null) {
//                 result.add(c)
//                 c = c.enclosingClassName
//             }
//             result.reverse()
//             return result
//         }
//
//         var charsEmitted = false
//
//         for (className in enclosingClasses()) {
//             val simpleName: String
//             if (charsEmitted) {
//                 // We've already emitted an enclosing class. Emit as we go.
//                 codeWriter.emit(".")
//                 simpleName = className.simpleName
//             } else if (className === this) {
//                 // We encountered the first enclosing class that must be emitted.
//                 val qualifiedName: String = codeWriter.lookupName(className)
//                 val dot = qualifiedName.lastIndexOf('.')
//                 if (dot != -1) {
//                     codeWriter.emitAndIndent(qualifiedName.substring(0, dot + 1))
//                     simpleName = qualifiedName.substring(dot + 1)
//                     charsEmitted = true
//                 } else {
//                     simpleName = qualifiedName
//                 }
//             } else {
//                 // Don't emit this enclosing type. Keep going so we can be more precise.
//                 continue
//             }
//
//             codeWriter.emit(simpleName)
//             charsEmitted = true
//         }
//     }
//
//     override fun toString(): String {
//         return emitToString()
//     }
// }
//
