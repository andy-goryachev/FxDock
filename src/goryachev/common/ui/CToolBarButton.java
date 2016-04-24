// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.theme.CTButtonBorder;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.Action;
import javax.swing.Icon;


public class CToolBarButton
	extends CButton
{
	public static final CTButtonBorder BORDER = new CTButtonBorder();
	public static final Dimension SIZE = new Dimension(22,22);
	public static final Insets MARGIN = new Insets(2,2,2,2);
	
	
	public CToolBarButton(Action a)
	{
		super(a);
		init();
	}

	
	public CToolBarButton(String text)
	{
		super(text);
		init();
	}
	
	
	public CToolBarButton(Icon icon)
	{
		super(icon);
		init();
	}
	
	
	public CToolBarButton(String text, String tooltip)
	{
		super(text, tooltip);
		init();
	}
	
	
	public CToolBarButton(String text, Action a)
	{
		super(text, a);
		init();
	}
	
	
	public CToolBarButton(Icon icon, Action a)
	{
		super(icon, a);
		init();
	}
	
	
	public CToolBarButton(Icon icon, String text, Action a)
	{
		super(icon, text, a);
		init();
	}
	
	
	public CToolBarButton(String text, String tooltip, Action a)
	{
		super(text, tooltip, a);
		init();
	}
	
	
//	public CToolBarButton(Action a, String text, String tooltip)
//	{
//		super(a, text, tooltip);
//		init();
//	}
	
	
//	public CToolBarButton(Action a, String tooltip)
//	{
//		super(a);
//		setToolTipText(tooltip);
//		init();
//	}
	
	
	public CToolBarButton(Icon icon, String text, String tooltip, Action a)
	{
		super(icon, text, tooltip, a);
		init();
		setIcon(icon);
	}
	

	private void init()
	{
		setMinimumSize(SIZE);
		setBorder(BORDER);
		setFocusable(false);
		setFocusPainted(false);
		setMargin(MARGIN);
	}
}
