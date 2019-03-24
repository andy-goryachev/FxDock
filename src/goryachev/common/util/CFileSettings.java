// Copyright © 2011-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;


public class CFileSettings
	implements CSettings.Provider
{
	private File file;
	private Properties properties;
	private boolean sorted;


	public CFileSettings(File f)
	{
		this.file = f;
		this.properties = new Properties();
	}


	public File getFile()
	{
		return file;
	}


	public void setSorted(boolean on)
	{
		sorted = on;
	}


	public boolean isSorted()
	{
		return sorted;
	}


	public String getProperty(String key)
	{
		return properties.getProperty(key);
	}


	public void setProperty(String key, String value)
	{
		if(value == null)
		{
			properties.remove(key);
		}
		else
		{
			properties.setProperty(key, value);
		}
	}


	public CList<String> getPropertyNames()
	{
		return new CList<String>(properties.stringPropertyNames());
	}


	public void load() throws Exception
	{
		load(getFile());
	}


	public void load(File f) throws Exception
	{
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
		try
		{
			properties.load(in);
		}
		finally
		{
			CKit.close(in);
		}
	}
	

	public void save() throws Exception
	{
		save(getFile());
	}

	
	public void save(File f) throws Exception
	{
		FileTools.ensureParentFolder(f);
		
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "8859_1"));
		try
		{
			save(out);
		}
		finally
		{
			CKit.close(out);
		}
	}
	
	
	protected void save(BufferedWriter wr) throws Exception
	{
		synchronized(properties)
		{
			CList<String> keys = new CList(properties.stringPropertyNames());
			if(sorted)
			{
				CComparator.sortStrings(keys);
			}
			
			for(String key: keys)
			{
				String val = properties.getProperty(key);
				key = saveConvert(key, true);
				val = saveConvert(val, false);
				wr.write(key);
				wr.write('=');
				wr.write(val);
				wr.newLine();
			}
		}
		
		wr.flush();
	}


	protected String saveConvert(String s, boolean escapeSpace)
	{
		int len = s.length();
		int sz = len + len;
		if(sz < 0)
		{
			sz = Integer.MAX_VALUE;
		}

		StringBuffer sb = new StringBuffer(sz);
		for(int i=0; i<len; i++)
		{
			char ch = s.charAt(i);
			if((ch > 61) && (ch < 127))
			{
				if(ch == '\\')
				{
					sb.append('\\');
					sb.append('\\');
					continue;
				}
				sb.append(ch);
				continue;
			}
			switch(ch)
			{
			case ' ':
				if(i == 0 || escapeSpace)
				{
					sb.append('\\');
				}
				sb.append(' ');
				break;
			case '\t':
				sb.append('\\');
				sb.append('t');
				break;
			case '\n':
				sb.append('\\');
				sb.append('n');
				break;
			case '\r':
				sb.append('\\');
				sb.append('r');
				break;
			case '\f':
				sb.append('\\');
				sb.append('f');
				break;
			case '=':
			case ':':
			case '#':
			case '!':
				sb.append('\\');
				sb.append(ch);
				break;
			default:
				if((ch < 0x0020) || (ch > 0x007e))
				{
					sb.append('\\');
					sb.append('u');
					sb.append(Hex.toHexChar((ch >> 12) & 0xF));
					sb.append(Hex.toHexChar((ch >> 8) & 0xF));
					sb.append(Hex.toHexChar((ch >> 4) & 0xF));
					sb.append(Hex.toHexChar(ch & 0xF));
				}
				else
				{
					sb.append(ch);
				}
			}
		}
		return sb.toString();
	}
	
	
	public static CFileSettings loadQuiet(File f)
	{
		CFileSettings s = new CFileSettings(f);
		s.setSorted(true);
		
		try
		{
			s.load();
		}
		catch(Exception e)
		{ }
		return s;
	}
	
	
	public void clear()
	{
		properties.clear();
	}
}
