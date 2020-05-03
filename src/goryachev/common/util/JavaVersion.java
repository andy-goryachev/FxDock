// Copyright © 2018-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;

import goryachev.common.log.Log;

/**
 * Java Version.
 * 
 * Oracle's idea of versioning is a very interesting one, check out for example this document:
 * http://www.oracle.com/technetwork/java/javase/versioning-naming-139433.html
 * 
 * Starting with version 9, a new numering scheme is introduced:
 * http://openjdk.java.net/jeps/223
 * 
 * This class provides a simplified encapsulation of the version string, allowing for
 * comparison, while dropping minor details such as build version.  In the cases where
 * these details are still needed, the raw system property should be used, or, starting 
 * with Java 9, a Runtime.Version class.
 */
public class JavaVersion
	implements Comparable<JavaVersion>
{
	private final String version;
	private final int[] ver;
	
	
	protected JavaVersion(String version, int[] ver)
	{
		this.version = version;
		this.ver = ver;
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof JavaVersion)
		{
			JavaVersion v = (JavaVersion)x;
			return version.equals(v.version);
		}
		else
		{
			return false;
		}
	}
	
	
	public int hashCode()
	{
		return FH.hash(JavaVersion.class, version);
	}
	
	
	public int compareTo(JavaVersion v)
	{
		int n = Math.min(ver.length, v.ver.length);
		for(int i=0; i<n; i++)
		{
			int a = ver[i];
			int b = v.ver[i];
			int d = a - b;
			if(d != 0)
			{
				return d;
			}
		}
		return 0;
	}
	
	
	public String toString()
	{
		return version;
	}
	
	
	public int[] toIntArray()
	{
		return ver.clone();
	}
	
	
	public boolean isSameOrLaterThan(JavaVersion x)
	{
		return compareTo(x) >= 0;
	}
	
	
	public boolean isLaterThan(JavaVersion x)
	{
		return compareTo(x) > 0;
	}
	
	
	public static JavaVersion getJavaVersion()
	{
		return parseSystemProperty("java.version");
	}
	
	
	public static JavaVersion getJavaRuntimeVersion()
	{
		return parseSystemProperty("java.runtime.version");
	}
	
	
	public static JavaVersion getJavaSpecificationVersion()
	{
		return parseSystemProperty("java.specification.version");
	}
	
	
	protected static int parseNumber(String s) throws Exception
	{
		int end = s.length();
		
		for(int i=0; i<end; i++)
		{
			char c = s.charAt(i);
			if(!Character.isDigit(c))
			{
				end = i;
				break;
			}
		}
		
		if(end < s.length())
		{
			s = s.substring(0, end);
		}
		
		return Integer.parseInt(s);
	}
	
	
	protected static JavaVersion parseSystemProperty(String propertyKey)
	{
		String s = System.getProperty(propertyKey);
		return parse(s);
	}
	
	
	protected static JavaVersion parse(String s)
	{
		int[] ver;
		
		try
		{
			String[] ss = CKit.split(s, '.');
			ver = new int[ss.length];
			
			for(int i=0; i<ss.length; i++)
			{
				ver[i] = parseNumber(ss[i]);
			}
		}
		catch(Exception e)
		{
			Log.err(e);
			ver = new int[0];
		}
		
		return new JavaVersion(s, ver);
	}
}
