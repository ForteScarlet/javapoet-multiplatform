package love.forte.codegentle.kotlin.ref.internal

import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.kotlin.InternalKotlinCodeGentleApi
import love.forte.codegentle.kotlin.ref.KotlinTypeNameRefStatus

/**
 * [love.forte.codegentle.kotlin.ref.KotlinTypeNameRefStatus] 的实现类。
 *
 * @property annotations 类型上的注解列表
 * @property nullable 类型是否可为空
 */
@InternalKotlinCodeGentleApi
internal data class KotlinTypeNameRefStatusImpl(
    override val annotations: List<AnnotationRef>,
    val nullable: Boolean
) : KotlinTypeNameRefStatus

//
// /**
//  * [KotlinTypeNameRefStatus] 的实现类。
//  *
//  * @property annotations 类型上的注解列表
//  * @property nullable 类型是否可为空
//  */
// @InternalKotlinCodeGentleApi
// internal class KotlinTypeNameRefStatusImpl(
//     override val annotations: List<AnnotationRef>,
//     val nullable: Boolean
// ) : KotlinTypeNameRefStatus {
//     override fun equals(other: Any?): Boolean {
//         if (this === other) return true
//         if (other !is KotlinTypeNameRefStatusImpl) return false
//
//         if (annotations != other.annotations) return false
//         if (nullable != other.nullable) return false
//
//         return true
//     }
//
//     override fun hashCode(): Int {
//         var result = annotations.hashCode()
//         result = 31 * result + nullable.hashCode()
//         return result
//     }
//
//     override fun toString(): String {
//         return "KotlinTypeNameRefStatusImpl(annotations=$annotations, nullable=$nullable)"
//     }
// }
