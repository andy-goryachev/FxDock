// Copyright © 2012-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;
import goryachev.common.util.DKey;
import goryachev.common.util.Hex;
import goryachev.common.util.PTable;
import goryachev.common.util.SKey;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.math.BigDecimal;
import java.math.BigInteger;


public class Persister
{
	public static final byte SIGNATURE = (byte)0xAA;
	
	public static final byte NULL      = (byte)0x80;
	public static final byte BOOL      = (byte)0x81;
	public static final byte BOOLS     = (byte)0x82;
	public static final byte BYTE      = (byte)0x83;
	public static final byte BYTES     = (byte)0x84;
	public static final byte CHAR      = (byte)0x85;
	public static final byte CHARS     = (byte)0x86;
	public static final byte SHORT     = (byte)0x87;
	public static final byte SHORTS    = (byte)0x88;
	public static final byte INT       = (byte)0x89;
	public static final byte INTS      = (byte)0x8a;
	public static final byte LONG      = (byte)0x8b;
	public static final byte LONGS     = (byte)0x8c;
	public static final byte FLOAT     = (byte)0x8d;
	public static final byte FLOATS    = (byte)0x8e;
	public static final byte DOUBLE    = (byte)0x8f;
	public static final byte DOUBLES   = (byte)0x90;
	public static final byte STRING    = (byte)0x91;
	public static final byte STRINGS   = (byte)0x92;
	public static final byte BIGINT    = (byte)0x93;
	public static final byte BIGDEC    = (byte)0x94;
	public static final byte OBJECT    = (byte)0x95;
	public static final byte OBJECTS   = (byte)0x96;
	public static final byte LIST      = (byte)0x97;
	public static final byte MAP       = (byte)0x98;
	public static final byte TABLE     = (byte)0x99;
	public static final byte PRECORD   = (byte)0x9a;
	public static final byte COLOR     = (byte)0x9b;
	public static final byte DKEY      = (byte)0x9c;
	//public static final byte PREV_KEY  = (byte)0x9d;
	public static final byte SKEY      = (byte)0x9e;
	public static final byte REF       = (byte)0x9f;
	
	private static CMap<Object,PrimitiveHandler> handlers = initPrimitiveHandlers();
	
	
	private Persister()
	{
	}
	
	
	@Deprecated // switch to KRecord everywhere
	public static synchronized void register(String id, Class<?> type, PersistenceHandler h)
	{
		// disallow disparate handlers
		// ok to register the same handler twice 
		PrimitiveHandler old = handlers.get(type);
		if(old != null)
		{
			if(old.getClass() != h.getClass())
			{
				throw new Error("duplicate registration of type " + type);
			}
		}
		
		old = handlers.get(id);
		if(old != null)
		{
			if(id != ((PersistenceHandler)old).getTypeID())
			{
				throw new Error("duplicate registration of type id " + id);
			}
		}
		
		h.setTypeID(id);
		handlers.put(type, h);
		handlers.put(id, h);
	}
	
	
	@Deprecated // switch to KRecord everywhere
	public static void register(String id, final Class<? extends PRecord> type)
	{
		// check a no-arg constructor
		try
		{
			Constructor c = type.getConstructor();
			if((c.getModifiers() & (2 << Member.PUBLIC)) != 0)
			{
				throw new Error("not public");
			}
		}
		catch(Exception e)
		{
			throw new Error("class must have a public no-arg constructor: " + type, e);
		}
		
		register(id, type, new PersistenceHandler()
		{
			public Object load(PrimitiveInput in) throws Exception
			{
				PRecord r = type.newInstance();
				CMap m = (CMap)in.readObject();
				r.setAttributeMap(m);
				return r;
			}

			public void store(Object x, PrimitiveOutput out) throws Exception
			{
				PRecord r = (PRecord)x;
				out.write(r.getAttributeMap());
			}
		});
	}


