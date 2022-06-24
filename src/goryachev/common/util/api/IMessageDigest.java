// Copyright © 2021-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.api;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Message Digest Interface.
 */
public interface IMessageDigest
{
	public void update(byte b);
	

	public void update(byte[] buf, int offset, int length);

	
	public void reset();
	

	public byte[] digest();
	
	
	//


	/** returns IMessageDigest based java.security implementation, may throw an Error */
	public static IMessageDigest getInstance(String algorithm)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance(algorithm);
			
			return new IMessageDigest()
			{
				public void update(byte[] buf, int offset, int length)
				{
					md.update(buf, offset, length);
				}
				
				
				public void update(byte b)
				{
					md.update(b);
				}
				
				
				public void reset()
				{
					md.reset();
				}
				
				
				public byte[] digest()
				{
					return md.digest();
				}
			};
		}
		catch(NoSuchAlgorithmException e)
		{
			throw new Error(e);
		}
	}
}
