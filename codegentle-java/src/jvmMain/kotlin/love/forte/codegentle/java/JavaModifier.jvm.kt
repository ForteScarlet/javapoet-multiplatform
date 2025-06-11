package love.forte.codegentle.java

// javax.lang.model.element.Modifier

// /**
//  * @see javax.lang.model.element.Modifier
//  */
// @Suppress("ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT")
// public actual typealias JavaModifier = javax.lang.model.element.Modifier

public fun javax.lang.model.element.Modifier.toJavaModifier(): JavaModifier = JavaModifier.valueOf(name)

public fun JavaModifier.toJavaxModifier(): javax.lang.model.element.Modifier = javax.lang.model.element.Modifier.valueOf(name)
