// Copyright © 2013-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.BitSet;


/** Bloom filter based on 32 bit Murmur 3 hash function. */ 
public class BloomFilterMurmur3
{
	private static final double LN2 = Math.log(2);
	private final int hashFunctionCount;
	private final int bitSetSize;
	private final BitSet bits;

	
	public BloomFilterMurmur3(double falsePositiveProbability, int capacity)
	{
		this
		(
			(int)Math.ceil(-(Math.log(falsePositiveProbability) / LN2)),
			(int)Math.ceil(capacity * (Math.ceil(-(Math.log(falsePositiveProbability) / LN2)) / LN2))
		);
	}
	
	
	public BloomFilterMurmur3(int hashFunctionCount, int bitSetSize)
	{
		this.hashFunctionCount = hashFunctionCount;
		this.bitSetSize = bitSetSize;
		bits = new BitSet(bitSetSize);
	}
	
	
	public BloomFilterMurmur3(int hashFunctionCount, byte[] b)
	{
		this.hashFunctionCount = hashFunctionCount;
		this.bitSetSize = b.length * 8;
		bits = BitSet.valueOf(b);
	}
	
	
	/** Returns the number of hash functions chosen for this Bloom filter. */
	public int getHashFunctionCount()
	{
		return hashFunctionCount;
	}
	
	
	/** Returns the number of bits in the Bloom filter bit set. */
	public int getBitSetSize()
	{
		return bitSetSize;
	}
	
	
	/** Adds a byte array element to this Bloom filter. */
	public void add(byte[] data)
	{
		for(int i=0; i<hashFunctionCount; i++)
		{
			int hash = MurmurHash3.hash(data, 0, data.length, i);
			int ix = Math.abs(hash % bitSetSize);
			bits.set(ix, true);
		}
	}
	
	
	/** Adds a string element to this Bloom filter. */
	public void add(CharSequence data)
	{
		for(int i=0; i<hashFunctionCount; i++)
		{
			int hash = MurmurHash3.hash(data, 0, data.length(), i);
			int ix = Math.abs(hash % bitSetSize);
			bits.set(ix, true);
		}
	}


	/**
	 * Returns true if the byte array element could have been inserted into this Bloom filter with the given
	 * false positive probability. 
	 */
	public boolean contains(byte[] data)
	{
		for(int i=0; i<hashFunctionCount; i++)
		{
			int hash = MurmurHash3.hash(data, 0, data.length, i);
			int ix = Math.abs(hash % bitSetSize);
			
			if(!bits.get(ix))
			{
				return false;
			}
		}
		
		return true;
	}
	
	
	/**
	 * Returns true if the string element could have been inserted into this Bloom filter with the given
	 * false positive probability. 
	 */
	public boolean contains(CharSequence data)
	{
		for(int i=0; i<hashFunctionCount; i++)
		{
			int hash = MurmurHash3.hash(data, 0, data.length(), i);
			int ix = Math.abs(hash % bitSetSize);
			
			if(!bits.get(ix))
			{
				return false;
			}
		}
		
		return true;
	}
	
	
	public byte[] toByteArray()
	{
		int sz = bitSetSize / 8;
		byte[] b = bits.toByteArray();
		if(b.length == sz)
		{
			return b;
		}
		
		byte[] rv = new byte[sz];
		System.arraycopy(b, 0, rv, 0, b.length);
		return rv;
	}
}
