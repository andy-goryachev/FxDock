// Copyright © 2012-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.log;
import goryachev.common.io.CWriter;
import goryachev.common.util.CKit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class FileLogWriter
	extends LogWriter
{
	private File file;
	private long maxSize;
	private int rounds;


	public FileLogWriter(String name, File f, long maxSize, int rounds)
	{
		super(name);
		this.file = f;
		this.maxSize = maxSize;
		this.rounds = Math.max(1, rounds);
	}
	
	
	protected void openFile()
	{
		try
		{
			File p = file.getParentFile();
			if(p != null)
			{
				p.mkdirs();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	public void print(String s)
	{
		openFile();
		
		try
		{
			long pos;
			FileOutputStream fs = new FileOutputStream(file, true);
			try
			{
				CWriter out = new CWriter(fs);
				try
				{
					out.write(s);
					out.write("\n");
					out.flush();
					
					pos = fs.getChannel().position();
				}
				finally
				{
					CKit.close(out);
				}
			}
			finally
			{
				CKit.close(fs);
			}
			
			if(pos > maxSize)
			{
				rotate();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public void close() throws IOException
	{
	}
	
	
	protected void rotate()
	{
		File prev = null;
		for(int round=rounds-1; round>=0; round--)
		{
			File f = getFile(round);
			if(prev == null)
			{
				f.delete();
			}
			else
			{
				f.renameTo(prev);
			}
			prev = f;
		}
		
		openFile();
	}
	
	
	protected File getFile(int round)
	{
		if(round == 0)
		{
			return file;
		}
		
		String name = file.getName(); 
		int ix = name.lastIndexOf('.');
		if(ix < 0)
		{
			name = name + "." + round;
		}
		else
		{
			name = name.substring(0, ix) + "." + round + name.substring(ix);
		}
		
		return new File(file.getParentFile(), name);
	}
}
