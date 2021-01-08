// Copyright © 2012-2021 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import static java.lang.annotation.ElementType.*;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Prevents obfuscation of an annotated field, method, or an entire class.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { CONSTRUCTOR, FIELD, METHOD, TYPE })
public @interface Keep
{
}