// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.dialogs.options;
import goryachev.common.ui.CButton;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.CTextField;
import goryachev.common.ui.Theme;
import goryachev.common.ui.ThemeKey;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;
import goryachev.common.util.CSorter;
import goryachev.common.util.Hex;
import goryachev.common.util.SB;
import goryachev.common.util.TXT;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;


public class FontColorEditorPanel
	extends CPanel
{
	public FontColorEditorPanel()
	{
		border();
		setGaps(10, 5);
		addColumns
		(
			CPanel.PREFERRED,
			CPanel.FILL,
			CPanel.PREFERRED, 
			CPanel.PREFERRED
		);
	}
	
	
	public void createEditors(boolean font, Features fs, boolean header)
	{
		CMap<String,Integer> m = null;
		for(int i=fs.size()-1; i>=0; --i)
		{
			String name = null;
			if(font)
			{
				if(fs.isFont(i))
				{
					name = fs.getName(i);
				}
			}
			else
			{
				if(fs.isColor(i))
				{
					name = fs.getName(i);
				}
			}
			
			if(name != null)
			{
				if(m == null)
				{
					m = new CMap();
				}
				m.put(name, i);
			}
		}
		
		if(m != null)
		{
			CList<String> names = new CList(m.keySet());
			CSorter.sort(names);
			
			if(header)
			{
				if(font)
				{
					nextRow();
					//row(1, header(TXT.get("FontColorEditorPanel.header: font", "Font")));
					row(0, label(" "));
				}
				else
				{
//					nextRow();
//					row(2, header(TXT.get("FontColorEditorPanel.header: foreground color", "Foreground")));
//					row(3, header(TXT.get("FontColorEditorPanel.header: background color", "Background")));
				}
			}
			
			for(String name: names)
			{
				ThemeKey k = fs.getTFeature(m.get(name));
				if(font)
				{
					createFontEditor(k, name);
				}
				else
				{
					createColorEditor(name, k);
				}
			}
		}
	}
	
	
	protected void createFontEditor(ThemeKey k, String name)
	{
		Font f = Theme.getFont(k);
		SB sb = new SB();
		sb.a(f.getFamily());
		sb.a(", ");
		sb.a(f.getSize());
			
		CTextField t = new CTextField(sb.toString());
		t.setEditable(false);
		t.setFont(f);
		
		// FIX large font creates large buttons
		
		nextRow();
		row(0, label(name));
		row(1, t);
		row(2, new CButton("- Smaller")); // TODO tool tip, action
		row(3, new CButton("+ Larger"));
	}
	
	
	protected void createColorEditor(String name, ThemeKey k)
	{
		Color c = Theme.getColor(k);
		
//		String s = TXT.get
//		(
//			"FontColorEditorPanel",
//			"rgb=({0},{1},{2}); web={3}",
//			c.getRed(),
//			c.getGreen(),
//			c.getBlue(),
//			"#" + Hex.toHexString(c.getRGB() & 0xffffff, 6)
//		);
		
		String s =
			"#" + Hex.toHexString(c.getRGB() & 0xffffff, 6) +
			" (" +
			c.getRed() + ", " +
			c.getGreen() + ", " +
			c.getBlue() + ")";
		
		CTextField t = new CTextField(s); // FIX tool tip, action
		t.setEditable(false);
		//t.setForeground(c);
		
		CTextField ct = new CTextField(); // FIX tool tip, action
		ct.setEditable(false);
		ct.setBackground(c);
		ct.setPreferredSize(new Dimension(70, -1));
		
		nextRow();
		row(0, label(name));
		row(1, 2, t);
		row(3, ct); 
	}

	
	protected JLabel header(String name)
	{
		JLabel t = new JLabel(name);
		t.setFont(Theme.boldFont());
		return t;
	}
}
