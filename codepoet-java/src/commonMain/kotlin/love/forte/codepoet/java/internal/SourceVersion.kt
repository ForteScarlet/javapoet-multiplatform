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

package love.forte.codepoet.java.internal

import love.forte.codepoet.common.codepoint.charCount
import love.forte.codepoet.common.codepoint.codePointAt
import love.forte.codepoet.java.codepoint.isJavaIdentifierPart
import love.forte.codepoet.java.codepoint.isJavaIdentifierStart

// See javax.lang.model.SourceVersion.java

private val keywords = setOf(
    "abstract", "continue", "for", "new", "switch",
    "assert", "default", "if", "package", "synchronized",
    "boolean", "do", "goto", "private", "this",
    "break", "double", "implements", "protected", "throw",
    "byte", "else", "import", "public", "throws",
    "case", "enum", "instanceof", "return", "transient",
    "catch", "extends", "int", "short", "try",
    "char", "final", "interface", "static", "void",
    "class", "finally", "long", "strictfp", "volatile",
    "const", "float", "native", "super", "while",
    // literals
    "null", "true", "false"
)

internal fun CharSequence.isSourceKeyword() = toString() in keywords

internal fun CharSequence.isSourceName(): Boolean {
    val id = toString()

    for (s in id.split("\\.".toRegex()).toTypedArray()) {
        if (!s.isSourceIdentifier() || s.isSourceKeyword()) return false
    }
    return true
}

internal fun CharSequence.isSourceIdentifier(): Boolean {
    val id: String = toString()

    if (id.isEmpty()) {
        return false
    }
    var cp = id.codePointAt(0)
    if (!cp.isJavaIdentifierStart()) {
        return false
    }

    var i: Int = cp.charCount()
    while (i < id.length) {
        cp = id.codePointAt(i)

        if (!cp.isJavaIdentifierPart()) {
            return false
        }

        i += cp.charCount()
    }

    return true
}

