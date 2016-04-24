/*
 * Copyright 2013, Morten Nobel-Joergensen
 *
 * License: The BSD 3-Clause License
 * http://opensource.org/licenses/BSD-3-Clause
 */
package goryachev.common.util.img.mortennobel;


/**
 * @author Heinz Doerr
 */
public class BiCubicHighFreqResponse
	extends BiCubicFilter
{
	public BiCubicHighFreqResponse()
	{
		super(-1.f);
	}


	public String getName()
	{
		return "BiCubicHighFreqResponse";
	}
}