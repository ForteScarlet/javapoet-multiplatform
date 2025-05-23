package love.forte.codegentle.java.spec

import love.forte.codegentle.common.spec.Spec
import love.forte.codegentle.java.writer.JavaCodeEmitter


public sealed interface JavaSpec : Spec, JavaCodeEmitter
