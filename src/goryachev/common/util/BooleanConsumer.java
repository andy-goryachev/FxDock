// Copyright © 2025-2026 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/**
 * Consumer<boolean>.
 */
@FunctionalInterface
public interface BooleanConsumer
{
	public void acceptBoolean(boolean value);
}
