package love.forte.codepoet.java

// internal actual fun Char.isJavaIdentifierStart(): Boolean = isJavaIdentifierStartCommon()
//
// internal actual fun Char.isJavaIdentifierPart(): Boolean = isJavaIdentifierPartCommon()

// internal actual inline fun <K, V> MutableMap<K, V>.computeValueIfAbsent(key: K, f: (K) -> V): V {
//     val v = get(key)
//     if (v == null) {
//         val newValue = f(key)
//         // TODO if newValue != null?
//         put(key, newValue)
//         return newValue
//     }
//
//     return v
// }
//
// internal actual fun <K, V> MutableMap<K, V>.computeValue(key: K, f: (K, V?) -> V?): V? {
//     val oldValue = get(key)
//
//     val newValue = f(key, oldValue)
//     if (newValue == null) {
//         if (oldValue != null || containsKey(key)) {
//             remove(key)
//             return null
//         } else {
//             return null
//         }
//     } else {
//         put(key, newValue)
//         return newValue
//     }
// }
