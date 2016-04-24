// Copyright (c) 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;


public class Cursors
{
	public static final Cursor DEFAULT = new Cursor(Cursor.DEFAULT_CURSOR);
	public static final Cursor HAND = new Cursor(Cursor.HAND_CURSOR);
	public static final Cursor MOVE = new Cursor(Cursor.MOVE_CURSOR);
	public static final Cursor TEXT = new Cursor(Cursor.TEXT_CURSOR);
	public static final Cursor CROSSHAIR = new Cursor(Cursor.CROSSHAIR_CURSOR);
	public static final Cursor NE = new Cursor(Cursor.NE_RESIZE_CURSOR);
	public static final Cursor NW = new Cursor(Cursor.NW_RESIZE_CURSOR);
	public static final Cursor SE = new Cursor(Cursor.SE_RESIZE_CURSOR);
	public static final Cursor SW = new Cursor(Cursor.SW_RESIZE_CURSOR);
	public static final Cursor N = new Cursor(Cursor.N_RESIZE_CURSOR);
	public static final Cursor S = new Cursor(Cursor.S_RESIZE_CURSOR);
	public static final Cursor E = new Cursor(Cursor.E_RESIZE_CURSOR);
	public static final Cursor W = new Cursor(Cursor.W_RESIZE_CURSOR);
	
	
	public static Cursor emptyCursor()
	{
		BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		return Toolkit.getDefaultToolkit().createCustomCursor(img, new Point(0, 0), "empty");
	}
}
