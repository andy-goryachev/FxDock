// Copyright © 2010-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.log.Log;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;


public class CFileLock
{
	protected static final Log log = Log.get("CFileLock");
	private File file;
	private FileChannel channel;
	private FileLock lock;


	public CFileLock(File file)
	{
		this.file = file;
	}


	/** returns true if lock has been successfully acquired */
	@SuppressWarnings("resource")
	public boolean lock()
	{
		try
		{
			if(!file.exists())
			{
				file.getParentFile().mkdirs();
			}
			channel = new RandomAccessFile(file, "rw").getChannel();

			lock = channel.tryLock();
			if(lock != null)
			{
				return true;
			}
		}
		catch(Exception e)
		{ 
			log.error(e);
		}
		
		unlock();

		return false;
	}


	public void unlock()
	{
		if(lock != null)
		{
			try
			{
				lock.release();
			}
			catch(Exception e)
			{
				log.error(e);
			}
			
			lock = null;
		}
		
		if(channel != null)
		{
			CKit.close(channel);
			channel = null;
			try
			{
				file.delete();
			}
			catch(Exception e)
			{
				log.error(e);
			}
		}
	}
	
	
	public void checkLock() throws CFileLockedException
	{
		if(lock() == false)
		{
			throw new CFileLockedException();
		}
	}
}
