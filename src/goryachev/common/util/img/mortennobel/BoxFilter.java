/*
 * Copyright 2013, Morten Nobel-Joergensen
 *
 * License: The BSD 3-Clause License
 * http://opensource.org/licenses/BSD-3-Clause
 */
package goryachev.common.util.img.mortennobel;


/**
 * A box filter (also known as nearest neighbor).
 */
public class BoxFilter
	implements ResampleFilter
{
	public float getSamplingRadius()
	{
		return 0.5f;
	}


	public float apply(float value)
	{
		if(value > -0.5f && value <= 0.5f)
		{
			return 1.0f;
		}
		else
		{
			return 0.0f;
		}
	}


	public String getName()
	{
		return "Box";
	}
}
