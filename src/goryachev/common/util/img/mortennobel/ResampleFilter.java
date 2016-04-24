/*
 * Copyright 2013, Morten Nobel-Joergensen
 *
 * License: The BSD 3-Clause License
 * http://opensource.org/licenses/BSD-3-Clause
 */
package goryachev.common.util.img.mortennobel;


public interface ResampleFilter
{
	public float getSamplingRadius();


	public float apply(float v);


	public abstract String getName();
}
