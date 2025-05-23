// /*
//  * Copyright (C) 2015 Square, Inc.
//  *
//  * Licensed under the Apache License, Version 2.0 (the "License");
//  * you may not use this file except in compliance with the License.
//  * You may obtain a copy of the License at
//  *
//  * http://www.apache.org/licenses/LICENSE-2.0
//  *
//  * Unless required by applicable law or agreed to in writing, software
//  * distributed under the License is distributed on an "AS IS" BASIS,
//  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  * See the License for the specific language governing permissions and
//  * limitations under the License.
//  */
//
// @file:JvmName("JavaAnnotationSpecs")
// @file:JvmMultifileClass
//
// package love.forte.codegentle.java.spec
//
// import love.forte.codegentle.common.BuilderDsl
// import love.forte.codegentle.common.code.CodeArgumentPart
// import love.forte.codegentle.common.code.CodeValue
// import love.forte.codegentle.common.code.CodeValueSingleFormatBuilderDsl
// import love.forte.codegentle.common.computeValueIfAbsent
// import love.forte.codegentle.common.naming.ClassName
// import love.forte.codegentle.common.naming.TypeName
// import love.forte.codegentle.common.ref.AnnotationRef
// import love.forte.codegentle.common.ref.AnnotationRefCollectable
// import love.forte.codegentle.java.InternalJavaCodeGentleApi
// import love.forte.codegentle.java.internal.isSourceName
// import love.forte.codegentle.java.spec.internal.JavaAnnotationSpecImpl
// import love.forte.codegentle.java.writer.JavaCodeWriter
// import kotlin.jvm.JvmMultifileClass
// import kotlin.jvm.JvmName
// import kotlin.jvm.JvmStatic
//
// /**
//  * A generated annotation on a declaration.
//  */
// @SubclassOptInRequired(CodeGentleJavaSpecImplementation::class)
// @Deprecated("Use AnnotationRef")
// public interface JavaAnnotationSpec : JavaSpec {
//
//     public val type: TypeName
//
//     public val members: Map<String, List<CodeValue>>
//
//     public val annotations: List<AnnotationRef>
//
//     @InternalJavaCodeGentleApi
//     override fun emit(codeWriter: JavaCodeWriter) {
//         emit(codeWriter, true)
//     }
//
//     @InternalJavaCodeGentleApi
//     public fun emit(codeWriter: JavaCodeWriter, inline: Boolean = true)
//
//     public companion object {
//         public const val VALUE: String = "value"
//
//         @JvmStatic
//         public fun builder(type: ClassName): JavaAnnotationSpecBuilder = JavaAnnotationSpecBuilder(type)
//     }
// }
// //
// // public inline fun JavaAnnotationSpec(
// //     annotation: ClassName,
// //     block: JavaAnnotationSpecBuilder.() -> Unit = {}
// // ): JavaAnnotationSpec =
// //     JavaAnnotationSpec.builder(annotation).also(block).build()
// //
// //
// // public inline fun JavaAnnotationSpecBuilder.addMember(
// //     name: String,
// //     format: String,
// //     block: CodeValueSingleFormatBuilderDsl = {}
// // ): JavaAnnotationSpecBuilder =
// //     addMember(name, CodeValue(format, block))
// //
// // public class JavaAnnotationSpecBuilder internal constructor(private val type: TypeName) :
// //     AnnotationRefCollectable<JavaAnnotationSpecBuilder>,
// //     BuilderDsl {
// //     private val members: MutableMap<String, MutableList<CodeValue>> = linkedMapOf()
// //     private val annotations: MutableList<AnnotationRef> = mutableListOf()
// //
// //     public fun addMember(name: String, codeValue: CodeValue): JavaAnnotationSpecBuilder = apply {
// //         val values = members.computeValueIfAbsent(name) { mutableListOf() }
// //         values.add(codeValue)
// //     }
// //
// //     public fun addMember(
// //         name: String,
// //         format: String,
// //         vararg argumentParts: CodeArgumentPart
// //     ): JavaAnnotationSpecBuilder =
// //         addMember(name, CodeValue(format, *argumentParts))
// //
// //     override fun addAnnotationRef(ref: AnnotationRef): JavaAnnotationSpecBuilder = apply {
// //         annotations.add(ref)
// //     }
// //
// //     override fun addAnnotationRefs(refs: Iterable<AnnotationRef>): JavaAnnotationSpecBuilder = apply {
// //         annotations.addAll(refs)
// //     }
// //
// //     // TODO addMemberForValue(memberName: String, value: Any)
// //
// //     public fun build(): JavaAnnotationSpec {
// //         for (name in members.keys) {
// //             check(name.isSourceName()) { "not a valid name: $name" }
// //         }
// //
// //         return JavaAnnotationSpecImpl(
// //             type,
// //             members.mapValues { it.value.toList() },
// //             annotations.toList()
// //         )
// //     }
// // }
