package love.forte.codepoet.java

internal actual fun Char.isJavaIdentifierStart(): Boolean =
    Character.isJavaIdentifierStart(this)

internal actual fun Char.isJavaIdentifierPart(): Boolean =
    Character.isJavaIdentifierPart(this)

// internal actual inline fun <K, V> MutableMap<K, V>.computeValueIfAbsent(key: K, crossinline f: (K) -> V): V {
//     return computeIfAbsent(key) { f(it) }
// }
//
// internal actual inline fun <K, V> MutableMap<K, V>.computeValue(key: K, crossinline f: (K, V?) -> V?): V? {
//     return compute(key) { k, v -> f(k, v) }!!
// }
