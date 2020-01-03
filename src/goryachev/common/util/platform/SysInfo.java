// Copyright © 2009-2020 Andy Goryachev <andy@goryachev.com>
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
		SysInfo s = new SysInfo(out);
		
		s.extractApp();
		s.extractEnvironment();
		s.extractSystemProperties();
		
		return out.getReport();
	}
	
	
	protected void header(String title)
	{
		out.header(title);
	}
	
	
	protected void nl()
	{
		out.nl();
	}
	
	
	protected void print(String x)
	{
		print(1, x);
	}
	
	
	protected void print(int indents, String x)
	{
		out.print(indents, x);
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
		print("Time: " + new SimpleDateFormat("yyyy-MMdd HH:mm:ss").format(System.currentTimeMillis()));
		
		long max = Runtime.getRuntime().maxMemory();
		long free = max - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory();
		
		print("Available Memory: " + number(max));
		print("Free Memory:" + number(free));
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
			print(key + " = " + safe(env.get(key)));
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
			print(key + " = " + safe(p.getProperty(key)));
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
		print(name);

		try
		{
			CList<String> names = new CList<>(Security.getAlgorithms(name));
			CSorter.sort(names);
			
			for(String s: names)
			{
				print(2, s);
			}
		}
		catch(Exception e)
		{
			print(CKit.stackTrace(e));
		}
	}
	
	
	//
	
	
	public abstract static class Out
	{
		public abstract void header(String title);
		
		public abstract void nl();
		
		public abstract void print(int count, String x);
		
		public abstract void describe(Object key, Object v);
		
		public abstract Out a(Object x);
		
		//
		

		protected void describe(Object x)
		{
			if(x == null)
			{
			}
			else if(x instanceof String)
			{
				a('"');
				a(x);
				a('"');
			}
			else if(x instanceof Object[])
			{
				Object[] a = (Object[])x;
				a("Object[");
				a(a.length);
				a("]");
			}
			else if(x instanceof int[])
			{
				int[] a = (int[])x;
				a("int[");
				a(a.length);
				a("]");
			}
			else
			{
				a(x);
			}
		}
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

		
		public Out a(Object x)
		{
			sb.a(x);
			return this;
		}
		
	
		public void header(String title)
		{
			sb.a(title).nl();
		}
		
		
		public void nl()
		{
			sb.nl();
		}
		
		
		public void print(int count, String x)
		{
			for(int i=0; i<count; i++)
			{
				sb.a(indent);
			}
			sb.append(x);		
			sb.nl();
		}
		
		
		public void describe(Object key, Object v)
		{
			sb.a(indent);
			sb.a(key);
			sb.a(" = ");
			describe(sb, v);
			sb.nl();
		}
		
		
		public String getReport()
		{
			return sb.getAndClear();
		}
	}
}
