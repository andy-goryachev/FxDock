// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;


/**
 * Keeps track of the user choice when closing a window, 
 * possibly multiple windows.
 */
public class OnWindowClosing
{
	public enum Choice
	{
		CANCELLED,
		DISCARD_ALL,
		SAVE_ALL
	}
	
	//
	
	private final boolean multiple;
	private Object choice;
	
	
	public OnWindowClosing(boolean closingMultipleWindows)
	{
		this.multiple = closingMultipleWindows;
	}
	
	
	/** indicates whether the operation involves multiple windows */
	public boolean isClosingMultipleWindows()
	{
		return multiple;
	}
	
	
	/** the choice may in fact be application-specific, so here is a method to set it */
	public void setChoice(Object choice)
	{
		this.choice = choice;
	}
	
	
	public Object getChoice()
	{
		return choice;
	}
	
	
	/** sets the Save All user choice */
	public void setSaveAll()
	{
		setChoice(Choice.SAVE_ALL);
	}
	
	
	/** sets the Discard All user choice */
	public void setDiscardAll()
	{
		setChoice(Choice.DISCARD_ALL);
	}
	
	
	/** sets cancelled state */
	public void setCancelled()
	{
		setChoice(Choice.CANCELLED);
	}
	
	
	public boolean isSaveAll()
	{
		return choice == Choice.SAVE_ALL;
	}
	
	
	public boolean isDiscardAll()
	{
		return choice == Choice.DISCARD_ALL;
	}
	
	
	public boolean isCancelled()
	{
		return choice == Choice.CANCELLED;
	}
}
