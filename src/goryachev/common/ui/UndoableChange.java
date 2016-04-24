// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;


public interface UndoableChange
{
	public void change();
	
	public void undoChange();
}