	// by Class or String id
	protected static synchronized PrimitiveHandler lookup(Object x)
	{
		return handlers.get(x);
	}
	
	
	protected static String readShortString(PrimitiveInputStream in, int sz) throws Exception
	{
		byte[] b = new byte[sz];
		in.readRawBytes(b);
		return new String(b, CKit.CHARSET_UTF8);
	}
	
	
	public static Object read(File f) throws Exception
	{
		PrimitiveInputStream in = new PrimitiveInputStream(f);
		try
		{
			return read(in);
		}
		finally
		{
			CKit.close(in);
		}
	}
	
	
	public static Object read(InputStream inp) throws Exception
	{
		PrimitiveInputStream in = new PrimitiveInputStream(inp);
		try
		{
			return read(in);
		}
		finally
		{
			CKit.close(in);
		}
	}
	
	
	public static Object read(byte[] b) throws Exception
	{
		PrimitiveInputStream in = new PrimitiveInputStream(b);
		return read(in);
	}
	
	
	public static Object readNoClose(InputStream inp) throws Exception
	{
		PrimitiveInputStream in = new PrimitiveInputStream(inp);
		return read(in);
	}
	

	public static Object read(PrimitiveInputStream in) throws Exception
	{
		byte t = in.readRawByte();
		if(t >= 0)
		{
			return readShortString(in, t);
		}
		else if(t == OBJECT)
		{
			// lookup persistent id
			String id = in.readString();
			PrimitiveHandler h = lookup(id);
			if(h == null)
			{
				throw new Exception("format error: unknown persistence id: " + id);
			}
			else
			{
				return h.read(in);
			}
		}
		else
		{
			PrimitiveHandler h = lookup(t);
			if(h == null)
			{
				// may be unregistered DKEY SKEY
				throw new Exception("format error: unknown type id: " + Hex.toHexString(t, 2));
			}
			else
			{
				return h.read(in);
			}
		}
	}
	
	
	public static DContainer readContainer(File f) throws Exception
	{
		return readContainer(new PrimitiveInputStream(f));
	}
	
	
	public static DContainer readContainer(byte[] b) throws Exception
	{
		return readContainer(new PrimitiveInputStream(b));
	}
	
	
	public static DContainer readContainer(PrimitiveInputStream in) throws Exception
	{
		DContainer c = new DContainer();
		try
		{
			readContainer(in, c);
		}
		finally
		{
			CKit.close(in);
		}
		return c;
	}
	
	
	protected static CMap readMap(PrimitiveInputStream in) throws Exception
	{
		int sz = in.readRawInt();
		CMap m = new CMap(sz);
		PersisterStreamCache c = new PersisterStreamCache(sz);
		
		for(int i=0; i<sz; i++)
		{
			Object k = c.read(in.readObject());
			Object v = c.read(in.readObject());
			m.put(k, v);
		}
		return m;
	}
	
	
	protected static void writeMap(CMap m, PrimitiveOutputStream out) throws Exception
	{
		int sz = m == null ? 0 : m.size();
		PersisterStreamCache c = new PersisterStreamCache(sz);
		out.writeRawInt(sz);
		
		if(m != null)
		{
			for(Object k: m.keySet())
			{
				Object v = m.get(k);
				out.write(c.write(k));
				out.write(c.write(v));
			}
		}
	}
	
	
	protected static void readContainer(PrimitiveInputStream in, DContainer c) throws Exception
	{
		for(;;)
		{
			int x = in.streamRead();
			if(x < 0)
			{
				return;
			}
			else
			{
				byte t = (byte)x;
				if(t >= 0)
				{
					String s = readShortString(in, t);
					c.add(s);
				}
				else if(t == OBJECT)
				{
					String id = in.readString();
					c.add(new DContainer.ObjectMarker(id));
				}
				else
				{
					PrimitiveHandler h = lookup(t);
					if(h == null)
					{
						throw new Exception("format error: unknown type id: " + Hex.toHexString(t, 2));
					}
					else
					{
						h.readContainer(in, c);
					}
				}
			}
		}
	}
	
	
	public static byte[] write(Object x) throws Exception
	{
		try
		{
			ByteArrayOutputStream ba = new ByteArrayOutputStream();
			store(x, ba);
			return ba.toByteArray();
		}
		catch(Exception e)
		{
			throw new Exception("failed to serialize " + (x == null ? "null" : x.getClass()), e);
		}
	}
	
	
	public static void store(Object x, OutputStream out) throws Exception
	{
		PrimitiveOutputStream os = new PrimitiveOutputStream(out);
		store(x, os);
	}
	
	
	public static void store(Object x, File f) throws Exception
	{
		File parent = f.getParentFile();
		if(parent != null)
		{
			parent.mkdirs();
		}
		
		PrimitiveOutputStream out = new PrimitiveOutputStream(f);
		try
		{
			store(x, out);
		}
		finally
		{
			CKit.close(out);
		}
	}
	

