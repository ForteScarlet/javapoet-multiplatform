# CodeGentle Java - Java 代码生成 API

CodeGentle Java 是一个强大的 Java 源代码生成 API。它提供了一个流畅、易用的接口，用于创建 Java 类、接口、枚举、注解、方法、字段等。

## 目录

- [简介](#简介)
- [入门](#入门)
- [基本示例](#基本示例)
  - [Hello World](#hello-world)
  - [创建 Java 文件](#创建-java-文件)
- [使用类型](#使用类型)
  - [类](#类)
  - [接口](#接口)
  - [枚举](#枚举)
  - [注解](#注解)
  - [记录](#记录)
  - [密封类](#密封类)
- [使用方法](#使用方法)
  - [基本方法](#基本方法)
  - [构造函数](#构造函数)
  - [方法参数](#方法参数)
  - [泛型方法](#泛型方法)
  - [异常](#异常)
- [使用字段](#使用字段)
  - [基本字段](#基本字段)
  - [字段初始化器](#字段初始化器)
- [类型名称和引用](#类型名称和引用)
  - [类名](#类名)
  - [基本类型](#基本类型)
  - [数组类型](#数组类型)
  - [参数化类型](#参数化类型)
  - [类型变量](#类型变量)
  - [通配符类型](#通配符类型)
- [控制流](#控制流)
  - [If 语句](#if-语句)
  - [For 循环](#for-循环)
  - [While 循环](#while-循环)
  - [Try-Catch 块](#try-catch-块)
- [高级功能](#高级功能)
  - [静态导入](#静态导入)
  - [Javadoc](#javadoc)
  - [注解](#注解-1)
  - [修饰符](#修饰符)
- [最佳实践](#最佳实践)

## 简介

CodeGentle Java 是一个 Kotlin Multiplatform 库，用于生成 Java 源代码。它提供了一个类型安全、流畅的 API，使得以编程方式生成 Java 代码变得简单。无论您是在编写注解处理器、代码生成器，还是任何其他需要生成 Java 代码的工具，CodeGentle Java 都可以帮助您高效、正确地完成任务。

## 入门

要使用 CodeGentle Java，您需要将其添加为项目的依赖项。然后，您可以使用 API 开始生成 Java 代码。

## 基本示例

### Hello World

让我们从一个简单的 "Hello World" 示例开始：

```kotlin
val spec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.CLASS, "HelloWorld") {
    modifierOp.public()
    modifierOp.final()

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

这会生成以下 Java 代码：

```java
public final class HelloWorld {
    public static void main() {
        System.out.println("Hello, World!");
    }
}
```

### 创建 Java 文件

要创建一个完整的 Java 文件，包括包声明和导入，请使用 `JavaFile` 类：

```kotlin
val typeSpec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.CLASS, "MyClass")
val packageName = "com.example".parseToPackageName()

val javaFile = JavaFile(packageName, typeSpec) {
    addFileComment("这是一个生成的文件。请勿编辑！")
    addStaticImport(ClassName("java.util", "Collections"), "emptyList")
}

// 写入字符串
val javaCode = javaFile.writeToJavaString()

// 或写入文件
// javaFile.writeTo(File("MyClass.java"))
```

这会生成：

```java
// 这是一个生成的文件。请勿编辑！
package com.example;

import static java.util.Collections.emptyList;

class MyClass {
}
```

## 使用类型

### 类

创建一个简单的类：

```kotlin
val classSpec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.CLASS, "MyClass") {
    modifierOp.public()
    
    addJavadoc("这是一个示例类。")
    
    addField(JavaPrimitiveTypeNames.INT, "count") {
        modifierOp.private()
        initializer("0")
    }
    
    addMethod("getCount") {
        modifierOp.public()
        returns(JavaPrimitiveTypeNames.INT.javaRef())
        addStatement("return count")
    }
    
    addMethod("setCount") {
        modifierOp.public()
        addParameter(JavaParameterSpec(JavaPrimitiveTypeNames.INT.javaRef(), "count"))
        addStatement("this.count = count")
    }
}
```

### 接口

创建一个接口：

```kotlin
val interfaceSpec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.INTERFACE, "MyInterface") {
    modifierOp.public()
    
    addMethod("doSomething") {
        addParameter(JavaParameterSpec(ClassName("java.lang", "String").javaRef(), "input"))
        returns(JavaPrimitiveTypeNames.BOOLEAN.javaRef())
    }
}
```

### 枚举

创建一个枚举：

```kotlin
val enumSpec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.ENUM, "Direction") {
    modifierOp.public()
    
    addField(JavaFieldSpec(ClassName("java.lang", "String").javaRef(), "NORTH") {
        modifierOp.public()
        modifierOp.static()
        modifierOp.final()
    })
    
    addField(JavaFieldSpec(ClassName("java.lang", "String").javaRef(), "SOUTH") {
        modifierOp.public()
        modifierOp.static()
        modifierOp.final()
    })
    
    addField(JavaFieldSpec(ClassName("java.lang", "String").javaRef(), "EAST") {
        modifierOp.public()
        modifierOp.static()
        modifierOp.final()
    })
    
    addField(JavaFieldSpec(ClassName("java.lang", "String").javaRef(), "WEST") {
        modifierOp.public()
        modifierOp.static()
        modifierOp.final()
    })
}
```

### 注解

创建一个注解：

```kotlin
val annotationSpec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.ANNOTATION, "MyAnnotation") {
    modifierOp.public()
    
    addMethod("value") {
        returns(ClassName("java.lang", "String").javaRef())
        defaultValue("\"\"")
    }
}
```

### 记录

创建一个记录（Java 16+）：

```kotlin
val recordSpec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.RECORD, "Person") {
    modifierOp.public()
    
    addParameter(JavaParameterSpec(ClassName("java.lang", "String").javaRef(), "name"))
    addParameter(JavaParameterSpec(JavaPrimitiveTypeNames.INT.javaRef(), "age"))
    
    addMethod("greet") {
        modifierOp.public()
        addStatement("return \"Hello, \" + name")
    }
}
```

### 密封类

创建一个密封类（Java 17+）：

```kotlin
val sealedClassSpec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.SEALED_CLASS, "Shape") {
    modifierOp.public()
    
    addMethod("area") {
        modifierOp.public()
        modifierOp.abstract()
        returns(JavaPrimitiveTypeNames.DOUBLE.javaRef())
    }
}

val circleSpec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.NON_SEALED_CLASS, "Circle") {
    modifierOp.public()
    superclass(ClassName("com.example", "Shape").javaRef())
    
    addField(JavaPrimitiveTypeNames.DOUBLE, "radius") {
        modifierOp.private()
        modifierOp.final()
    }
    
    addMethod("area") {
        modifierOp.public()
        modifierOp.override()
        returns(JavaPrimitiveTypeNames.DOUBLE.javaRef())
        addStatement("return Math.PI * radius * radius")
    }
}
```

## 使用方法

### 基本方法

创建一个简单的方法：

```kotlin
val methodSpec = JavaMethodSpec("greet") {
    modifierOp.public()
    returns(ClassName("java.lang", "String").javaRef())
    addStatement("return \"Hello, World!\"")
}
```

### 构造函数

创建一个构造函数：

```kotlin
val constructorSpec = JavaMethodSpec {
    modifierOp.public()
    addParameter(JavaParameterSpec(ClassName("java.lang", "String").javaRef(), "name"))
    addParameter(JavaParameterSpec(JavaPrimitiveTypeNames.INT.javaRef(), "age"))
    addStatement("this.name = name")
    addStatement("this.age = age")
}
```

### 方法参数

向方法添加参数：

```kotlin
val methodSpec = JavaMethodSpec("calculate") {
    modifierOp.public()
    modifierOp.static()
    returns(JavaPrimitiveTypeNames.INT.javaRef())
    
    addParameter(JavaParameterSpec(JavaPrimitiveTypeNames.INT.javaRef(), "a"))
    addParameter(JavaParameterSpec(JavaPrimitiveTypeNames.INT.javaRef(), "b"))
    
    addStatement("return a + b")
}
```

### 泛型方法

创建一个泛型方法：

```kotlin
val methodSpec = JavaMethodSpec("transform") {
    modifierOp.public()
    modifierOp.static()
    
    addTypeVariable(TypeVariableName("T").javaRef())
    addTypeVariable(TypeVariableName("R").javaRef())
    
    addParameter(JavaParameterSpec(TypeVariableName("T").javaRef(), "input"))
    returns(TypeVariableName("R").javaRef())
    
    addStatement("// 将输入转换为输出")
    addStatement("return null")
}
```

### 异常

向方法添加异常：

```kotlin
val methodSpec = JavaMethodSpec("readFile") {
    modifierOp.public()
    
    addException(ClassName("java.io", "IOException").javaRef())
    
    addStatement("// 读取文件实现")
}
```

## 使用字段

### 基本字段

创建一个简单的字段：

```kotlin
val fieldSpec = JavaFieldSpec(JavaPrimitiveTypeNames.INT, "count") {
    modifierOp.private()
}
```

### 字段初始化器

向字段添加初始化器：

```kotlin
val fieldSpec = JavaFieldSpec(JavaPrimitiveTypeNames.INT, "count") {
    modifierOp.private()
    initializer("0")
}

val stringFieldSpec = JavaFieldSpec(ClassName("java.lang", "String").javaRef(), "name") {
    modifierOp.private()
    initializer("%V", "John Doe")
}
```

## 类型名称和引用

### 类名

创建类名：

```kotlin
val stringName = ClassName("java.lang", "String")
val listName = ClassName("java.util", "List")
val mapName = ClassName("java.util", "Map")
```

### 基本类型

使用基本类型：

```kotlin
val intType = JavaPrimitiveTypeNames.INT
val booleanType = JavaPrimitiveTypeNames.BOOLEAN
val doubleType = JavaPrimitiveTypeNames.DOUBLE
```

### 数组类型

创建数组类型：

```kotlin
val stringArrayType = ClassName("java.lang", "String").array()
val intArrayType = JavaPrimitiveTypeNames.INT.array()
```

### 参数化类型

创建参数化类型（泛型）：

```kotlin
val listOfStrings = ClassName("java.util", "List").parameterizedBy(ClassName("java.lang", "String"))
val mapOfStringToInteger = ClassName("java.util", "Map").parameterizedBy(
    ClassName("java.lang", "String"),
    ClassName("java.lang", "Integer")
)
```

### 类型变量

创建类型变量：

```kotlin
val tType = TypeVariableName("T")
val eType = TypeVariableName("E", ClassName("java.lang", "Exception"))
```

### 通配符类型

创建通配符类型：

```kotlin
val wildcardType = WildcardTypeName.subtypeOf(ClassName("java.lang", "Number"))
val listOfWildcards = ClassName("java.util", "List").parameterizedBy(wildcardType)
```

## 控制流

### If 语句

生成 if 语句：

```kotlin
val methodSpec = JavaMethodSpec("checkValue") {
    modifierOp.public()
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

### For 循环

生成 for 循环：

```kotlin
val methodSpec = JavaMethodSpec("printNumbers") {
    modifierOp.public()
    
    beginControlFlow("for (int i = 0; i < 10; i++)") {
        addStatement("System.out.println(i)")
    }
    endControlFlow()
}
```

### While 循环

生成 while 循环：

```kotlin
val methodSpec = JavaMethodSpec("countDown") {
    modifierOp.public()
    addParameter(JavaParameterSpec(JavaPrimitiveTypeNames.INT.javaRef(), "start"))
    
    addStatement("int i = start")
    beginControlFlow("while (i > 0)") {
        addStatement("System.out.println(i)")
        addStatement("i--")
    }
    endControlFlow()
}
```

### Try-Catch 块

生成 try-catch 块：

```kotlin
val methodSpec = JavaMethodSpec("readFile") {
    modifierOp.public()
    addParameter(JavaParameterSpec(ClassName("java.lang", "String").javaRef(), "filename"))
    
    beginControlFlow("try") {
        addStatement("// 读取文件实现")
    }
    nextControlFlow("catch (java.io.IOException e)") {
        addStatement("e.printStackTrace()")
    }
    endControlFlow()
}
```

## 高级功能

### 静态导入

向 Java 文件添加静态导入：

```kotlin
val javaFile = JavaFile(packageName, typeSpec) {
    addStaticImport(ClassName("java.util", "Collections"), "emptyList")
    addStaticImport(ClassName("java.util", "Collections"), "emptyMap")
}
```

### Javadoc

向类型、方法和字段添加 Javadoc：

```kotlin
val classSpec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.CLASS, "MyClass") {
    addJavadoc("这是一个示例类。\n")
    addJavadoc("@author CodeGentle")
    
    addMethod("doSomething") {
        addJavadoc("此方法执行某些操作。\n")
        addJavadoc("@param input 输入字符串\n")
        addJavadoc("@return 如果成功则为 true，否则为 false")
        
        addParameter(JavaParameterSpec(ClassName("java.lang", "String").javaRef(), "input"))
        returns(JavaPrimitiveTypeNames.BOOLEAN.javaRef())
        addStatement("return true")
    }
}
```

### 注解

向类型、方法、字段和参数添加注解：

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
    
    addStatement("// 处理数据")
}
```

### 修饰符

向类型、方法和字段添加修饰符：

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

## 最佳实践

1. **使用构建器模式**：始终使用构建器模式创建规范，因为它提供了流畅的 API 并确保设置了所有必需的属性。

2. **组织代码**：根据功能将代码生成逻辑分离到不同的方法或类中，保持代码组织良好。

3. **使用类型引用**：使用类型引用而不是字符串来确保类型安全，并利用 IDE 功能，如自动完成和重构。

4. **添加注释**：向生成的代码添加注释，解释其目的和用法。

5. **测试生成的代码**：始终测试生成的代码，确保它能够编译并按预期运行。

6. **处理边缘情况**：在生成代码时考虑边缘情况，如空值、空集合和无效输入。

7. **使用有意义的名称**：为类型、方法、字段和参数使用有意义的名称，使生成的代码更具可读性。

8. **遵循 Java 约定**：在生成的代码中遵循 Java 命名约定和编码风格。
