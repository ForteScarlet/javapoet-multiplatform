# CodeGentle Kotlin 模块开发计划

## 简介

CodeGentle Kotlin 模块旨在提供一个完整的 Kotlin 代码生成 API，可以完全替代 `kotlinpoet`，并保持与 `codegentle-common` 和 `codegentle-java` 模块一致的风格和结构。本文档概述了开发 CodeGentle Kotlin 模块的计划和步骤。

## 目标

1. 创建一个完整的 Kotlin 代码生成 API
2. 保持与 `codegentle-common` 和 `codegentle-java` 模块一致的风格和结构
3. 提供与 `kotlinpoet` 相同的功能，但更加符合 CodeGentle 的设计理念
4. 支持 Kotlin 特有的语言特性，如扩展函数、属性委托、协程等

## 项目结构

参照 `codegentle-java` 模块的结构，`codegentle-kotlin` 模块应包含以下主要包和文件：

### 核心包和文件

- `love.forte.codegentle.kotlin`
  - `InternalKotlinCodeGentleApi.kt` - 内部 API 注解
  - `KotlinModifier.kt` - Kotlin 修饰符枚举（已存在）
  - `KotlinFile.kt` - Kotlin 文件生成
  - `KotlinCodeValue.kt` - Kotlin 代码值
  - `NameAllocator.kt` - 名称分配器
  - `Util.kt` - 工具函数（已存在，但为空）

### 规范包 (spec)

- `love.forte.codegentle.kotlin.spec`
  - `CodeGentleKotlinSpecImplementation.kt` - Kotlin 规范实现标记
  - `KotlinTypeSpec.kt` - Kotlin 类型规范接口
  - `KotlinSimpleTypeSpec.kt` - 简单 Kotlin 类型（类、接口等）
  - `KotlinFunctionSpec.kt` - Kotlin 函数规范
  - `KotlinPropertySpec.kt` - Kotlin 属性规范
  - `KotlinParameterSpec.kt` - Kotlin 参数规范
  - `KotlinTypeAliasSpec.kt` - Kotlin 类型别名规范
  - `KotlinObjectSpec.kt` - Kotlin 对象规范
  - `KotlinCompanionObjectSpec.kt` - Kotlin 伴生对象规范
  - `KotlinEnumTypeSpec.kt` - Kotlin 枚举规范
  - `KotlinAnnotationTypeSpec.kt` - Kotlin 注解规范
  - `KotlinSealedTypeSpec.kt` - Kotlin 密封类规范
  - `KotlinValueClassSpec.kt` - Kotlin 值类规范
  - `KotlinDataClassSpec.kt` - Kotlin 数据类规范
  - `KotlinFunInterfaceSpec.kt` - Kotlin 函数式接口规范

### 规范实现包 (spec.internal)

- `love.forte.codegentle.kotlin.spec.internal`
  - `KotlinSimpleTypeSpecImpl.kt` - 简单 Kotlin 类型实现
  - `KotlinFunctionSpecImpl.kt` - Kotlin 函数规范实现
  - `KotlinPropertySpecImpl.kt` - Kotlin 属性规范实现
  - `KotlinParameterSpecImpl.kt` - Kotlin 参数规范实现
  - `KotlinTypeAliasSpecImpl.kt` - Kotlin 类型别名规范实现
  - `KotlinObjectSpecImpl.kt` - Kotlin 对象规范实现
  - `KotlinCompanionObjectSpecImpl.kt` - Kotlin 伴生对象规范实现
  - `KotlinEnumTypeSpecImpl.kt` - Kotlin 枚举规范实现
  - `KotlinAnnotationTypeSpecImpl.kt` - Kotlin 注解规范实现
  - `KotlinSealedTypeSpecImpl.kt` - Kotlin 密封类规范实现
  - `KotlinValueClassSpecImpl.kt` - Kotlin 值类规范实现
  - `KotlinDataClassSpecImpl.kt` - Kotlin 数据类规范实现
  - `KotlinFunInterfaceSpecImpl.kt` - Kotlin 函数式接口规范实现

### 引用包 (ref)

- `love.forte.codegentle.kotlin.ref`
  - `KotlinTypeNameRefStatus.kt` - Kotlin 类型名称引用状态（已存在）
  - `KotlinTypeNameRefStatusImpl.kt` - Kotlin 类型名称引用状态实现

### 写入器包 (writer)

- `love.forte.codegentle.kotlin.writer`
  - `KotlinCodeEmitter.kt` - Kotlin 代码发射器
  - `KotlinCodeWriter.kt` - Kotlin 代码写入器
  - `KotlinImport.kt` - Kotlin 导入
  - `KotlinCodeValueEmitOption.kt` - Kotlin 代码值发射选项
  - `KotlinTypeNameEmitOption.kt` - Kotlin 类型名称发射选项
  - `KotlinTypeRefEmitOption.kt` - Kotlin 类型引用发射选项

### 命名包 (naming)

- `love.forte.codegentle.kotlin.naming`
  - `KotlinClassName.kt` - Kotlin 类名
  - `KotlinTypeName.kt` - Kotlin 类型名称

## 开发步骤

1. **基础设施**
   - 完成 `InternalKotlinCodeGentleApi.kt`
   - 完善 `KotlinModifier.kt`（已存在）
   - 完善 `Util.kt`（已存在但为空）
   - 实现 `KotlinTypeNameRefStatusImpl.kt`

2. **核心类型**
   - 实现 `KotlinTypeName` 和相关类
   - 实现 `KotlinFile`

3. **规范接口**
   - 实现 `KotlinTypeSpec` 及其变体
   - 实现 `KotlinFunctionSpec`
   - 实现 `KotlinPropertySpec`
   - 实现 `KotlinParameterSpec`
   - 实现其他规范接口

4. **规范实现**
   - 实现所有规范接口的具体实现类

5. **写入器**
   - 实现 `KotlinCodeWriter` 和相关类

6. **测试**
   - 为所有组件编写单元测试
   - 创建集成测试，验证生成的代码

7. **文档**
   - 创建 API 文档
   - 创建示例文档（类似于 `Example.md` 和 `Example_CN.md`）

## Kotlin 特有功能

除了与 Java 模块类似的功能外，Kotlin 模块还需要支持以下 Kotlin 特有的功能：

1. **扩展函数和属性**
   - 支持生成扩展函数和扩展属性

2. **属性委托**
   - 支持生成使用委托的属性（如 `by lazy`）

3. **协程**
   - 支持生成挂起函数和协程构建器

4. **解构声明**
   - 支持生成 `component` 函数和解构声明

5. **内联函数**
   - 支持生成内联函数和 reified 类型参数

6. **函数式接口**
   - 支持生成函数式接口（`fun interface`）

7. **值类**
   - 支持生成值类（`value class`）

8. **多平台代码**
   - 支持生成多平台特定的代码（`expect`/`actual`）

## 时间线

1. **第一阶段**：基础设施和核心类型（1-2周）
2. **第二阶段**：规范接口和实现（2-3周）
3. **第三阶段**：写入器和测试（1-2周）
4. **第四阶段**：文档和示例（1周）

## 注意事项

- 保持与 `codegentle-common` 和 `codegentle-java` 模块一致的 API 设计
- 确保生成的代码符合 Kotlin 编码规范
- 考虑 Kotlin 多平台的兼容性
- 提供详细的文档和示例
