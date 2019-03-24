// Copyright © 2011-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;


public interface PrimitiveInput
	extends Closeable
{
	public void close() throws IOException;
	
	public Object readObject() throws Exception;
	
	public boolean readBool() throws Exception;
	
	public Boolean readBoolean() throws Exception;
	
	public Byte readByte() throws Exception;
	
	public byte[] readByteArray() throws Exception;
	
	public Short readShort() throws Exception;
	
	public int readInt() throws Exception;
	
	public Integer readInteger() throws Exception;
	
	public Long readLong() throws Exception;
	
	public Float readFloat() throws Exception;
	
	public Double readDouble() throws Exception;
	
	public String readString() throws Exception;
	
	public BigInteger readBigInteger() throws Exception;
	
	public BigDecimal readBigDecimal() throws Exception;
}
