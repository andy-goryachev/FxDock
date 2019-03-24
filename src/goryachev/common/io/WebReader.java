// Copyright © 2013-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import goryachev.common.util.CKit;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


/**
 * Retrieves the content with the specified URL as a UTF-8 string
 */ 
public class WebReader
{
	private int connectTimeout;
	private int readTimeout;
	private int maxContentLength;
	
	
	public WebReader()
	{
		this(10000, 5000, 1000000);
	}
	
	
	public WebReader(int connectTimeout, int readTimeout, int maxContentLength)
	{
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
		this.maxContentLength = maxContentLength;
	}
	
	
	public void setMaxContentLength(int max)
	{
		maxContentLength = max;
	}
	
	
	public void setConnectTimeout(int ms)
	{
		connectTimeout = ms;
	}
	
	
	public void setReadTimeout(int ms)
	{
		readTimeout = ms;
	}
	
	
	public String readString(String url) throws Exception
	{
		return readString(new URL(url));
	}
	
	
	public String readString(URL url) throws Exception
	{
		byte[] b = readBytes(url);
		return new String(b, CKit.CHARSET_UTF8);
	}
	
	
	// I thought it will parse the headers and return a good, typed representation of the content
	// but it's probably unnecessary
	public byte[] readBytes(URL url) throws Exception
	{
		URLConnection c = url.openConnection();
		
//		if(c instanceof HttpsURLConnection)
//		{
//			// TODO set gullible access handler?
//		}
		
		c.setConnectTimeout(connectTimeout);
		c.setReadTimeout(readTimeout);
		c.setDoOutput(false);
		c.setAllowUserInteraction(false);
		c.setUseCaches(false);
		
		c.connect();
		
		//D.print("content-encoding: " + c.getContentEncoding());
		
		// text/html
		// image/jpeg
		// text/plain
		//D.print("content-type: " + c.getContentType());
		
		InputStream in = c.getInputStream();
		try
		{
			return CKit.readBytes(in, maxContentLength);
		}
		finally
		{
			CKit.close(in);
		}
	}
}
