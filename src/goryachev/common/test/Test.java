// Copyright © 2013-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.common.test;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Test
{
	static class NoThrowable
		extends Throwable
	{
		private NoThrowable()
		{
		}
	}
	
	
	//


	public Class<? extends Throwable> expected() default NoThrowable.class;
}