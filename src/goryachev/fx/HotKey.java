// Copyright © 2020-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.Mnemonic;
import javafx.stage.Window;


/**
 * Hot Key.
 * 
 * TODO: 
 * - register with multiple windows
 * - update scene.getAccelerators() when key combination changes
 * - new Mnemonic(node, keyCombination), scene.addMnemonic(m)
 */
public class HotKey
{
	private final String id;
	private KeyCombination key;
	private Runnable action;
	
	
	public HotKey(String id, KeyCombination key)
	{
		this.id = id;
		this.key = key;
	}
	
	
	public void setKeyCombination(KeyCombination k)
	{
		// TODO scan windows for accelerators and mnemonics
	}
	
	
	public boolean isSet()
	{
		return (key != null) && (action != null);
	}
	
	
	public void attach(Node n)
	{
		if(isSet())
		{
			Mnemonic m = new Mnemonic(n, key);
			Scene sc = n.getScene();
			if(sc == null)
			{
				n.sceneProperty().addListener((s,p,c) ->
				{
					if(c != null)
					{
						c.addMnemonic(m);
						// do it once
						n.sceneProperty().removeListener((ChangeListener)this);
					}
				});
			}
			else
			{
				sc.addMnemonic(m);
			}
		}
	}
	
	
	public void attach(Window w)
	{
		if(isSet())
		{
			Scene sc = w.getScene();
			if(sc != null)
			{
				sc.getAccelerators().put(key, action);
			}
		}
	}
	
	
	public void attach(MenuItem m)
	{
		if(key != null)
		{
			m.setAccelerator(key);
		}
		else
		{
			// TODO
		}
	}
}
