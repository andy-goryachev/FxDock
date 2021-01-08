// Copyright © 2013-2021 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/** application product information */
public class ProductInfo
{
	// web site url
	private static Object webSite = new Obj("http://goryachev.com");
	public static String getWebSite() { return webSite.toString(); }
	public static void setWebSite(String s) { webSite = s; }
	
	// support email address
	private static Object supportEmail = new Obj("support@goryachev.com");
	public static String getSupportEmail() { return supportEmail.toString(); }
	public static void setSupportEmail(String s) { supportEmail = s; }
}
