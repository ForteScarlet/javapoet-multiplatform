# CodeGentle Common

CodeGentle Common 是一个 Kotlin 多平台库，为跨不同平台的代码生成提供基础。它提供了一套丰富的工具和抽象，用于表示和操作代码元素、类型名称以及生成代码的写入。

## 功能特性

### 跨平台支持

CodeGentle Common 支持多个平台：
- JVM
- JavaScript
- WebAssembly
- 原生平台（macOS、iOS、Linux、Android、Windows）

### 代码表示

该库提供了一套全面的 API 用于表示代码元素：

#### 代码值和部分

```kotlin
// 创建一个简单的代码值
val simpleCode = CodeValue("val name = %V", CodePart.string("John"))

// 使用构建器创建更复杂的代码值
val complexCode = CodeValue {
    add("class User(")
    add("val name: String,")
    add("val age: Int")
    add(")")
}

// 添加语句和控制流
val functionCode = CodeValue {
    addStatement("fun greet(name: String) {")
    withIndent {
        addStatement("println(\"Hello, \$name!\")")
    }
    addStatement("}")
}
```

### 类型名称

该库提供了表示不同类型名称的类：

```kotlin
// 创建一个类名
val userClass = ClassName("com.example", "User")

// 创建一个参数化类型名称
val listOfUsers = ParameterizedTypeName(
    ClassName("kotlin.collections", "List"),
    userClass
)

// 创建一个数组类型名称
val arrayOfUsers = ArrayTypeName(userClass)

// 创建一个类型变量名称
val typeVar = TypeVariableName("T")

// 创建一个通配符类型名称
val wildcardType = LowerWildcardTypeName(userClass.ref(EmptyTypeNameRefStatus))
```

### 代码写入

该库提供了一个灵活的 API 用于写入代码：

```kotlin
// 从您的实现中获取 CodeWriter 实例
val codeWriter = /* ... */

// 使用缩进写入代码
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

// 获取生成的代码
val generatedCode = codeWriter.toString()
```

### Unicode 支持

该库提供了处理 Unicode 码点的工具：

```kotlin
import love.forte.codegentle.common.codepoint.*

// 从字符串获取码点
val codePoint = "A".codePointAt(0)

// 该库提供了检查码点属性的函数
// 如大小写、字符计数和 Unicode 类别

// 示例：将码点附加到 StringBuilder
val sb = StringBuilder()
sb.appendCodePoint(codePoint)

// 示例：将码点转换为字符串
val str = codePoint.toString()
```

## 安装

将依赖项添加到您的项目中：

```kotlin
// build.gradle.kts
dependencies {
    implementation("love.forte:codegentle-common:VERSION")
}
```

## 使用示例

### 生成一个简单的类

```kotlin
fun generateUserClass(): String {
    val codeWriter = CodeWriter.create()

    codeWriter.emit("package com.example")
    codeWriter.emitNewLine()

    codeWriter.emit("/**")
    codeWriter.emit(" * 表示系统中的用户。")
    codeWriter.emit(" */")
    codeWriter.emit("class User(")
    codeWriter.withIndent {
        emit("val name: String,")
        emit("val age: Int")
    }
    codeWriter.emit(") {")
    codeWriter.withIndent {
        emit("/**")
        emit(" * 向用户问候。")
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

### 使用类型名称

```kotlin
fun generateRepositoryInterface(): String {
    val codeWriter = CodeWriter.create()

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
    codeWriter.emit(" * 用于管理 User 实体的仓库。")
    codeWriter.emit(" */")
    codeWriter.emit("interface UserRepository : ")
    codeWriter.emit(repositoryClass)

    return codeWriter.toString()
}
```

### 构建复杂的代码值

```kotlin
fun generateServiceClass(): CodeValue {
    return CodeValue {
        addStatement("package com.example.service")
        addStatement("")
        addStatement("import com.example.repository.UserRepository")
        addStatement("")
        addStatement("/**")
        addStatement(" * 用于管理用户的服务。")
        addStatement(" */")
        addStatement("class UserService(private val repository: UserRepository) {")
        withIndent {
            addStatement("/**")
            addStatement(" * 通过 ID 查找用户。")
            addStatement(" */")
            addStatement("fun findById(id: Long): User? {")
            withIndent {
                addStatement("return repository.findById(id)")
            }
            addStatement("}")

            addStatement("")

            addStatement("/**")
            addStatement(" * 创建一个新用户。")
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

## API 文档

### 核心包

- `love.forte.codegentle.common.code` - 表示代码元素的类
- `love.forte.codegentle.common.codepoint` - 处理 Unicode 码点的工具
- `love.forte.codegentle.common.naming` - 表示类型名称的类
- `love.forte.codegentle.common.ref` - 表示类型和注解引用的类
- `love.forte.codegentle.common.writer` - 写入生成代码的类

### 关键接口和类

- `CodeValue` - 表示代码中的值
- `CodePart` - 表示代码的部分
- `TypeName` - 类型名称的基础接口
- `ClassName` - 表示类名
- `PackageName` - 表示包名
- `TypeRef` - 表示对类型的引用
- `CodeWriter` - 写入代码的接口

## 许可证

该库使用 [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0) 许可证。
