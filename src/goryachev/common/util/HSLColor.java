// Copyright (c) 2006-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.awt.Color;


// http://en.wikipedia.org/wiki/HSL_color_space
public class HSLColor
{
	private static final float FRACT_1_3 = 1f/3f;
	private static final float FRACT_1_6 = 1f/6f;
	private static final float FRACT_2_3 = 2f/3f;
	
	// TODO immutable
	private float hue;
	private float sat;
	private float lum;
	
	
	public HSLColor(float H, float S, float L)
	{
		hue = H;
		sat = S;
		lum = L;
	}
	
	
	public HSLColor(HSLColor other)
	{
		hue = other.hue;
		sat = other.sat;
		lum = other.lum;
	}
	
	
	public HSLColor(Color c)
	{
		this(c.getRed(),c.getGreen(),c.getBlue());
	}
	
	
	public HSLColor(int r, int g, int b)
	{
		int max = Math.max(Math.max(r,g),b);
		int min = Math.min(Math.min(r,g),b);
		
		if(max == min)
		{
			hue = 0f;
		}
		else if(max == r)
		{
			if(g >= b)
			{
				hue = (FRACT_1_6 * (g - b))/(max-min);
			}
			else
			{
				hue = (FRACT_1_6 * (g - b))/(max-min) + 1f;
			}
		}
		else if(max == g)
		{
			hue = (FRACT_1_6 * (b - r))/(max-min) + FRACT_1_3;
		}
		else
		{
			hue = (FRACT_1_6 * (r - g))/(max-min) + FRACT_2_3;
		}
		
		lum = (max + min)/510f;
		
		if(max == min)
		{
			sat = 0;
		}
		else if(lum < 0.5f)
		{
			sat = (max-min)/510f/lum;
		}
		else
		{
			sat = (max-min)/(510f-510f*lum);
		}
	}
	
	
	public int getIntHue()
	{
		return (int)(hue * 360);
	}
	
	
	public float getHue()
	{
		return hue;
	}
	
	
	public void setHue(float hue)
	{
		check(hue);
		this.hue = hue;
	}
	
	
	public int getIntLuminance()
	{
		return (int)(lum * 255);
	}
	
	
	public float getLuminance()
	{
		return lum;
	}
	
	
	public void setLuminance(float lum)
	{
		check(lum);
		this.lum = lum;
	}
	
	
	public int getIntSaturation()
	{
		return (int)(sat * 255);
	}
	
	
	public float getSaturation()
	{
		return sat;
	}
	
	
	public void setSaturation(float sat)
	{
		check(sat);
		this.sat = sat;
	}
	
	
	protected void check(float x)
	{
		if((x < 0.0f) || (x > 1.0f))
		{
			throw new IllegalArgumentException("value must be within range [0 ... 1.0]");
		}
	}
	
	
//	public float hue()
//	{
//		return hue;
//	}
//	
//	
//	public float saturation()
//	{
//		return sat;
//	}
//	
//	
//	public float luminance()
//	{
//		return lum;
//	}

	
	public Color getColor()
	{
		return toColor(hue,sat,lum);
	}


	public Color getColor(int alpha)
	{
		return toColor(hue,sat,lum,alpha);
	}


	// H,S,L: 0 .. 1.0
	public static Color toColor(float H, float S, float L)
	{
		return toColor(H, S, L, 255);
	}


	// H,S,L: 0 .. 1.0
	public static int toColorRGB(float H, float S, float L)
	{
		return toColorARGB(H, S, L, 255);
	}


	// H,S,L: 0 .. 1.0
	public static Color toColor(float H, float S, float L, int alpha)
	{
		float temp2 = (L < 0.5f ? (L * (1.0f + S)) : (L + S - (L * S)));
		float temp1 = 2.0f * L - temp2;

		float temp3R = norm(H + FRACT_1_3);
		float temp3G = H;
		float temp3B = norm(H - FRACT_1_3);

		int r = color(temp1, temp2, temp3R);
		int g = color(temp1, temp2, temp3G);
		int b = color(temp1, temp2, temp3B);

		try
		{
			return new Color(r, g, b, alpha);
		}
		catch(Exception e)
		{
			Log.print(r + "." + g + "." + b);
			Log.err(e);
			return new Color(0, 0, 0, 0);
		}
	}
	
	
	// H,S,L: 0 .. 1.0
	public static int toColorARGB(float H, float S, float L, int alpha)
	{
		float temp2 = (L < 0.5f ? (L * (1.0f + S)) : (L + S - (L * S)));
		float temp1 = 2.0f * L - temp2;

		float temp3R = norm(H + FRACT_1_3);
		float temp3G = H;
		float temp3B = norm(H - FRACT_1_3);

		int r = color(temp1, temp2, temp3R);
		int g = color(temp1, temp2, temp3G);
		int b = color(temp1, temp2, temp3B);

		int d = ((alpha & 0xff) << 24);
		d |= ((r & 0xff) << 16);
		d |= ((g & 0xff) << 8);
		d |= (b & 0xff);
		return d;
	}
	
	
	private static int color(float temp1, float temp2, float temp3)
	{
		float c;
		if(temp3 < FRACT_1_6)
		{
			c = temp1 + ((temp2 - temp1)*6.0f*temp3);
		}
		else if(temp3 < 0.5f)
		{
			c = temp2;
		}
		else if(temp3 < FRACT_2_3)
		{
			c = temp1 + ((temp2 - temp1)*(FRACT_2_3 - temp3)*6.0f);
		}
		else
		{
			c = temp1;
		}
		
		return (int)(c*255);
	}
	
	
	private static float norm(float t)
	{
		if(t < 0.0f)
		{
			return t + 1.0f;
		}
		else if(t > 1.0f)
		{
			return t - 1.0f;
		}
		else
		{
			return t;
		}
	}
}
