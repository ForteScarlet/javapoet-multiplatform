package love.forte.javapoet

internal actual fun Char.isJavaIdentifierStart(): Boolean = isJavaIdentifierStartCommon()

internal actual fun Char.isJavaIdentifierPart(): Boolean = isJavaIdentifierPartCommon()
internal actual inline fun <K, V> MutableMap<K, V>.computeValueIfAbsent(key: K, f: (K) -> V): V {
    val v = get(key)
    if (v == null) {
        val newValue = f(key)
        // TODO if newValue != null?
        put(key, newValue)
        return newValue
    }

    return v
}
