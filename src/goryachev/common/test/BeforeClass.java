// Copyright © 2013-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.test;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/** marks static code to be executed once before each class containing @Test */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BeforeClass
{
}