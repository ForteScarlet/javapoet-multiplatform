package love.forte.codegentle.common

@InternalCommonCodeGentleApi
public actual fun <K, V> MutableMap<K, V>.computeValue(key: K, f: (K, V?) -> V?): V? {
    TODO("Not yet implemented")
}

@InternalCommonCodeGentleApi
public actual fun <K, V> MutableMap<K, V>.computeValueIfAbsent(key: K, f: (K) -> V): V {
    TODO("Not yet implemented")
}