	public static void store(Object x, PrimitiveOutputStream out) throws Exception
	{
		if(x == null)
		{
			out.writeRawByte(Persister.NULL); 
		}
		else
		{
			Class<?> c = x.getClass();
			PrimitiveHandler h = handlers.get(c);
			if(h == null)
			{
				throw new Exception("can not serialize " + c);
			}
			else
			{
				h.storeWithID(x, out);
			}
		}
	}
	
	
	private static void add(CMap<Object,PrimitiveHandler> h, Class<?> c, PrimitiveHandler p)
	{
		h.put(c, p);
		h.put(p.getID(), p);
	}
	
	
	private static CMap<Object,PrimitiveHandler> initPrimitiveHandlers()
	{
		CMap<Object,PrimitiveHandler> m = new CMap();
		
		// null
		m.put(Persister.NULL, new PrimitiveHandler(Persister.NULL)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				return null;
			}
			
			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
			}
		});
		
		// boolean
		add(m, Boolean.class, new PrimitiveHandler(Persister.BOOL)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				return in.readRawBoolean();
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
				out.writeRawBoolean(Boolean.TRUE.equals(x));
			}
		});
		
		// boolean[]
		add(m, boolean[].class, new PrimitiveHandler(Persister.BOOLS)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				int sz = in.readRawInt();
				boolean[] a = new boolean[sz];
				for(int i=0; i<sz; i++)
				{
					a[i] = in.readRawBoolean();
				}
				return a;
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
				boolean[] a = (boolean[])x;
				int sz = a.length;
				out.writeRawInt(sz);
				for(int i=0; i<sz; i++)
				{
					out.writeRawBoolean(a[i]);
				}
			}
		});
		
		// byte
		add(m, Byte.class, new PrimitiveHandler(Persister.BYTE)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				return in.readRawByte();
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
				Byte b = (Byte)x;
				out.writeRawByte(b);
			}
		});
		
		// byte[]
		add(m, byte[].class, new PrimitiveHandler(Persister.BYTES)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				int sz = in.readRawInt();
				byte[] a = new byte[sz];
				in.readRawBytes(a);
				return a;
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
				byte[] a = (byte[])x;
				int sz = a.length;
				out.writeRawInt(sz);
				out.writeRawBytes(a);
			}
		});
		
		// char
		add(m, Character.class, new PrimitiveHandler(Persister.CHAR)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				return in.readRawChar();
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
				out.writeRawChar((Character)x);
			}
		});
		
		// char[]
		add(m, char[].class, new PrimitiveHandler(Persister.CHARS)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				int sz = in.readRawInt();
				char[] a = new char[sz];
				for(int i=0; i<sz; i++)
				{
					a[i] = in.readRawChar();
				}
				return a;
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
				char[] a = (char[])x;
				int sz = a.length;
				out.writeRawInt(sz);
				for(int i=0; i<sz; i++)
				{
					out.writeRawChar(a[i]);
				}
			}
		});
		
		// short
		add(m, Short.class, new PrimitiveHandler(Persister.SHORT)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				return in.readRawShort();
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
				out.writeRawShort((Short)x);
			}
		});
		
		// short[]
		add(m, short[].class, new PrimitiveHandler(Persister.SHORTS)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				int sz = in.readRawInt();
				short[] a = new short[sz];
				for(int i=0; i<sz; i++)
				{
					a[i] = in.readRawShort();
				}
				return a;
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
				short[] a = (short[])x;
				int sz = a.length;
				out.writeRawInt(sz);
				for(int i=0; i<sz; i++)
				{
					out.writeRawShort(a[i]);
				}
			}
		});
		
		// int
		add(m, Integer.class, new PrimitiveHandler(Persister.INT)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				return in.readRawInt();
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
				out.writeRawInt((Integer)x);
			}
		});
		
		// int[]
		add(m, int[].class, new PrimitiveHandler(Persister.INTS)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				int sz = in.readRawInt();
				int[] a = new int[sz];
				for(int i=0; i<sz; i++)
				{
					a[i] = in.readRawInt();
				}
				return a;
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
				int[] a = (int[])x;
				int sz = a.length;
				out.writeRawInt(sz);
				for(int i=0; i<sz; i++)
				{
					out.writeRawInt(a[i]);
				}
			}
		});
		
		// long
		add(m, Long.class, new PrimitiveHandler(Persister.LONG)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				return in.readRawLong();
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
				out.writeRawLong((Long)x);
			}
		});
		
		// long[]
		add(m, long[].class, new PrimitiveHandler(Persister.LONGS)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				int sz = in.readRawInt();
				long[] a = new long[sz];
				for(int i=0; i<sz; i++)
				{
					a[i] = in.readRawLong();
				}
				return a;
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
				long[] a = (long[])x;
				int sz = a.length;
				out.writeRawInt(sz);
				for(int i=0; i<sz; i++)
				{
					out.writeRawLong(a[i]);
				}
			}
		});
		
		// float
		add(m, Float.class, new PrimitiveHandler(Persister.FLOAT)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				return in.readRawFloat();
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
				out.writeRawFloat((Float)x);
			}
		});
		
		// float[]
		add(m, float[].class, new PrimitiveHandler(Persister.FLOATS)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				int sz = in.readRawInt();
				float[] a = new float[sz];
				for(int i=0; i<sz; i++)
				{
					a[i] = in.readRawFloat();
				}
				return a;
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
				float[] a = (float[])x;
				int sz = a.length;
				out.writeRawInt(sz);
				for(int i=0; i<sz; i++)
				{
					out.writeRawFloat(a[i]);
				}
			}
		});
		
		// double
		add(m, Double.class, new PrimitiveHandler(Persister.DOUBLE)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				return in.readRawDouble();
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
				out.writeRawDouble((Double)x);
			}
		});
		
		// double[]
		add(m, double[].class, new PrimitiveHandler(Persister.DOUBLES)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				int sz = in.readRawInt();
				double[] a = new double[sz];
				for(int i=0; i<sz; i++)
				{
					a[i] = in.readRawDouble();
				}
				return a;
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
				double[] a = (double[])x;
				int sz = a.length;
				out.writeRawInt(sz);
				for(int i=0; i<sz; i++)
				{
					out.writeRawDouble(a[i]);
				}
			}
		});
			
		// String
		add(m, String.class, new PrimitiveHandler(Persister.STRING)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				int sz = in.readRawInt();
				return readString(in, sz);
			}
			
			@Override
			protected void storeWithID(Object x, PrimitiveOutputStream out) throws Exception
			{
				out.writeRawString((String)x);
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
			}
		});
		
		// String[]
		add(m, String[].class, new PrimitiveHandler(Persister.STRINGS)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				int sz = in.readRawInt();
				PersisterStreamCache c = new PersisterStreamCache(sz);
				String[] a = new String[sz];
				
				for(int i=0; i<sz; i++)
				{
					a[i] = (String)c.read(in.readObject());
				}
				return a;
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
				String[] a = (String[])x;
				int sz = a.length;
				PersisterStreamCache c = new PersisterStreamCache(sz);
				out.writeRawInt(sz);
				
				for(int i=0; i<sz; i++)
				{
					String s = a[i];
					out.write(c.write(s));
				}
			}
		});
		
		// Color - was a mistake
