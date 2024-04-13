// Copyright © 2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;


/**
 * User choice for saving/discarding modified content in multiple windows
 * when exiting the application.
 */
public enum ShutdownChoice
{
	CANCEL,
	CONTINUE,
	DISCARD_ALL,
	SAVE_ALL,
	UNDEFINED;
}