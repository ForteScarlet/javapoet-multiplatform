/*
 * Copyright (c) 2005, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package love.forte.codegentle.java

import love.forte.codegentle.common.BuilderDsl
import love.forte.codegentle.common.GenEnumSet
import kotlin.jvm.JvmInline


/**
 *
 * see `javax.lang.model.element.Modifier`
 */
@GenEnumSet(internal = true)
public enum class JavaModifier {

    // See JLS sections 8.1.1, 8.3.1, 8.4.3, 8.8.3, and 9.1.1.
    // java.lang.reflect.Modifier includes INTERFACE, but that's a VMism.

    /** The modifier `public` */
    PUBLIC,

    /** The modifier `protected` */
    PROTECTED,

    /** The modifier `private` */
    PRIVATE,

    /** The modifier `abstract` */
    ABSTRACT,

    /** The modifier `default` */
    DEFAULT,

    /** The modifier `static` */
    STATIC,

    /**
     * The modifier `sealed` (since Java 17)
     */
    SEALED,

    /**
     * The modifier `non-sealed` (since Java 17)
     */
    NON_SEALED {
        override fun toString(): String = "non-sealed"
    },

    /** The modifier `final` */
    FINAL,

    /** The modifier `transient` */
    TRANSIENT,

    /** The modifier `volatile` */
    VOLATILE,

    /** The modifier `synchronized` */
    SYNCHRONIZED,

    /** The modifier `native` */
    NATIVE,

    /** The modifier `strictfp` */
    STRICTFP;

    /**
     * Returns this modifier's name as defined in
     * *The Java Language Specification*.
     * The modifier name is the [name of the enum constant][name]
     * in lowercase and with any underscores ("`_`")
     * replaced with hyphens ("`-`").
     *
     * @return the modifier's name
     */
    override fun toString(): String = name.lowercase()
}

public interface JavaModifierBuilderContainer : BuilderDsl {
    public fun addModifier(modifier: JavaModifier): JavaModifierBuilderContainer
    public fun addModifiers(vararg modifiers: JavaModifier): JavaModifierBuilderContainer
    public fun addModifiers(modifiers: Iterable<JavaModifier>): JavaModifierBuilderContainer
}

@JvmInline
public value class JavaModifiers
@PublishedApi internal constructor(private val container: JavaModifierBuilderContainer) {
    public fun public() {
        container.addModifier(JavaModifier.PUBLIC)
    }

    public fun protected() {
        container.addModifier(JavaModifier.PROTECTED)
    }

    public fun private() {
        container.addModifier(JavaModifier.PRIVATE)
    }

    public fun abstract() {
        container.addModifier(JavaModifier.ABSTRACT)
    }

    public fun default() {
        container.addModifier(JavaModifier.DEFAULT)
    }

    public fun static() {
        container.addModifier(JavaModifier.STATIC)
    }

    public fun sealed() {
        container.addModifier(JavaModifier.SEALED)
    }

    public fun nonSealed() {
        container.addModifier(JavaModifier.NON_SEALED)
    }

    public fun final() {
        container.addModifier(JavaModifier.FINAL)
    }

    public fun transient() {
        container.addModifier(JavaModifier.TRANSIENT)
    }

    public fun volatile() {
        container.addModifier(JavaModifier.VOLATILE)
    }

    public fun synchronized() {
        container.addModifier(JavaModifier.SYNCHRONIZED)
    }

    public fun native() {
        container.addModifier(JavaModifier.NATIVE)
    }

    public fun strictfp() {
        container.addModifier(JavaModifier.STRICTFP)
    }
}

public inline val JavaModifierBuilderContainer.modifiers: JavaModifiers
    get() = JavaModifiers(this)

/**
 * ```kotlin
 * container.modifiers {
 *    // add Modifier.PUBLIC
 *    public()
 *
 *    // add Modifier.static
 *    static()
 * }
 * ```
 */
public inline fun JavaModifierBuilderContainer.modifiers(block: JavaModifiers.() -> Unit) {
    modifiers.block()
}
