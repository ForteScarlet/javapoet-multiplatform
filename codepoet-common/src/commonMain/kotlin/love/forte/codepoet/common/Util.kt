package love.forte.codepoet.common

@InternalCommonApi
public expect fun <K, V> MutableMap<K, V>.computeValue(key: K, f: (K, V?) -> V?): V?

@InternalCommonApi
public expect fun <K, V> MutableMap<K, V>.computeValueIfAbsent(key: K, f: (K) -> V): V
