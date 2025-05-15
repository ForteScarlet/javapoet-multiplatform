The java source class:
```Java
public final class HelloWorld {
    public static void main() {
        System.out.println("Hello, World!");
    }
}        
```

The CodeGentle code:

```Kotlin
val spec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.CLASS, "HelloWorld") {
    modifierOp.public()
    modifierOp.final()

    addMethod("main") {
        modifiers {
            public()
            static()
        }

        addStatement("System.out.println(%V)") {
            string("Hello, World!")
        }
    }
}
```
