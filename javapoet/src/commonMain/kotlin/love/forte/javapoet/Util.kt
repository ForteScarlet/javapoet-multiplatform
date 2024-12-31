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

internal expect fun Char.isJavaIdentifierStart(): Boolean

internal expect fun Char.isJavaIdentifierPart(): Boolean

internal fun Char.isJavaIdentifierStartCommon(): Boolean {
    return isLetter() ||
        this in CharCategory.LETTER_NUMBER ||
        this == '$' ||
        this == '_'
}

internal fun Char.isJavaIdentifierPartCommon(): Boolean {
    //   A character may be part of a Java identifier if any of the following conditions are true:
    //   - it is a letter
    //   - it is a currency symbol (such as '$')
    //   - it is a connecting punctuation character (such as '_')
    //   - it is a digit
    //   - it is a numeric letter (such as a Roman numeral character)
    //   - it is a combining mark
    //   - it is a non-spacing mark
    //   isIdentifierIgnorable returns true for the character.
    //   TODO Also missing here:
    //    - a combining mark
    return isLetter() ||
        isDigit() ||
        this in CharCategory.LETTER_NUMBER ||
        this in CharCategory.NON_SPACING_MARK ||
        this == '_' ||
        this == '$' ||
        isIdentifierIgnorable()
    //
}

private fun Char.isIdentifierIgnorable(): Boolean {
    // The following Unicode characters are ignorable in a Java identifier or a Unicode identifier:
    // - ISO control characters that are not whitespace
    //   - '\u0000' through '\u0008'
    //   - '\u000E' through '\u001B'
    //   - '\u007F' through '\u009F'
    // - all characters that have the FORMAT general category value
    return (
        isISOControl() && (
            this in '\u0000'..'\u0008' ||
                this in '\u000E'..'\u001B' ||
                this in '\u007F'..'\u009F'
            )
        ) || this in CharCategory.FORMAT
}