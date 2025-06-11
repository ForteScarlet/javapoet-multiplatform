# CodeGentle Java

CodeGentle Java 是一个 Kotlin 多平台库，用于生成 Java 源代码。它提供了丰富的、类型安全的 API，用于创建 Java 类、接口、枚举和其他 Java 语言结构。该库受到 [JavaPoet](https://github.com/square/javapoet) 的启发，但使用 Kotlin 实现并支持多平台。

## 特性

### 跨平台支持

CodeGentle Java 支持多个平台：
- JVM
- JavaScript
- WebAssembly
- 原生平台（macOS、iOS、Linux、Android、Windows）

### Java 代码生成

该库提供了全面的 API 用于生成 Java 代码：

- **Java 类型**：生成类、接口、枚举、注解、记录和密封类型
- **Java 成员**：生成字段、方法、构造函数和初始化块
- **Java 修饰符**：应用修饰符，如 public、private、static、final 等
- **Java 注解**：为类型、字段、方法和参数应用注解
- **Java 注释**：为生成的代码添加 Javadoc 和其他注释
- **Java 导入**：管理导入，包括静态导入

## 安装

将依赖项添加到您的项目中：

```kotlin
// build.gradle.kts
dependencies {
    implementation("love.forte:codegentle-java:VERSION")
}
```

## 使用示例

### 生成简单类

```kotlin
import love.forte.codegentle.common.naming.parseToPackageName
import love.forte.codegentle.java.*
import love.forte.codegentle.java.spec.*
import love.forte.codegentle.java.writer.writeToJavaString

// 创建一个简单的类
val typeSpec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.CLASS, "MyClass") {
    addModifiers(JavaModifier.PUBLIC)
}

// 创建一个包含该类的 Java 文件
val javaFile = JavaFile("com.example".parseToPackageName(), typeSpec)

// 生成 Java 代码
val javaCode = javaFile.writeToJavaString()

// 生成的代码将是：
// package com.example;
//
// public class MyClass {
// }
```

### 生成 Hello World 程序

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

// 创建 main 方法
val mainMethod = JavaMethodSpec("main") {
    addModifiers(JavaModifier.PUBLIC, JavaModifier.STATIC)
    returns(JavaPrimitiveTypeNames.VOID)
    addParameter(JavaParameterSpec(ArrayTypeName(JavaClassNames.STRING.javaRef()).javaRef(), "args"))
    addStatement("%V.out.println(%V)") {
        emitType(ClassName("java.lang".parseToPackageName(), "System"))
        emitString("Hello, World!")
    }
}

// 创建包含 main 方法的类
val helloWorldClass = JavaSimpleTypeSpec(JavaTypeSpec.Kind.CLASS, "HelloWorld") {
    addModifiers(JavaModifier.PUBLIC)
    addMethod(mainMethod)
}

// 创建 Java 文件
val javaFile = JavaFile("com.example".parseToPackageName(), helloWorldClass)

// 生成 Java 代码
val javaCode = javaFile.writeToJavaString()

// 生成的代码将是：
// package com.example;
//
// public class HelloWorld {
//     public static void main(String[] args) {
//         System.out.println("Hello, World!");
//     }
// }
```

### 生成带有字段和方法的类

```kotlin
import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.common.naming.parseToPackageName
import love.forte.codegentle.java.*
import love.forte.codegentle.java.naming.JavaPrimitiveTypeNames
import love.forte.codegentle.java.ref.javaRef
import love.forte.codegentle.java.spec.*
import love.forte.codegentle.java.writer.writeToJavaString

// 创建字段
val nameField = JavaFieldSpec(JavaClassNames.STRING.javaRef(), "name") {
    addModifiers(JavaModifier.PRIVATE)
}

val ageField = JavaFieldSpec(JavaPrimitiveTypeNames.INT.javaRef(), "age") {
    addModifiers(JavaModifier.PRIVATE)
}

// 创建构造函数
val constructor = JavaMethodSpec {
    addModifiers(JavaModifier.PUBLIC)
    addParameter(JavaParameterSpec(JavaClassNames.STRING.javaRef(), "name"))
    addParameter(JavaParameterSpec(JavaPrimitiveTypeNames.INT.javaRef(), "age"))
    addStatement("this.name = name")
    addStatement("this.age = age")
}

// 创建 getter 方法
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

// 创建类
val personClass = JavaSimpleTypeSpec(JavaTypeSpec.Kind.CLASS, "Person") {
    addModifiers(JavaModifier.PUBLIC)
    addJavadoc("表示一个人的类。")
    addField(nameField)
    addField(ageField)
    addMethod(constructor)
    addMethod(getNameMethod)
    addMethod(getAgeMethod)
}

// 创建 Java 文件
val javaFile = JavaFile("com.example".parseToPackageName(), personClass)

// 生成 Java 代码
val javaCode = javaFile.writeToJavaString()

// 生成的代码将是：
// package com.example;
//
// /**
//  * 表示一个人的类。
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

### 生成接口

```kotlin
import love.forte.codegentle.common.naming.parseToPackageName
import love.forte.codegentle.java.*
import love.forte.codegentle.java.naming.JavaClassNames
import love.forte.codegentle.java.naming.JavaPrimitiveTypeNames
import love.forte.codegentle.java.ref.javaRef
import love.forte.codegentle.java.spec.*
import love.forte.codegentle.java.writer.writeToJavaString

// 创建方法规范
val saveMethod = JavaMethodSpec("save") {
    addParameter(JavaParameterSpec(JavaClassNames.OBJECT.javaRef(), "entity"))
    returns(JavaPrimitiveTypeNames.BOOLEAN)
}

val findByIdMethod = JavaMethodSpec("findById") {
    addParameter(JavaParameterSpec(JavaPrimitiveTypeNames.LONG.javaRef(), "id"))
    returns(JavaClassNames.OBJECT)
}

// 创建接口
val repositoryInterface = JavaSimpleTypeSpec(JavaTypeSpec.Kind.INTERFACE, "Repository") {
    addModifiers(JavaModifier.PUBLIC)
    addJavadoc("通用仓库接口。")
    addMethod(saveMethod)
    addMethod(findByIdMethod)
}

// 创建 Java 文件
val javaFile = JavaFile("com.example.repository".parseToPackageName(), repositoryInterface)

// 生成 Java 代码
val javaCode = javaFile.writeToJavaString()

// 生成的代码将是：
// package com.example.repository;
//
// /**
//  * 通用仓库接口。
//  */
// public interface Repository {
//     boolean save(Object entity);
//
//     Object findById(long id);
// }
```

### 生成枚举

```kotlin
import love.forte.codegentle.common.naming.parseToPackageName
import love.forte.codegentle.java.*
import love.forte.codegentle.java.spec.*
import love.forte.codegentle.java.writer.writeToJavaString

// 创建枚举
val dayEnum = JavaEnumTypeSpec("Day") {
    addModifiers(JavaModifier.PUBLIC)
    addJavadoc("一周的天数。")
    
    // 添加枚举常量
    addEnumConstant("MONDAY")
    addEnumConstant("TUESDAY")
    addEnumConstant("WEDNESDAY")
    addEnumConstant("THURSDAY")
    addEnumConstant("FRIDAY")
    addEnumConstant("SATURDAY")
    addEnumConstant("SUNDAY")
}

// 创建 Java 文件
val javaFile = JavaFile("com.example".parseToPackageName(), dayEnum)

// 生成 Java 代码
val javaCode = javaFile.writeToJavaString()

// 生成的代码将是：
// package com.example;
//
// /**
//  * 一周的天数。
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

## API 文档

### 核心包

- `love.forte.codegentle.java` - Java 代码生成的核心类
- `love.forte.codegentle.java.spec` - 表示 Java 语言结构的类
- `love.forte.codegentle.java.naming` - 表示 Java 类型名称的类
- `love.forte.codegentle.java.ref` - 表示对 Java 类型和注解的引用的类
- `love.forte.codegentle.java.writer` - 用于编写生成的 Java 代码的类

### 关键接口和类

- `JavaFile` - 表示 Java 源文件
- `JavaTypeSpec` - Java 类型规范的基本接口（类、接口、枚举等）
- `JavaFieldSpec` - 表示 Java 类型中的字段
- `JavaMethodSpec` - 表示 Java 类型中的方法或构造函数
- `JavaParameterSpec` - 表示方法或构造函数中的参数
- `JavaModifier` - Java 修饰符的枚举（public、private、static 等）
- `JavaCodeWriter` - 用于编写 Java 代码的接口

### 类型规范

CodeGentle Java 为不同类型的 Java 类型提供了 `JavaTypeSpec` 的几种实现：

- `JavaSimpleTypeSpec` - 用于类和接口
- `JavaEnumTypeSpec` - 用于枚举
- `JavaAnnotationTypeSpec` - 用于注解类型
- `JavaRecordTypeSpec` - 用于记录（Java 16+）
- `JavaSealedTypeSpec` - 用于密封类和接口（Java 17+）
- `JavaNonSealedTypeSpec` - 用于非密封类和接口（Java 17+）

### 类型名称

CodeGentle Java 提供了表示不同类型的 Java 类型名称的类：

- `JavaPrimitiveTypeNames` - Java 原始类型的常量（int、boolean 等）
- `JavaClassNames` - 常见 Java 类的常量（String、Object 等）

## 许可证

该库使用 [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0) 许可证。
