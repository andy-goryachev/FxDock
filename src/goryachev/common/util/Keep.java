// Copyright © 2012-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import static java.lang.annotation.ElementType.*;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Prevents obfuscation of an annotated constructor, field, method, or class name.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { CONSTRUCTOR, FIELD, METHOD, TYPE })
public @interface Keep
{
}