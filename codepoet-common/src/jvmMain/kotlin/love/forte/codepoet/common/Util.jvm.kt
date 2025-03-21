package love.forte.codepoet.common

internal actual inline fun <K, V> MutableMap<K, V>.computeValueIfAbsent(key: K, crossinline f: (K) -> V): V {
    return computeIfAbsent(key) { f(it) }
}

internal actual inline fun <K, V> MutableMap<K, V>.computeValue(key: K, crossinline f: (K, V?) -> V?): V? {
    return compute(key) { k, v -> f(k, v) }!!
}
