// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.TXT;
import java.util.ArrayList;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;


// flexible undoable operation which can be built from a number of UndoableChanges
// or, override change() and undoChange() to make it a single change
public class UndoableOperation
	implements UndoableEdit, UndoableChange
{
	private String name;
	private ArrayList<UndoableChange> list;
	private boolean performed;
	
	
	public UndoableOperation(String name)
	{
		this.name = name;
	}
	
	
	public void add(UndoableChange change)
	{
		if(list == null)
		{
			list = new ArrayList();
		}
		
		list.add(change);
	}
	
	
	public boolean addEdit(UndoableEdit anEdit)
	{
		return false;
	}


	public boolean canRedo()
	{
		return !performed;
	}


	public boolean canUndo()
	{
		return performed;
	}


	public void die()
	{
	}


	public boolean replaceEdit(UndoableEdit anEdit)
	{
		return false;
	}


	public String getPresentationName()
	{
		return name;
	}


	public String getRedoPresentationName()
	{
		return TXT.get("UndoableOperation.redo presentation name","Redo {0}",name);
	}


	public String getUndoPresentationName()
	{
		return TXT.get("UndoableOperation.undo presentation name","Undo {0}",name);
	}


	public boolean isSignificant()
	{
		return true;
	}


	public void redo() throws CannotRedoException
	{
		if(canRedo())
		{
			change();
			performed = true;
		}
		else
		{
			throw new CannotRedoException();
		}
	}


	public void undo() throws CannotUndoException
	{
		if(canUndo())
		{
			undoChange();
			performed = false;
		}
		else
		{
			throw new CannotUndoException();
		}
	}
	
	
	public void change()
	{
		if(list != null)
		{
			for(int i=0; i<list.size(); i++)
			{
				list.get(i).change();
			}
		}
	}
	
	
	public void undoChange()
	{
		if(list != null)
		{
			for(int i=list.size()-1; i>=0; --i)
			{
				list.get(i).undoChange();
			}
		}
	}
	
	
	public boolean hasChanges()
	{
		if(list == null)
		{
			// overriden?
			return (getClass() != UndoableOperation.class);
		}
		else
		{
			return (list.size() > 0);
		}
	}
}
