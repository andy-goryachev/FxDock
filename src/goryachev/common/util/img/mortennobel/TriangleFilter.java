/*
 * Copyright 2013, Morten Nobel-Joergensen
 *
 * License: The BSD 3-Clause License
 * http://opensource.org/licenses/BSD-3-Clause
 */
package goryachev.common.util.img.mortennobel;


/**
 * A triangle filter (also known as linear or bilinear filter).
 */
public class TriangleFilter
	implements ResampleFilter
{
	public float getSamplingRadius()
	{
		return 1.0f;
	}


	public float apply(float value)
	{
		if(value < 0.0f)
		{
			value = -value;
		}
		if(value < 1.0f)
		{
			return 1.0f - value;
		}
		else
		{
			return 0.0f;
		}
	}


	public String getName()
	{
		return "Triangle";
	}
}
