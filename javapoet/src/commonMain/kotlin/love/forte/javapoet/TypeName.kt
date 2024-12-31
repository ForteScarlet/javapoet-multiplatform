/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package love.forte.javapoet

public interface TypeName {

    public val annotations: List<AnnotationSpec>

    public fun annotated(annotations: List<AnnotationSpec>): TypeName

    public fun annotated(vararg annotations: AnnotationSpec): TypeName {
        return annotated(annotations.asList())
    }

    public fun withoutAnnotations(): TypeName

    public fun concatAnnotations(annotations: List<AnnotationSpec>): TypeName

    public val isAnnotated: Boolean
        get() = annotations.isNotEmpty()

    /**
     * Returns true if this is a primitive type like `int`.
     * Returns false for all other types
     *
     * types including boxed primitives and `void`.
     */
    public val isPrimitive: Boolean

    // TODO emit?
}
