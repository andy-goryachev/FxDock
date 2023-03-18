// Copyright © 2021-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/**
 * Functional interface with three arguments, similar to Consumer and BiConsumer.
 */
@FunctionalInterface
public interface TriConsumer<A,B,C>
{
	public void accept(A a, B b, C c);
}