//		add(m, Color.class, new PrimitiveHandler(Persister.COLOR)
//		{
//			public Object read(PrimitiveInputStream in) throws Exception
//			{
//				return new Color(in.readRawInt(), true);
//			}
//
//			public void write(Object x, PrimitiveOutputStream out) throws Exception
//			{
//				Color c = (Color)x;
//				out.writeRawInt(c.getRGB());
//			}
//		});
		
		// Object[]
		add(m, Object[].class, new PrimitiveHandler(Persister.OBJECTS)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				int sz = in.readRawInt();
				PersisterStreamCache c = new PersisterStreamCache(sz);
				Object[] a = new Object[sz];
				for(int i=0; i<sz; i++)
				{
					a[i] = c.read(in.readObject());
				}
				return a;
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
				Object[] a = (Object[])x;
				int sz = a.length;
				PersisterStreamCache c = new PersisterStreamCache(sz);
				out.writeRawInt(sz);
				for(int i=0; i<sz; i++)
				{
					Object v = a[i];
					out.write(c.write(v));
				}
			}
		});
		
		// CList
		add(m, CList.class, new PrimitiveHandler(Persister.LIST)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				int sz = in.readRawInt();
				PersisterStreamCache c = new PersisterStreamCache(sz);
				CList a = new CList(sz);
				
				for(int i=0; i<sz; i++)
				{
					Object v = in.readObject();
					a.add(c.read(v));
				}
				return a;
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{				
				CList a = (CList)x;
				int sz = a.size();
				PersisterStreamCache c = new PersisterStreamCache(sz);
				out.writeRawInt(sz);

				for(int i=0; i<sz; i++)
				{
					Object v = a.get(i);
					out.write(c.write(v));
				}
			}
		});
		
		// CMap
		add(m, CMap.class, new PrimitiveHandler(Persister.MAP)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				return Persister.readMap(in);
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
				Persister.writeMap((CMap)x, out);
			}
		});
		
		// PTable
		add(m, PTable.class, new PrimitiveHandler(Persister.TABLE)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				int cols = in.readRawInt();
				int rows = in.readRawInt();

				PTable t = new PTable();
				for(int i=0; i<cols; i++)
				{
					String name = in.readString();
					t.addColumn(name);
				}
				
				PersisterStreamCache ca = new PersisterStreamCache(cols * rows);
				for(int r=0; r<rows; r++)
				{
					for(int c=0; c<cols; c++)
					{
						Object v = in.readObject();
						t.setValueAt(r, c, ca.read(v));
					}
				}
				return t;
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
				PTable t = (PTable)x;
				int cols = t.getColumnCount();
				int rows = t.getRowCount();
				out.writeRawInt(cols);
				out.writeRawInt(rows);
				
				for(int c=0; c<cols; c++)
				{
					out.write(t.getColumnName(c));
				}
				
				PersisterStreamCache ca = new PersisterStreamCache(cols * rows);
				for(int r=0; r<rows; r++)
				{
					for(int c=0; c<cols; c++)
					{
						Object v = t.getValueAt(r, c);
						out.write(ca.write(v));
					}
				}
			}
		});
		
		// PRecord
		add(m, PRecord.class, new PrimitiveHandler(Persister.PRECORD)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				CMap m = Persister.readMap(in);
				return new PRecord(m);
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
				PRecord r = (PRecord)x;
				Persister.writeMap(r.getAttributeMap(), out);
			}
		});
		
		// BigInteger
		add(m, BigInteger.class, new PrimitiveHandler(Persister.BIGINT)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				return new BigInteger(in.readByteArray());
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
				BigInteger d = (BigInteger)x;
				out.write(d.toByteArray());
			}
		});
		
		// BigDecimal
		add(m, BigDecimal.class, new PrimitiveHandler(Persister.BIGDEC)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				return new BigDecimal(in.readString());
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
				BigDecimal d = (BigDecimal)x;
				out.write(d.toString());
			}
		});
		
		// ref
		add(m, PersisterStreamRef.class, new PrimitiveHandler(Persister.REF)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				return new PersisterStreamRef(in.readRawInt());
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
				out.writeRawInt(((PersisterStreamRef)x).getIndex());
			}
		});
		
		
		// SKey
		add(m, SKey.class, new PrimitiveHandler(Persister.SKEY)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				int sz = in.readRawInt();
				byte[] b = new byte[sz];
				in.readRawBytes(b);
				String s = new String(b, CKit.CHARSET_UTF8);
				return new SKey(s);
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
				String s = ((SKey)x).toString();
				out.writeRawInt(s.length());
				out.writeRawBytes(s.getBytes(CKit.CHARSET_UTF8));
			}
		});
		
		
		// DKey
		add(m, DKey.class, new PrimitiveHandler(Persister.DKEY)
		{
			public Object read(PrimitiveInputStream in) throws Exception
			{
				byte[] b = new byte[DKey.SIZE_IN_BYTES];
				in.readRawBytes(b);
				return new DKey(b);
			}

			public void write(Object x, PrimitiveOutputStream out) throws Exception
			{
				DKey k = (DKey)x;
				k.writeBytes(out);
			}
		});
		
		return m;
	}
}
