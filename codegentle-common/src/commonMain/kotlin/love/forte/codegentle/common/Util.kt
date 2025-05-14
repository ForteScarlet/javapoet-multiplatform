package love.forte.codegentle.common

@InternalCommonCodeGentleApi
public expect fun <K, V> MutableMap<K, V>.computeValue(key: K, f: (K, V?) -> V?): V?

@InternalCommonCodeGentleApi
public expect fun <K, V> MutableMap<K, V>.computeValueIfAbsent(key: K, f: (K) -> V): V
