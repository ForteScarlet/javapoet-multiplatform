package love.forte.codepoet.common

internal expect fun <K, V> MutableMap<K, V>.computeValue(key: K, f: (K, V?) -> V?): V?

internal expect fun <K, V> MutableMap<K, V>.computeValueIfAbsent(key: K, f: (K) -> V): V
