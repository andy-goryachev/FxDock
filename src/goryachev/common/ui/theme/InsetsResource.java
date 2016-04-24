// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;

import java.awt.Insets;
import javax.swing.plaf.UIResource;


public class InsetsResource extends Insets implements UIResource
{
	public InsetsResource(int top, int left, int bottom, int right)
	{
		super(top, left, bottom, right);
	}
}