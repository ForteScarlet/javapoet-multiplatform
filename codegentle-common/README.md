# CodeGentle Common

CodeGentle Common is a Kotlin Multiplatform library that provides a foundation for code generation across different platforms. It offers a rich set of utilities and abstractions for representing and manipulating code elements, type names, and writing generated code.

## Features

### Cross-Platform Support

CodeGentle Common supports multiple platforms:
- JVM
- JavaScript
- WebAssembly
- Native platforms (macOS, iOS, Linux, Android, Windows)

### Code Representation

The library provides a comprehensive API for representing code elements:

#### Code Values and Parts

```kotlin
// Create a simple code value
val simpleCode = CodeValue("val name = %V", CodePart.string("John"))

// Create a more complex code value with a builder
val complexCode = CodeValue {
    add("class User(")
    add("val name: String,")
    add("val age: Int")
    add(")")
}

// Add statements and control flow
val functionCode = CodeValue {
    addStatement("fun greet(name: String) {")
    withIndent {
        addStatement("println(\"Hello, \$name!\")")
    }
    addStatement("}")
}
```

### Type Names

The library provides classes for representing different types of names:

```kotlin
// Create a class name
val userClass = ClassName("com.example", "User")

// Create a parameterized type name
val listOfUsers = ParameterizedTypeName(
    ClassName("kotlin.collections", "List"),
    userClass
)

// Create an array type name
val arrayOfUsers = ArrayTypeName(userClass)

// Create a type variable name
val typeVar = TypeVariableName("T")

// Create a wildcard type name
val wildcardType = LowerWildcardTypeName(userClass.ref(EmptyTypeNameRefStatus))
```

### Code Writing

The library provides a flexible API for writing code:

```kotlin
// Obtain a CodeWriter instance from your implementation
val codeWriter = /* ... */

// Write code with indentation
codeWriter.emit("class User {")
codeWriter.withIndent {
    emit("val name: String")
    emit("val age: Int")

    emit("fun greet() {")
    withIndent {
        emit("println(\"Hello, \$name!\")")
    }
    emit("}")
}
codeWriter.emit("}")

// Get the generated code
val generatedCode = codeWriter.toString()
```

### Unicode Support

The library provides utilities for handling Unicode code points:

```kotlin
import love.forte.codegentle.common.codepoint.*

// Get a code point from a string
val codePoint = "A".codePointAt(0)

// The library provides functions to check properties of code points
// such as case, character count, and Unicode category

// Example: Append a code point to a StringBuilder
val sb = StringBuilder()
sb.appendCodePoint(codePoint)

// Example: Convert a code point to a string
val str = codePoint.toString()
```

## Installation

Add the dependency to your project:

```kotlin
// build.gradle.kts
dependencies {
    implementation("love.forte:codegentle-common:VERSION")
}
```

## Usage Examples

### Generating a Simple Class

```kotlin
fun generateUserClass(): String {
    // Obtain a CodeWriter instance from your implementation
    val codeWriter = /* ... */

    codeWriter.emit("package com.example")
    codeWriter.emitNewLine()

    codeWriter.emit("/**")
    codeWriter.emit(" * Represents a user in the system.")
    codeWriter.emit(" */")
    codeWriter.emit("class User(")
    codeWriter.withIndent {
        emit("val name: String,")
        emit("val age: Int")
    }
    codeWriter.emit(") {")
    codeWriter.withIndent {
        emit("/**")
        emit(" * Greets the user.")
        emit(" */")
        emit("fun greet() {")
        withIndent {
            emit("println(\"Hello, \$name!\")")
        }
        emit("}")
    }
    codeWriter.emit("}")

    return codeWriter.toString()
}
```

### Working with Type Names

```kotlin
fun generateRepositoryInterface(): String {
    // Obtain a CodeWriter instance from your implementation
    val codeWriter = /* ... */

    val entityClass = ClassName("com.example.entity", "User")
    val idType = ClassName("kotlin", "Long")
    val repositoryClass = ParameterizedTypeName(
        ClassName("com.example.repository", "Repository"),
        entityClass,
        idType
    )

    codeWriter.emit("package com.example.repository")
    codeWriter.emitNewLine()

    codeWriter.emit("import com.example.entity.User")
    codeWriter.emitNewLine()

    codeWriter.emit("/**")
    codeWriter.emit(" * Repository for managing User entities.")
    codeWriter.emit(" */")
    codeWriter.emit("interface UserRepository : ")
    codeWriter.emit(repositoryClass)

    return codeWriter.toString()
}
```

### Building Complex Code Values

```kotlin
fun generateServiceClass(): CodeValue {
    return CodeValue {
        addStatement("package com.example.service")
        addStatement("")
        addStatement("import com.example.repository.UserRepository")
        addStatement("")
        addStatement("/**")
        addStatement(" * Service for managing users.")
        addStatement(" */")
        addStatement("class UserService(private val repository: UserRepository) {")
        withIndent {
            addStatement("/**")
            addStatement(" * Finds a user by ID.")
            addStatement(" */")
            addStatement("fun findById(id: Long): User? {")
            withIndent {
                addStatement("return repository.findById(id)")
            }
            addStatement("}")

            addStatement("")

            addStatement("/**")
            addStatement(" * Creates a new user.")
            addStatement(" */")
            addStatement("fun create(name: String, age: Int): User {")
            withIndent {
                addStatement("val user = User(name, age)")
                addStatement("return repository.save(user)")
            }
            addStatement("}")
        }
        addStatement("}")
    }
}
```

## API Documentation

### Core Packages

- `love.forte.codegentle.common.code` - Classes for representing code elements
- `love.forte.codegentle.common.codepoint` - Utilities for handling Unicode code points
- `love.forte.codegentle.common.naming` - Classes for representing type names
- `love.forte.codegentle.common.ref` - Classes for representing references to types and annotations
- `love.forte.codegentle.common.writer` - Classes for writing generated code

### Key Interfaces and Classes

- `CodeValue` - Represents a value in code
- `CodePart` - Represents a part of code
- `TypeName` - Base interface for type names
- `ClassName` - Represents a class name
- `PackageName` - Represents a package name
- `TypeRef` - Represents a reference to a type
- `CodeWriter` - Interface for writing code

## License

This library is licensed under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).
