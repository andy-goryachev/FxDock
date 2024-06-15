// Copyright © 2009-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.platform;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.CSorter;
import goryachev.common.util.Hex;
import goryachev.common.util.SB;
import java.security.Security;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Properties;


public class SysInfo
{
	protected DecimalFormat numberFormat = new DecimalFormat("#,##0.##");
	protected final Out out;
	
	
	public SysInfo(Out out)
	{
		this.out = out;
	}
	
	
	/** generates system information report as text string */
	public static String getSystemInfo()
	{
		StringOut out = new StringOut();
		getSystemInfo(out);
		return out.getReport();
	}
	
	
	public static void getSystemInfo(Out out)
	{
		SysInfo s = new SysInfo(out);
		s.extractApp();
		s.extractSystemProperties();
		s.extractEnvironment();
	}
	
	
	protected void header(String title)
	{
		out.header(title);
	}
	
	
	protected void nl()
	{
		out.nl();
	}
	
	
	protected void print(String name, String value)
	{
		print(1, name, value);
	}
	
	
	protected void print(int indents, String name, String value)
	{
		out.print(indents, name, value);
	}
	
	
	protected String number(Object x)
	{
		return numberFormat.format(x);
	}
	
	
	protected String safe(String s)
	{
		if(s != null)
		{
			boolean notSafe = false;
			int sz = s.length();
			for(int i=0; i<sz; i++)
			{
				char c = s.charAt(i);
				if(c < 0x20)
				{
					notSafe = true;
					break;
				}
			}
			
			if(notSafe)
			{
				SB sb = new SB(sz);
				for(int i=0; i<sz; i++)
				{
					char c = s.charAt(i);
					if(c < 0x20)
					{
						sb.a(unicode(c));
					}
					else
					{
						sb.a(c);
					}
				}
				s = sb.toString();
			}
		}
		return s;
	}
	
	
	protected static String unicode(char c)
	{
		return "\\u" + Hex.toHexString(c, 4);
	}
	
	
	public void extractApp()
	{
		long max = Runtime.getRuntime().maxMemory();
		long free = max - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory();

		header("Application");
		print("Time/Date", new SimpleDateFormat("yyyy-MMdd HH:mm:ss").format(System.currentTimeMillis()));
		print("Available Memory", number(max));
		print("Free Memory:", number(free));
		nl();
	}

	
	public void extractEnvironment()
	{
		header("Environment");
		
		Map<String,String> env = System.getenv();
		CList<String> keys = new CList<>(env.keySet());
		CSorter.sort(keys);
		for(String key: keys)
		{
			print(key, safe(env.get(key)));
		}
		nl();
	}
	
	
	public void extractSystemProperties()
	{
		header("System Properties");
		
		Properties p = System.getProperties();
		CList<String> keys = new CList<>(p.stringPropertyNames());
		CSorter.sort(keys);
		for(String key: keys)
		{
			print(key, safe(p.getProperty(key)));
		}
		nl();
	}
	
	
	public void extractSecurity()
	{
		header("Security");
		
		listSecurityAlgorithms("Cipher");
		listSecurityAlgorithms("KeyStore");
		listSecurityAlgorithms("Mac");
		listSecurityAlgorithms("MessageDigest");
		listSecurityAlgorithms("Signature");
		
		nl();
	}
	
	
	protected void listSecurityAlgorithms(String name)
	{
		print(name, "");

		try
		{
			CList<String> names = new CList<>(Security.getAlgorithms(name));
			CSorter.sort(names);
			
			for(String s: names)
			{
				print(2, s, "");
			}
		}
		catch(Exception e)
		{
			print(e.getMessage(), "");
		}
	}
	
	
	//
	
	
	public abstract static class Out
	{
		public abstract void header(String title);
		
		public abstract void nl();
		
		/** output name and value, the client must also append new line */
		public abstract void print(int indent, String name, String value);
	}
	
	
	//
	
	
	public static class StringOut
		extends Out
	{
		private final SB sb;
		private String indent = "\t";

		
		public StringOut()
		{
			sb = new SB();
		}

	
		@Override
		public void header(String title)
		{
			sb.a(title).nl();
		}
		
		
		@Override
		public void nl()
		{
			sb.nl();
		}
		
		
		@Override
		public void print(int count, String name, String value)
		{
			for(int i=0; i<count; i++)
			{
				sb.a(indent);
			}
			sb.append(name);
			sb.append("=");
			sb.append(value);
			sb.nl();
		}
		
		
		public String getReport()
		{
			return sb.getAndClear();
		}
	}
}
