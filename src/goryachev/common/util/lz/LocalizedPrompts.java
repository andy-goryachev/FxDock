// Copyright (c) 2005-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.lz;
import goryachev.common.io.CWriter;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;


/*
 * This class encapsulates prompts loaded from a source per LocalizedPromptsFormat.
 */
public class LocalizedPrompts
{
	private final CMap<String,String> prompts = new CMap();
	
	
	public LocalizedPrompts()
	{
	}
	
	
	/** returns a prompt for a given key or null */
	public String get(String key)
	{
		return prompts.get(key);
	}
	
	
	public String put(String key, String translation)
	{
		return prompts.put(key, translation);
	}
	
	
	public List<String> getKeys()
	{
		return new CList(prompts.keySet());
	}
	
	
	public int size()
	{
		return prompts.size();
	}
	
	
	/** loads prompts from a file source */
	public static LocalizedPrompts load(File file) throws Exception
	{
		byte[] b = CKit.readBytes(file);
		return parse(b);
	}
	
	
	/** loads prompts from a JAR resource */
	public static LocalizedPrompts load(String resource) throws Exception
	{
		InputStream in = ClassLoader.getSystemResourceAsStream(resource);
		if(in == null)
		{
			throw new IOException("prompts resource not found: " + resource);
		}
		
		try
		{
			byte[] b = CKit.readBytes(in);
			return parse(b);
		}
		finally
		{
			CKit.close(in);
		}
	}
	
	
	protected static LocalizedPrompts parse(byte[] b) throws Exception
	{
		String text = LocalizedPromptsFormat.detectEncodingAndExtractText(b);
		BufferedReader rd = new BufferedReader(new StringReader(text));
		
		String s;
		int line = 0;
		LocalizedPrompts prompts = new LocalizedPrompts();
		while((s = rd.readLine()) != null)
		{
			++line;
			
			if(s.startsWith(LocalizedPromptsFormat.COMMENT_PREFIX))
			{
				continue;
			}
			
			int ix = s.indexOf(LocalizedPromptsFormat.SEPARATOR_CHAR);
			if(ix > 0)
			{
				try
				{
					String id = s.substring(0, ix);
					
					String tr = s.substring(ix + 1);
					if(tr.length() == 0)
					{
						// FIX need to decide whether no translation means empty translation or null translation,
						// in which case the master should be used.
						continue;
					}
					
					id = LocalizedPromptsFormat.decode(id);
					tr = LocalizedPromptsFormat.decode(tr);
					
					prompts.put(id, tr);
				}
				catch(Exception e)
				{
					throw new Exception("problem on line " + line);
				}
			}
		}
		return prompts;
	}
	
	
	public void write(File file) throws Exception
	{
		CWriter wr = new CWriter(file);
		try
		{
			wr.write(CKit.BOM);
			
			for(String key: getKeys())
			{
				String tr = get(key);

				key = LocalizedPromptsFormat.encode(key);
				tr = LocalizedPromptsFormat.encode(tr);
				
				wr.write(key);
				wr.write(LocalizedPromptsFormat.SEPARATOR_CHAR);
				wr.write(tr);
				wr.write('\n');
			}
		}
		finally
		{
			CKit.close(wr);
		}
	}
}
