// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package demo.dock;
import goryachev.fx.CPane;


/**
 * Row/Column Specification Choice.
 */
public enum Spec
{
	FILL(CPane.FILL, "FILL"),
	PREF(CPane.PREF, "PREF"),
	P10(0.1, "10%"),
	P20(0.2, "20%"),
	P30(0.3, "30%"),
	P40(0.4, "40%"),
	P50(0.5, "50%"),
	P60(0.6, "60%"),
	P70(0.7, "70%"),
	P80(0.8, "80%"),
	P90(0.9, "90%"),
	PIX100(100, "100 pixels"),
	PIX200(200, "200 pixels");
	
	//
	
	public final double spec;
	public final String name;
	
	
	Spec(double spec, String name)
	{
		this.spec = spec;
		this.name = name;
	}
	
	
	public String toString()
	{
		return name;
	}
}