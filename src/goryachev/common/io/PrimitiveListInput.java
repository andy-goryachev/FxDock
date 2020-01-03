// Copyright © 2011-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import goryachev.common.util.CList;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;


public class PrimitiveListInput
	implements PrimitiveInput
{
	private CList<Object> list;
	private int pos;


	public PrimitiveListInput(CList<Object> in)
	{
		this.list = in;
	}
	
	
	public static PrimitiveListInput parse(Object x)
	{
		if(x instanceof CList)
		{
			return new PrimitiveListInput((CList)x);
		}
		else if(x instanceof PrimitiveListInput)
		{
			return (PrimitiveListInput)x;
		}
		else
		{
			return null;
		}
	}


	public void close() throws IOException
	{
	}
	
	
	public boolean hasMoreData()
	{
		if(list == null)
		{
			return false;
		}
		
		return pos < list.size();
	}


	public Object readObject() throws Exception
	{
		if(list == null)
		{
			return null;
		}
		
		if(pos < list.size())
		{
			return list.get(pos++);
		}
		else
		{
			return null;
		}
	}
	
	
	public boolean readBool() throws Exception
	{
		return (Boolean)readObject();
	}
	
	
	public Boolean readBoolean() throws Exception
	{
		return (Boolean)readObject();
	}
	
	
	public Byte readByte() throws Exception
	{
		return (Byte)readObject();
	}
	
	
	public byte[] readByteArray() throws Exception
	{
		return (byte[])readObject();
	}
	
	
	public Short readShort() throws Exception
	{
		return (Short)readObject();
	}


	public int readInt() throws Exception
	{
		return readInteger();
	}
	
	
	public Integer readInteger() throws Exception
	{
		return (Integer)readObject();
	}


	public Long readLong() throws Exception
	{
		return (Long)readObject();
	}
	
	
	public Float readFloat() throws Exception
	{
		return (Float)readObject();
	}
	
	
	public Double readDouble() throws Exception
	{
		return (Double)readObject();
	}


	public String readString() throws Exception
	{
		return (String)readObject();
	}
	
	
	public BigInteger readBigInteger() throws Exception
	{
		return (BigInteger)readObject();
	}
	
	
	public BigDecimal readBigDecimal() throws Exception
	{
		return (BigDecimal)readObject();
	}

	
//	public DKey readDKey() throws Exception
//	{
//		return (DKey)readObject();
//	}
//	
//	
//	public PrevKey readPrevKey() throws Exception
//	{
//		return (PrevKey)readObject();
//	}
//	
//
//	public SKey readSKey() throws Exception
//	{
//		return (SKey)readObject();
//	}
}
