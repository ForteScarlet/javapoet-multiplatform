# CodeGentle Java - Java Code Generation API

CodeGentle Java is a powerful API for generating Java source code. It provides a fluent, easy-to-use interface for creating Java classes, interfaces, enums, annotations, methods, fields, and more.

## Table of Contents

- [Introduction](#introduction)
- [Getting Started](#getting-started)
- [Basic Examples](#basic-examples)
  - [Hello World](#hello-world)
  - [Creating a Java File](#creating-a-java-file)
- [Working with Types](#working-with-types)
  - [Classes](#classes)
  - [Interfaces](#interfaces)
  - [Enums](#enums)
  - [Annotations](#annotations)
  - [Records](#records)
  - [Sealed Classes](#sealed-classes)
- [Working with Methods](#working-with-methods)
  - [Basic Methods](#basic-methods)
  - [Constructors](#constructors)
  - [Method Parameters](#method-parameters)
  - [Generic Methods](#generic-methods)
  - [Exceptions](#exceptions)
- [Working with Fields](#working-with-fields)
  - [Basic Fields](#basic-fields)
  - [Field Initializers](#field-initializers)
- [Type Names and References](#type-names-and-references)
  - [Class Names](#class-names)
  - [Primitive Types](#primitive-types)
  - [Array Types](#array-types)
  - [Parameterized Types](#parameterized-types)
  - [Type Variables](#type-variables)
  - [Wildcard Types](#wildcard-types)
- [Control Flow](#control-flow)
  - [If Statements](#if-statements)
  - [For Loops](#for-loops)
  - [While Loops](#while-loops)
  - [Try-Catch Blocks](#try-catch-blocks)
- [Advanced Features](#advanced-features)
  - [Static Imports](#static-imports)
  - [Javadoc](#javadoc)
  - [Annotations](#annotations-1)
  - [Modifiers](#modifiers)
- [Best Practices](#best-practices)

## Introduction

CodeGentle Java is a Kotlin Multiplatform library for generating Java source code. It provides a type-safe, fluent API that makes it easy to generate Java code programmatically. Whether you're writing an annotation processor, a code generator, or any other tool that needs to generate Java code, CodeGentle Java can help you do it efficiently and correctly.

## Getting Started

To use CodeGentle Java, you need to add it as a dependency to your project. Then, you can start generating Java code using the API.

## Basic Examples

### Hello World

Let's start with a simple "Hello World" example:

```kotlin
val spec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.CLASS, "HelloWorld") {
    modifiers.public()
    modifiers.final()

    addMethod("main") {
        modifiers {
            public()
            static()
        }

        addStatement("System.out.println(%V)") {
            emitString("Hello, World!")
        }
    }
}
```

This generates the following Java code:

```java
public final class HelloWorld {
    public static void main() {
        System.out.println("Hello, World!");
    }
}
```

### Creating a Java File

To create a complete Java file with a package declaration and imports, use the `JavaFile` class:

```kotlin
val typeSpec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.CLASS, "MyClass")
val packageName = "com.example".parseToPackageName()

val javaFile = JavaFile(packageName, typeSpec) {
    addFileComment("This is a generated file. Do not edit!")
    addStaticImport(ClassName("java.util", "Collections"), "emptyList")
}

// Write to a string
val javaCode = javaFile.writeToJavaString()

// Or write to a file
// javaFile.writeTo(File("MyClass.java"))
```

This generates:

```java
// This is a generated file. Do not edit!
package com.example;

import static java.util.Collections.emptyList;

class MyClass {
}
```

## Working with Types

### Classes

Create a simple class:

```kotlin
val classSpec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.CLASS, "MyClass") {
    modifiers.public()

    addJavadoc("This is a sample class.")

    addField(JavaPrimitiveTypeNames.INT, "count") {
        modifiers.private()
        initializer("0")
    }

    addMethod("getCount") {
        modifiers.public()
        returns(JavaPrimitiveTypeNames.INT.javaRef())
        addStatement("return count")
    }

    addMethod("setCount") {
        modifiers.public()
        addParameter(JavaParameterSpec(JavaPrimitiveTypeNames.INT.javaRef(), "count"))
        addStatement("this.count = count")
    }
}
```

### Interfaces

Create an interface:

```kotlin
val interfaceSpec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.INTERFACE, "MyInterface") {
    modifiers.public()

    addMethod("doSomething") {
        addParameter(JavaParameterSpec(ClassName("java.lang", "String").javaRef(), "input"))
        returns(JavaPrimitiveTypeNames.BOOLEAN.javaRef())
    }
}
```

### Enums

Create an enum:

```kotlin
val enumSpec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.ENUM, "Direction") {
    modifiers.public()

    addField(JavaFieldSpec(ClassName("java.lang", "String").javaRef(), "NORTH") {
        modifiers {
            public()
            static()
            final()
        }
    })

    addField(JavaFieldSpec(ClassName("java.lang", "String").javaRef(), "SOUTH") {
        modifiers {
            public()
            static()
            final()
        }
    })

    addField(JavaFieldSpec(ClassName("java.lang", "String").javaRef(), "EAST") {
        modifiers {
            public()
            static()
            final()
        }
    })

    addField(JavaFieldSpec(ClassName("java.lang", "String").javaRef(), "WEST") {
        modifiers {
            public()
            static()
            final()
        }
    })
}
```

### Annotations

Create an annotation:

```kotlin
val annotationSpec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.ANNOTATION, "MyAnnotation") {
    modifiers.public()

    addMethod("value") {
        returns(ClassName("java.lang", "String").javaRef())
        defaultValue("\"\"")
    }
}
```

### Records

Create a record (Java 16+):

```kotlin
val recordSpec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.RECORD, "Person") {
    modifiers.public()

    addParameter(JavaParameterSpec(ClassName("java.lang", "String").javaRef(), "name"))
    addParameter(JavaParameterSpec(JavaPrimitiveTypeNames.INT.javaRef(), "age"))

    addMethod("greet") {
        modifiers.public()
        addStatement("return \"Hello, \" + name")
    }
}
```

### Sealed Classes

Create a sealed class (Java 17+):

```kotlin
val sealedClassSpec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.SEALED_CLASS, "Shape") {
    modifiers.public()

    addMethod("area") {
        modifiers {
            public()
            abstract()
        }
        returns(JavaPrimitiveTypeNames.DOUBLE.javaRef())
    }
}

val circleSpec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.NON_SEALED_CLASS, "Circle") {
    modifiers.public()
    superclass(ClassName("com.example", "Shape").javaRef())

    addField(JavaPrimitiveTypeNames.DOUBLE, "radius") {
        modifiers {
            private()
            final()
        }
    }

    addMethod("area") {
        modifiers {
            public()
            override()
        }
        returns(JavaPrimitiveTypeNames.DOUBLE.javaRef())
        addStatement("return Math.PI * radius * radius")
    }
}
```

## Working with Methods

### Basic Methods

Create a simple method:

```kotlin
val methodSpec = JavaMethodSpec("greet") {
    modifiers.public()
    returns(ClassName("java.lang", "String").javaRef())
    addStatement("return \"Hello, World!\"")
}
```

### Constructors

Create a constructor:

```kotlin
val constructorSpec = JavaMethodSpec {
    modifiers.public()
    addParameter(JavaParameterSpec(ClassName("java.lang", "String").javaRef(), "name"))
    addParameter(JavaParameterSpec(JavaPrimitiveTypeNames.INT.javaRef(), "age"))
    addStatement("this.name = name")
    addStatement("this.age = age")
}
```

### Method Parameters

Add parameters to a method:

```kotlin
val methodSpec = JavaMethodSpec("calculate") {
    modifiers {
        public()
        static()
    }
    returns(JavaPrimitiveTypeNames.INT.javaRef())

    addParameter(JavaParameterSpec(JavaPrimitiveTypeNames.INT.javaRef(), "a"))
    addParameter(JavaParameterSpec(JavaPrimitiveTypeNames.INT.javaRef(), "b"))

    addStatement("return a + b")
}
```

### Generic Methods

Create a generic method:

```kotlin
val methodSpec = JavaMethodSpec("transform") {
    modifiers {
        public()
        static()
    }

    addTypeVariable(TypeVariableName("T").javaRef())
    addTypeVariable(TypeVariableName("R").javaRef())

    addParameter(JavaParameterSpec(TypeVariableName("T").javaRef(), "input"))
    returns(TypeVariableName("R").javaRef())

    addStatement("// Transform input to output")
    addStatement("return null")
}
```

### Exceptions

Add exceptions to a method:

```kotlin
val methodSpec = JavaMethodSpec("readFile") {
    modifiers.public()

    addException(ClassName("java.io", "IOException").javaRef())

    addStatement("// Read file implementation")
}
```

## Working with Fields

### Basic Fields

Create a simple field:

```kotlin
val fieldSpec = JavaFieldSpec(JavaPrimitiveTypeNames.INT, "count") {
    modifiers.private()
}
```

### Field Initializers

Add an initializer to a field:

```kotlin
val fieldSpec = JavaFieldSpec(JavaPrimitiveTypeNames.INT, "count") {
    modifiers.private()
    initializer("0")
}

val stringFieldSpec = JavaFieldSpec(ClassName("java.lang", "String").javaRef(), "name") {
    modifiers.private()
    initializer("%V", "John Doe")
}
```

## Type Names and References

### Class Names

Create class names:

```kotlin
val stringName = ClassName("java.lang", "String")
val listName = ClassName("java.util", "List")
val mapName = ClassName("java.util", "Map")
```

### Primitive Types

Use primitive types:

```kotlin
val intType = JavaPrimitiveTypeNames.INT
val booleanType = JavaPrimitiveTypeNames.BOOLEAN
val doubleType = JavaPrimitiveTypeNames.DOUBLE
```

### Array Types

Create array types:

```kotlin
val stringArrayType = ClassName("java.lang", "String").array()
val intArrayType = JavaPrimitiveTypeNames.INT.array()
```

### Parameterized Types

Create parameterized types (generics):

```kotlin
val listOfStrings = ClassName("java.util", "List").parameterizedBy(ClassName("java.lang", "String"))
val mapOfStringToInteger = ClassName("java.util", "Map").parameterizedBy(
    ClassName("java.lang", "String"),
    ClassName("java.lang", "Integer")
)
```

### Type Variables

Create type variables:

```kotlin
val tType = TypeVariableName("T")
val eType = TypeVariableName("E", ClassName("java.lang", "Exception"))
```

### Wildcard Types

Create wildcard types:

```kotlin
val wildcardType = WildcardTypeName.subtypeOf(ClassName("java.lang", "Number"))
val listOfWildcards = ClassName("java.util", "List").parameterizedBy(wildcardType)
```

## Control Flow

### If Statements

Generate if statements:

```kotlin
val methodSpec = JavaMethodSpec("checkValue") {
    modifiers.public()
    addParameter(JavaParameterSpec(JavaPrimitiveTypeNames.INT.javaRef(), "value"))

    beginControlFlow("if (value > 0)") {
        addStatement("return \"Positive\"")
    }
    nextControlFlow("else if (value < 0)") {
        addStatement("return \"Negative\"")
    }
    nextControlFlow("else") {
        addStatement("return \"Zero\"")
    }
    endControlFlow()
}
```

### For Loops

Generate for loops:

```kotlin
val methodSpec = JavaMethodSpec("printNumbers") {
    modifiers.public()

    beginControlFlow("for (int i = 0; i < 10; i++)") {
        addStatement("System.out.println(i)")
    }
    endControlFlow()
}
```

### While Loops

Generate while loops:

```kotlin
val methodSpec = JavaMethodSpec("countDown") {
    modifiers.public()
    addParameter(JavaParameterSpec(JavaPrimitiveTypeNames.INT.javaRef(), "start"))

    addStatement("int i = start")
    beginControlFlow("while (i > 0)") {
        addStatement("System.out.println(i)")
        addStatement("i--")
    }
    endControlFlow()
}
```

### Try-Catch Blocks

Generate try-catch blocks:

```kotlin
val methodSpec = JavaMethodSpec("readFile") {
    modifierOp.public()
    addParameter(JavaParameterSpec(ClassName("java.lang", "String").javaRef(), "filename"))

    beginControlFlow("try") {
        addStatement("// Read file implementation")
    }
    nextControlFlow("catch (java.io.IOException e)") {
        addStatement("e.printStackTrace()")
    }
    endControlFlow()
}
```

## Advanced Features

### Static Imports

Add static imports to a Java file:

```kotlin
val javaFile = JavaFile(packageName, typeSpec) {
    addStaticImport(ClassName("java.util", "Collections"), "emptyList")
    addStaticImport(ClassName("java.util", "Collections"), "emptyMap")
}
```

### Javadoc

Add Javadoc to types, methods, and fields:

```kotlin
val classSpec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.CLASS, "MyClass") {
    addJavadoc("This is a sample class.\n")
    addJavadoc("@author CodeGentle")

    addMethod("doSomething") {
        addJavadoc("This method does something.\n")
        addJavadoc("@param input the input string\n")
        addJavadoc("@return true if successful, false otherwise")

        addParameter(JavaParameterSpec(ClassName("java.lang", "String").javaRef(), "input"))
        returns(JavaPrimitiveTypeNames.BOOLEAN.javaRef())
        addStatement("return true")
    }
}
```

### Annotations

Add annotations to types, methods, fields, and parameters:

```kotlin
val methodSpec = JavaMethodSpec("processData") {
    addAnnotationRef(ClassName("java.lang", "Override").javaRef().asAnnotation())
    addAnnotationRef(ClassName("java.lang", "SuppressWarnings").javaRef().asAnnotation {
        addMember("value", "\"unchecked\"")
    })

    modifierOp.public()
    returns(JavaPrimitiveTypeNames.VOID.javaRef())

    addParameter(JavaParameterSpec(ClassName("java.util", "List").javaRef(), "data") {
        addAnnotationRef(ClassName("javax.annotation", "Nonnull").javaRef().asAnnotation())
    })

    addStatement("// Process data")
}
```

### Modifiers

Add modifiers to types, methods, and fields:

```kotlin
val classSpec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.CLASS, "MyClass") {
    modifierOp.public()
    modifierOp.final()

    addField(JavaPrimitiveTypeNames.INT, "count") {
        modifierOp.private()
        modifierOp.static()
        modifierOp.final()
        initializer("0")
    }

    addMethod("getCount") {
        modifierOp.public()
        modifierOp.static()
        returns(JavaPrimitiveTypeNames.INT.javaRef())
        addStatement("return count")
    }
}
```

## Best Practices

1. **Use the Builder Pattern**: Always use the builder pattern to create specs, as it provides a fluent API and ensures that all required properties are set.

2. **Organize Your Code**: Keep your code generation logic organized by separating it into methods or classes based on functionality.

3. **Use Type References**: Use type references instead of strings to ensure type safety and to take advantage of IDE features like auto-completion and refactoring.

4. **Add Comments**: Add comments to your generated code to explain its purpose and usage.

5. **Test Your Generated Code**: Always test your generated code to ensure it compiles and behaves as expected.

6. **Handle Edge Cases**: Consider edge cases like null values, empty collections, and invalid inputs when generating code.

7. **Use Meaningful Names**: Use meaningful names for your types, methods, fields, and parameters to make the generated code more readable.

8. **Follow Java Conventions**: Follow Java naming conventions and coding style in your generated code.
