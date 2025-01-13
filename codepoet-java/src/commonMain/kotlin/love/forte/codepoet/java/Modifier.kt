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

package love.forte.codepoet.java


/**
 *
 * see `javax.lang.model.element.Modifier`
 */
public enum class Modifier {

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

    override fun toString(): String = name.lowercase()
}

internal class ModifierSet(private var value: Int = 0) : Set<Modifier> {
    override val size: Int
        get() = value.countOneBits()

    override fun contains(element: Modifier): Boolean {
        return value and element.ordinal == element.ordinal
    }

    override fun containsAll(elements: Collection<Modifier>): Boolean {
        return elements.all { contains(it) }
    }

    fun add(element: Modifier) {
        value = value or (1 shl element.ordinal)
    }

    fun addAll(vararg elements: Modifier) {
        elements.forEach { add(it) }
    }

    fun addAll(elements: Iterable<Modifier>) {
        if (elements is ModifierSet) {
            value = value or elements.value
        } else {
            elements.forEach { add(it) }
        }
    }

    fun remove(element: Modifier) {
        value or (1 shl element.ordinal)
    }

    override fun toString(): String {
        if (isEmpty()) return "[]"

        return joinToString(", ", prefix = "[", postfix = "]") { it.name }
    }


    override fun isEmpty(): Boolean = value == 0

    override fun iterator(): Iterator<Modifier> {
        if (isEmpty()) return EMPTY_ITERATOR

        return iterator {
            var bits = value
            var lowestBit = bits.takeLowestOneBit()
            do {
                val index = (lowestBit and -1).countTrailingZeroBits()
                yield(Modifier.entries[index])
                bits = bits xor lowestBit
                lowestBit = bits.takeLowestOneBit()
            } while (bits > 0 && lowestBit > 0)
        }
    }

    fun copy(): ModifierSet = ModifierSet(value)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Set<*>) return false

        if (other is ModifierSet) return value == other.value

        // Other sets

        if (other.size != size) return false

        return other.all { it in this }
    }

    override fun hashCode(): Int {
        return value
    }

    companion object {
        private val EMPTY_ITERATOR = object : Iterator<Modifier> {
            override fun hasNext(): Boolean = false

            override fun next(): Modifier {
                throw NoSuchElementException("No next modifier found.")
            }
        }
    }
}
