// Copyright © 2009-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.SB;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Random;


// RandomAccessFile equivalent with an internal read cache.
// cache uses linear search, so it will not be effective with a large number of buffers
// (although it's probably more efficient with a small number of buffers than using a hashtable)
// TODO implement FIFO cache, linked list of free/used buffers
public class BufferedFile
	extends RandomAccessFile
{
	public enum Mode
	{
		/** "r" Open for reading only */  
		READ,
		/** "rw" Open for reading and writing. If the file does not already exist then an attempt will be made to create it. */  
		READ_WRITE,
		/** "rws" Open for reading and writing, and also require that every update to the file's <b>content or metadata</b> be written synchronously to the underlying storage device. */
		READ_WRITE_CONTENT_METADATA,
		/** "rwd" Open for reading and writing, and also require that every update to the file's <b>content</b> be written synchronously to the underlying storage device. */
		READ_WRITE_CONTENT,
	}
	
	private Buffer[] buffers;
	private int blockSize;
	private CList<Buffer> free;
	private long marker;
	private Random random = new Random();


	public BufferedFile(File f, BufferedFile.Mode mode, int bufferSize, int bufferCount) throws Exception
	{
		super(f, modeToString(mode));
		
		this.blockSize = bufferSize;
		
		buffers = new Buffer[bufferCount];
		free = new CList<>(bufferCount);
		for(int i=0; i<bufferCount; i++)
		{
			Buffer b = new Buffer(bufferSize);
			buffers[i] = b;
			free.add(b);
		}
	}


	public BufferedFile(File f, BufferedFile.Mode mode) throws Exception
	{
		this(f, mode, 4096, 4);
	}
	
	
	protected static String modeToString(Mode m)
	{
		switch(m)
		{
		case READ:
			return "r";
		case READ_WRITE:
			return "rw";
		case READ_WRITE_CONTENT:
			return "rwd";
		case READ_WRITE_CONTENT_METADATA:
			return "rws";
		default:
			throw new Error("?" + m);
		}
	}


	protected void discardCache(long start, int len)
	{
		// TODO maintain [min,max] for the whole cache
		
		// WARNING linear search
		for(Buffer b: buffers)
		{
			if(b.contains(start,len))
			{
				//Log.print(b);
				b.discard();
			}
		}
	}
	
	
	// synchronize marker and actual file pointer
	protected void syncMarker() throws IOException
	{
		// avoid unecessary seeks
		if(marker != getFilePointer())
		{
			//Log.print("seek",marker);
			super.seek(marker);
		}
	}
	
	
	/** returns the actual write or read position */
	public long getCurrentPosition()
	{
		return marker;
	}
	
	
	/** the file pointer may be DIFFERENT from current position because of buffering.  Use getCurrentPosition() */
	public long getFilePointer() throws IOException
	{
		return super.getFilePointer();
	}
	
	
	// the core of caching functionality.  this method attempts to:
	// - find matching buffer in the cache
	// - if no match found, discards a random buffer and reads the fresh data
	// the last step is omitted if already at eof.
	// 
	// buffer start offsets within the file fall on the blockSize boundary, 
	// perhaps it is possible to minimize the amount of disk i/o by setting blockSize 
	// to be a multiple of underlying hardware storage block size
	// (seagate barracuda 1.5TB drive specifies sector size of 512 bytes:
	// http://www.seagate.com/staticfiles/support/disc/manuals/desktop/Barracuda%207200.11/100507013e.pdf
	protected Buffer getBufferAtMarker() throws IOException
	{
		// TODO hashtable
		int pos = (int)(marker % blockSize);
		long start = marker - pos;
		
		// WARNING linear search
		for(Buffer b: buffers)
		{
			if(b.getStartOffset() == start)
			{
				// check if the buffer has at least one byte
				if((int)(marker - start) + 1 < b.getAvailable())
				{
					return b;
				}
				else
				{
					// buffer does not have enough data
					// try to read into the existing buffer
					if(marker >= length())
					{
						// at eof
						return null;
					}
					else
					{
						// read fresh data into the same buffer
						int sz = Math.min(blockSize, (int)(length() - start));
						b.read(this, start, sz);
						return b;
					}
				}
			}
		}
		
		// cache miss
		Buffer b;
		
		// any free buffers?
		if(free.size() > 0)
		{
			b = free.remove(free.size() - 1);
		}
		else
		{
			// TODO remove last block, keep linked list for free and last-used blocks
			// discard random block
			b = buffers[random.nextInt(buffers.length)];
		}
		
		int sz = Math.min(blockSize, (int)(length() - start));
		if(sz < 0)
		{
			return null;
		}
		b.read(this, start, sz);
		return b;
	}
	
	
	protected int lowLevelRead(byte[] buffer, long offset, int len) throws IOException
	{
		super.seek(offset);
		return super.read(buffer, 0, len);
	}
	
	
	protected synchronized int readBytes(byte buf[], int off, int len) throws IOException
	{
		//Log.print(off,len);
		
		Buffer b = getBufferAtMarker();
		if(b == null)
		{
			// must be eof
			return -1;
		}
		
		int rv = b.get(marker, buf, off, len);
		marker += rv;
		return rv;
	}


	public synchronized int read() throws IOException
	{
		return readPrivate(true);
	}
	
	
	/** returns next byte without advancing the file pointer */
	public synchronized int peek() throws IOException
	{
		return readPrivate(false);
	}
	
	
	protected int readPrivate(boolean advance) throws IOException
	{
		Buffer b = getBufferAtMarker();
		if(b == null)
		{
			// must be eof
			return -1;
		}
		
		int c = b.get(marker);
		if(advance)
		{
			if(c >= 0)
			{
				marker++;
			}
		}
		return c;
	}
	
	
	/** unlike readLine(), this method reads UTF-8 string until EOF or newline.  Returns null when EOF. */
	@Deprecated // or improve
	public synchronized String readText() throws Exception
	{
		SB sb = new SB(128);
		for(;;)
		{
			int c = readPrivate(true);
			if(c < 0)
			{
				if(sb.length() == 0)
				{
					return null;
				}
				else
				{
					return sb.toString();
				}
			}
			else if(c == '\r')
			{
				// ignore, we do not support mac encoding with '\r' separators
			}
			else if(c == '\n')
			{
				return sb.toString();
			}
			else
			{
				sb.append((char)c);
			}
		}
	}
	

	public int read(byte b[], int off, int len) throws IOException
	{
		return readBytes(b, off, len);
	}


	public int read(byte b[]) throws IOException
	{
		return readBytes(b, 0, b.length);
	}
	

	public synchronized void write(int b) throws IOException
	{
		syncMarker();
		super.write(b);
		discardCache(marker, 1);
		marker += 1;
	}


	public synchronized void write(byte b[]) throws IOException
	{
		syncMarker();
		super.write(b, 0, b.length);
		discardCache(marker, b.length);
		marker += b.length;
	}


	public synchronized void write(byte b[], int off, int len) throws IOException
	{
		syncMarker();
		super.write(b, off, len);
		discardCache(marker, len);
		marker += len;
	}
	
	
	public synchronized void seek(long pos) throws IOException
	{
		marker = pos;
	}
	
	
	public synchronized void writeString(String s) throws IOException
	{
		if(s == null)
		{
			writeInt(-1);
		}
		else
		{
			int sz = s.length();
			byte[] b = new byte[4 + sz + sz];
			
			int ix = 0;
			b[ix++] = (byte)(sz >> 24);
			b[ix++] = (byte)(sz >> 16);
			b[ix++] = (byte)(sz >> 8);
			b[ix++] = (byte)sz;
			
			for(int i=0; i<sz; i++)
			{
				int c = s.charAt(i);
				b[ix++] = (byte)(c >> 8);
				b[ix++] = (byte)c;
			}
			
			write(b);
		}
	}
	
	
	public void writeText(String s) throws IOException
	{
		byte[] b = s.getBytes(CKit.CHARSET_UTF8);
		write(b);
	}
	
	
	public synchronized String readString() throws IOException
	{
		int sz = readInt();
		if(sz < -1)
		{
			throw new IOException("not a string");
		}
		else if(sz == -1)
		{
			return null;
		}
		else
		{
			byte[] b = new byte[sz+sz];
			readFully(b);
			SB sb = new SB(sz);
			int ix = 0;
			for(int i=0; i<sz; i++)
			{
				int c = (b[ix++] & 0xff) << 8;
				c |= (b[ix++] & 0xff);
				sb.append((char)c);
			}
			return sb.toString();
		}
	}
	
		
	//
	
	
	protected static class Buffer
	{
		private byte[] buffer;
		private long offset;
		private int available;
		
		
		public Buffer(int size)
		{
			buffer = new byte[size];
		}


		public void read(BufferedFile f, long start, int len) throws IOException
		{
			// can't use this
			//f.readFully(buffer);

			int read = 0;
			do
			{
				int count = f.lowLevelRead(buffer, start + read, len - read);
				if(count < 0)
				{
					throw new IOException();
				}
				read += count;
			} while(read < len);

			offset = start;
			available = len;
			
			//Log.print(start,len);
		}


		public long getStartOffset()
		{
			return offset;
		}
		
		
		public int getAvailable()
		{
			return available;
		}
		

		public void discard()
		{
			offset = -1;
			available = 0;
		}


		public boolean contains(long start, int len)
		{
			if((start + len) <= offset)
			{
				// [seg] [buffer]
				return false;
			}
			
			if(start >= (offset + available))
			{
				// [buffer] [seg]
				return false;
			}
			else
			{
				// intersect
				return true;
			}
		}
		
		
		public int get(long marker)
		{
			int pos = (int)(marker - offset);
			return buffer[pos] & 0xff;
		}

		
		public int get(long marker, byte[] dest, int off, int len)
		{
			int start = (int)(marker - offset);
			int sz = Math.min(available - start, len);
			System.arraycopy(buffer, start, dest, off, sz);
			return sz;
		}
		
		
		public String toString()
		{
			return "Buffer[" + offset + ":" + available + "]";
		}
	}
}
