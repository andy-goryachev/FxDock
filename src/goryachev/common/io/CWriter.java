// Copyright © 2012-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import goryachev.common.util.CKit;
import goryachev.common.util.FileTools;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;


public class CWriter
	extends BufferedWriter
{
	public CWriter(File file) throws IOException
	{
		this(file, CKit.CHARSET_UTF8);
	}
	
	
	public CWriter(File file, boolean append) throws IOException
	{
		this(file, CKit.CHARSET_UTF8, append);
	}
	
	
	public CWriter(String filename) throws IOException
	{
		this(filename, CKit.CHARSET_UTF8);
	}
	
	
	public CWriter(File file, Charset cs) throws IOException
	{
		this(file, cs, false);
	}
	
	
	public CWriter(File file, Charset cs, boolean append) throws IOException
	{
		this(new FileOutputStream(ensureParent(file), append), cs);
	}
	
	
	public CWriter(String filename, Charset cs) throws IOException
	{
		this(new FileOutputStream(ensureParent(new File(filename))), cs);
	}
	
	
	public CWriter(OutputStream in, Charset cs) throws IOException
	{
		super(new OutputStreamWriter(in, cs));
	}
	
	
	public CWriter(OutputStream in) throws IOException
	{
		super(new OutputStreamWriter(in, CKit.CHARSET_UTF8));
	}
	
	
	public void nl() throws IOException
	{
		write("\n");
	}
	
	
	private static File ensureParent(File f)
	{
		FileTools.ensureParentFolder(f);
		return f;
	}
}
