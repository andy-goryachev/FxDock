// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options.edit;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.Theme;
import goryachev.common.ui.options.ColorOption;
import goryachev.common.ui.options.OptionEditor;
import goryachev.common.util.Hex;
import goryachev.common.util.SB;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;


// TODO color picker
public class ColorOptionEditor
	extends OptionEditor<Color>
{
	public final JLabel editor;
	public final JLabel hexField;
	public final JLabel decField;
	public final JPanel panel;


	public ColorOptionEditor(ColorOption op)
	{
		super(op);
		
		editor = new JLabel(" ");
		editor.setOpaque(true);
		
		hexField = new JLabel();
		hexField.setBorder(new CBorder(2));
		
		decField = new JLabel();
		decField.setBorder(new CBorder(2));
		
		panel = new JPanel(new GridLayout(1, 3, 10, 0));
		panel.setBorder(BORDER_THIN);
		panel.add(editor);
		panel.add(hexField);
		panel.add(decField);
		panel.setBackground(Theme.TEXT_BG);
	}
	

	public JComponent getComponent()
	{
		return panel;
	}
	

	public Color getEditorValue()
	{
		return editor.getBackground();
	}

	
	public void setEditorValue(Color x)
	{
		hexField.setText(toHexText(x));
		decField.setText(toDecText(x));
		editor.setBackground(x);
	}
	
	
	public static String toHexText(Color c)
	{
		if(c == null)
		{
			return null;
		}
		
		int a = c.getAlpha();
		int rgb = c.getRGB();
		
		SB sb = new SB();
		sb.a(Hex.toHexString(rgb, 6));
		if(a != 255)
		{
			sb.a("/").a(Hex.toHexString(a, 2));
		}
		return sb.toString();
	}
	
	
	public static String toDecText(Color c)
	{
		if(c == null)
		{
			return null;
		}
		
		int a = c.getAlpha();
		int rgb = c.getRGB();
		
		SB sb = new SB();
		sb.a(c.getRed()).a(", ");
		sb.a(c.getGreen()).a(", ");
		sb.a(c.getBlue());
		if(a != 255)
		{
			sb.a(" alpha=").a(a);
		}
		
		return sb.toString();
	}
	
	
	public String getSearchString()
	{
		return null;
	}
}