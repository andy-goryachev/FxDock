// Copyright (c) 2014-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import java.awt.Image;


@SuppressWarnings("unused")
public class MultiCoreImageScaler
{
	private final Image image;
	private final int width;
	private final int height;
	private final int cores;


	public MultiCoreImageScaler(Image image, int width, int height, int cores)
	{
		this.image = image;
		this.width = width;
		this.height = height;
		this.cores = cores;
	}


	public Image scale()
	{
		// TODO
		return null;
	}
}
