// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options.edit;
import goryachev.common.ui.table.ZColumnHandler;
import goryachev.common.ui.table.ZModel;
import goryachev.common.util.CKit;
import goryachev.common.util.TXT;
import javax.swing.KeyStroke;


public class KeyBindingsTableModel
	extends ZModel<KeyBindingEntry>
{
	public KeyBindingsTableModel()
	{
		addColumn(TXT.get("KeyBindingsTableModel.column.key binding command", "Command"), new ZColumnHandler<KeyBindingEntry>()
		{
			public Object getCellValue(KeyBindingEntry x) { return x.getCommand(); }
		});
		
		addColumn(TXT.get("KeyBindingsTableModel.column.where this key is active", "Where"), new ZColumnHandler<KeyBindingEntry>()
		{
			public Object getCellValue(KeyBindingEntry x) { return x.accelerator.getSubsystem(); }
		});
		
		addColumn(TXT.get("KeyBindingsTableModel.column.name of a keyboard key", "Key"), new ZColumnHandler<KeyBindingEntry>()
		{
			public Object getCellValue(KeyBindingEntry x) { return x.getKeyName(); }
		});
	}
	

	public KeyBindingEntry findByKeyStroke(KeyStroke k)
	{
		if(k != null)
		{
			for(KeyBindingEntry en: this)
			{
				if(en.getKey() != null)
				{
					if(CKit.equals(en.getKey(), k))
					{
						return en;
					}
				}
			}
		}
		return null;
	}
}
