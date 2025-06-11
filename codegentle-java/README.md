# CodeGentle Java

CodeGentle Java is a Kotlin Multiplatform library for generating Java source code. It provides a rich, type-safe API for creating Java classes, interfaces, enums, and other Java language constructs. The library is inspired by [JavaPoet](https://github.com/square/javapoet) but is implemented in Kotlin with multiplatform support.

## Features

### Cross-Platform Support

CodeGentle Java supports multiple platforms:
- JVM
- JavaScript
- WebAssembly
- Native platforms (macOS, iOS, Linux, Android, Windows)

### Java Code Generation

The library provides a comprehensive API for generating Java code:

- **Java Types**: Generate classes, interfaces, enums, annotations, records, and sealed types
- **Java Members**: Generate fields, methods, constructors, and initializer blocks
- **Java Modifiers**: Apply modifiers like public, private, static, final, etc.
- **Java Annotations**: Apply annotations to types, fields, methods, and parameters
- **Java Comments**: Add Javadoc and other comments to generated code
- **Java Imports**: Manage imports, including static imports

## Installation

Add the dependency to your project:

```kotlin
// build.gradle.kts
dependencies {
    implementation("love.forte:codegentle-java:VERSION")
}
```

## Usage Examples

### Generating a Simple Class

```kotlin
import love.forte.codegentle.common.naming.parseToPackageName
import love.forte.codegentle.java.*
import love.forte.codegentle.java.spec.*
import love.forte.codegentle.java.writer.writeToJavaString

// Create a simple class
val typeSpec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.CLASS, "MyClass") {
    addModifiers(JavaModifier.PUBLIC)
}

// Create a Java file with the class
val javaFile = JavaFile("com.example".parseToPackageName(), typeSpec)

// Generate the Java code
val javaCode = javaFile.writeToJavaString()

// The generated code will be:
// package com.example;
//
// public class MyClass {
// }
```

### Generating a Hello World Program

```kotlin
import love.forte.codegentle.common.code.emitString
import love.forte.codegentle.common.code.emitType
import love.forte.codegentle.common.naming.ArrayTypeName
import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.common.naming.parseToPackageName
import love.forte.codegentle.java.*
import love.forte.codegentle.java.naming.JavaClassNames
import love.forte.codegentle.java.naming.JavaPrimitiveTypeNames
import love.forte.codegentle.java.ref.javaRef
import love.forte.codegentle.java.spec.*
import love.forte.codegentle.java.writer.writeToJavaString

// Create the main method
val mainMethod = JavaMethodSpec("main") {
    addModifiers(JavaModifier.PUBLIC, JavaModifier.STATIC)
    returns(JavaPrimitiveTypeNames.VOID)
    addParameter(JavaParameterSpec(ArrayTypeName(JavaClassNames.STRING.javaRef()).javaRef(), "args"))
    addStatement("%V.out.println(%V)") {
        emitType(ClassName("java.lang".parseToPackageName(), "System"))
        emitString("Hello, World!")
    }
}

// Create the class with the main method
val helloWorldClass = JavaSimpleTypeSpec(JavaTypeSpec.Kind.CLASS, "HelloWorld") {
    addModifiers(JavaModifier.PUBLIC)
    addMethod(mainMethod)
}

// Create the Java file
val javaFile = JavaFile("com.example".parseToPackageName(), helloWorldClass)

// Generate the Java code
val javaCode = javaFile.writeToJavaString()

// The generated code will be:
// package com.example;
//
// public class HelloWorld {
//     public static void main(String[] args) {
//         System.out.println("Hello, World!");
//     }
// }
```

### Generating a Class with Fields and Methods

```kotlin
import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.common.naming.parseToPackageName
import love.forte.codegentle.java.*
import love.forte.codegentle.java.naming.JavaPrimitiveTypeNames
import love.forte.codegentle.java.ref.javaRef
import love.forte.codegentle.java.spec.*
import love.forte.codegentle.java.writer.writeToJavaString

// Create fields
val nameField = JavaFieldSpec(JavaClassNames.STRING.javaRef(), "name") {
    addModifiers(JavaModifier.PRIVATE)
}

val ageField = JavaFieldSpec(JavaPrimitiveTypeNames.INT.javaRef(), "age") {
    addModifiers(JavaModifier.PRIVATE)
}

// Create constructor
val constructor = JavaMethodSpec {
    addModifiers(JavaModifier.PUBLIC)
    addParameter(JavaParameterSpec(JavaClassNames.STRING.javaRef(), "name"))
    addParameter(JavaParameterSpec(JavaPrimitiveTypeNames.INT.javaRef(), "age"))
    addStatement("this.name = name")
    addStatement("this.age = age")
}

// Create getter methods
val getNameMethod = JavaMethodSpec("getName") {
    addModifiers(JavaModifier.PUBLIC)
    returns(JavaClassNames.STRING)
    addStatement("return name")
}

val getAgeMethod = JavaMethodSpec("getAge") {
    addModifiers(JavaModifier.PUBLIC)
    returns(JavaPrimitiveTypeNames.INT)
    addStatement("return age")
}

// Create the class
val personClass = JavaSimpleTypeSpec(JavaTypeSpec.Kind.CLASS, "Person") {
    addModifiers(JavaModifier.PUBLIC)
    addJavadoc("A class representing a person.")
    addField(nameField)
    addField(ageField)
    addMethod(constructor)
    addMethod(getNameMethod)
    addMethod(getAgeMethod)
}

// Create the Java file
val javaFile = JavaFile("com.example".parseToPackageName(), personClass)

// Generate the Java code
val javaCode = javaFile.writeToJavaString()

// The generated code will be:
// package com.example;
//
// /**
//  * A class representing a person.
//  */
// public class Person {
//     private String name;
//     private int age;
//
//     public Person(String name, int age) {
//         this.name = name;
//         this.age = age;
//     }
//
//     public String getName() {
//         return name;
//     }
//
//     public int getAge() {
//         return age;
//     }
// }
```

### Generating an Interface

```kotlin
import love.forte.codegentle.common.naming.parseToPackageName
import love.forte.codegentle.java.*
import love.forte.codegentle.java.naming.JavaClassNames
import love.forte.codegentle.java.naming.JavaPrimitiveTypeNames
import love.forte.codegentle.java.ref.javaRef
import love.forte.codegentle.java.spec.*
import love.forte.codegentle.java.writer.writeToJavaString

// Create method specifications
val saveMethod = JavaMethodSpec("save") {
    addParameter(JavaParameterSpec(JavaClassNames.OBJECT.javaRef(), "entity"))
    returns(JavaPrimitiveTypeNames.BOOLEAN)
}

val findByIdMethod = JavaMethodSpec("findById") {
    addParameter(JavaParameterSpec(JavaPrimitiveTypeNames.LONG.javaRef(), "id"))
    returns(JavaClassNames.OBJECT)
}

// Create the interface
val repositoryInterface = JavaSimpleTypeSpec(JavaTypeSpec.Kind.INTERFACE, "Repository") {
    addModifiers(JavaModifier.PUBLIC)
    addJavadoc("A generic repository interface.")
    addMethod(saveMethod)
    addMethod(findByIdMethod)
}

// Create the Java file
val javaFile = JavaFile("com.example.repository".parseToPackageName(), repositoryInterface)

// Generate the Java code
val javaCode = javaFile.writeToJavaString()

// The generated code will be:
// package com.example.repository;
//
// /**
//  * A generic repository interface.
//  */
// public interface Repository {
//     boolean save(Object entity);
//
//     Object findById(long id);
// }
```

### Generating an Enum

```kotlin
import love.forte.codegentle.common.naming.parseToPackageName
import love.forte.codegentle.java.*
import love.forte.codegentle.java.spec.*
import love.forte.codegentle.java.writer.writeToJavaString

// Create the enum
val dayEnum = JavaEnumTypeSpec("Day") {
    addModifiers(JavaModifier.PUBLIC)
    addJavadoc("Days of the week.")
    
    // Add enum constants
    addEnumConstant("MONDAY")
    addEnumConstant("TUESDAY")
    addEnumConstant("WEDNESDAY")
    addEnumConstant("THURSDAY")
    addEnumConstant("FRIDAY")
    addEnumConstant("SATURDAY")
    addEnumConstant("SUNDAY")
}

// Create the Java file
val javaFile = JavaFile("com.example".parseToPackageName(), dayEnum)

// Generate the Java code
val javaCode = javaFile.writeToJavaString()

// The generated code will be:
// package com.example;
//
// /**
//  * Days of the week.
//  */
// public enum Day {
//     MONDAY,
//     TUESDAY,
//     WEDNESDAY,
//     THURSDAY,
//     FRIDAY,
//     SATURDAY,
//     SUNDAY
// }
```

## API Documentation

### Core Packages

- `love.forte.codegentle.java` - Core classes for Java code generation
- `love.forte.codegentle.java.spec` - Classes for representing Java language constructs
- `love.forte.codegentle.java.naming` - Classes for representing Java type names
- `love.forte.codegentle.java.ref` - Classes for representing references to Java types and annotations
- `love.forte.codegentle.java.writer` - Classes for writing generated Java code

### Key Interfaces and Classes

- `JavaFile` - Represents a Java source file
- `JavaTypeSpec` - Base interface for Java type specifications (classes, interfaces, enums, etc.)
- `JavaFieldSpec` - Represents a field in a Java type
- `JavaMethodSpec` - Represents a method or constructor in a Java type
- `JavaParameterSpec` - Represents a parameter in a method or constructor
- `JavaModifier` - Enum of Java modifiers (public, private, static, etc.)
- `JavaCodeWriter` - Interface for writing Java code

### Type Specifications

CodeGentle Java provides several implementations of `JavaTypeSpec` for different kinds of Java types:

- `JavaSimpleTypeSpec` - For classes and interfaces
- `JavaEnumTypeSpec` - For enums
- `JavaAnnotationTypeSpec` - For annotation types
- `JavaRecordTypeSpec` - For records (Java 16+)
- `JavaSealedTypeSpec` - For sealed classes and interfaces (Java 17+)
- `JavaNonSealedTypeSpec` - For non-sealed classes and interfaces (Java 17+)

### Type Names

CodeGentle Java provides classes for representing different types of Java type names:

- `JavaPrimitiveTypeNames` - Constants for Java primitive types (int, boolean, etc.)
- `JavaClassNames` - Constants for common Java classes (String, Object, etc.)

## License

This library is licensed under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).
