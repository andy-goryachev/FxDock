// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import java.awt.Component;


/** Base class for a long operation that takes place inside a BaseDialog.startOperation() */
// TODO perhaps add a hook to intercept cancel() method
public abstract class BaseOperation
{
	/** may return a component, a text (plain or starting with <html>), or a document */
	protected abstract Object executeOperation() throws Exception;
	
	/** override to provide custom viewer for the ongoing process */
	public Component getOperationViewer() { return null; }
	
	//
	
	protected final String name;
	
	
	public BaseOperation(String name)
	{
		this.name = name;
	}
	
	
	public String getName()
	{
		return name;
	}
}
