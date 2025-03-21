package love.forte.codepoet.common

@InternalCommonApi
public actual inline fun <K, V> MutableMap<K, V>.computeValueIfAbsent(key: K, crossinline f: (K) -> V): V {
    return computeIfAbsent(key) { f(it) }
}

@InternalCommonApi
public actual inline fun <K, V> MutableMap<K, V>.computeValue(key: K, crossinline f: (K, V?) -> V?): V? {
    return compute(key) { k, v -> f(k, v) }!!
}
