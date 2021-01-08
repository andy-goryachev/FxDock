// Copyright © 2014-2021 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.io.PRecord;
import goryachev.common.io.Persister;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Locale;


/** Simple licensing mechanism based on DSA digital signature */
public final class SimpleLicense
{
	/** error code for when the signature algorithm not available or other problem with JCE */
	public static final String ERR_ALGORITHM_FAILED = "ERR_ALGORITHM_FAILED";
	/** error code for improperly encoded license key text */ 
	public static final String ERR_BAD_LICENSE_TEXT = "ERR_BAD_LICENSE_TEXT";
	/** error code for failure to load public key */
	public static final String ERR_BAD_PUBLIC_KEY = "ERR_BAD_PUBLIC_KEY";
	/** signature is invalid */
	public static final String ERR_INVALID_SIGNATURE = "ERR_INVALID_SIGNATURE";

	public static final String ALGORITHM = "SHA1withDSA";
	public static final String SIGNER = "DSA";
	public static final int KEY_SIZE = 1024;
	public static final byte DIVIDER = 0;
	public static final String KEY_EMAIL = "e";
	public static final String KEY_NAME = "n";
	public static final String KEY_PRODUCT = "p";
	public static final String KEY_SIGNATURE = "s";
	public static final String PREFIX = "BEGIN>>>";
	public static final String SUFFIX = "<<<END";
	
	private final String name;
	private final String email;
	private final String product;
	private final byte[] sig;
	

	private SimpleLicense(String licenseKey) throws Exception
	{
		// decode license
		PRecord r;
		try
		{
			int start = licenseKey.lastIndexOf(PREFIX);
			int end = licenseKey.lastIndexOf(SUFFIX);
			String s = licenseKey.substring(start + PREFIX.length(), end);
			byte[] b = Hex.parseByteArray(s);
			r = (PRecord)Persister.read(b);
			
			// see pack()
			name = r.getString(KEY_NAME);
			email = r.getString(KEY_EMAIL);
			product = r.getString(KEY_PRODUCT);
			sig = (byte[])r.getAttribute(KEY_SIGNATURE);
		}
		catch(Exception e)
		{
			throw new Exception(ERR_BAD_LICENSE_TEXT, e);
		}
	}
	
	
	public static SimpleLicense parse(String s)
	{
		try
		{
			SimpleLicense lic = new SimpleLicense(s);
			if(CKit.isNotBlank(lic.getEmail()))
			{
				return lic;
			}
		}
		catch(Exception e)
		{ }
		
		return null;
	}
	
	
	public String getLicensedTo()
	{
		return name;
	}
	
	
	public String getEmail()
	{
		return email;
	}
	
	
	public String getProduct()
	{
		return product;
	}
	
	
	public void verify(InputStream publicKeyStream) throws Exception
	{
		// public key
		PublicKey pubKey;
		try
		{
			byte[] encoded = CKit.readBytes(publicKeyStream);
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encoded);
			KeyFactory kf = KeyFactory.getInstance(SIGNER);
			pubKey = kf.generatePublic(pubKeySpec);
		}
		catch(Exception e)
		{
			throw new Exception(ERR_BAD_PUBLIC_KEY, e);
		}
		finally
		{
			CKit.close(publicKeyStream);
		}
		
		// algorithm
		Signature sg;
		try
		{
			sg = Signature.getInstance(ALGORITHM);
			sg.initVerify(pubKey);
		}
		catch(Exception e)
		{
			throw new Exception(ERR_ALGORITHM_FAILED, e);
		}
		
		boolean valid;
		try
		{
			update(sg, name, email, product);
			valid = sg.verify(sig);
		}
		catch(Exception e)
		{
			throw new Exception(ERR_INVALID_SIGNATURE, e);
		}
		
		if(!valid)
		{
			throw new Exception(ERR_INVALID_SIGNATURE);
		}
	}
	
	
	protected static String wrap(String s, int columns)
	{
		SB sb = new SB(s.length() + 20);
		
		int start = 0;
		while(start < s.length())
		{
			int end = start + columns;
			end = Math.min(end, s.length());

			sb.append(s.substring(start, end));
			sb.nl();
			
			start = end;
		}
		
		return sb.toString();
	}
	
	
	public static String pack(String name, String email, String product, byte[] sig) throws Exception
	{
		PRecord r = new PRecord();
		r.setAttribute(KEY_NAME, name);
		r.setAttribute(KEY_EMAIL, email);
		r.setAttribute(KEY_PRODUCT, product);
		r.setAttribute(KEY_SIGNATURE, sig);
		
		byte[] b = Persister.write(r);
		String s = Hex.toHexString(b);
		
		return PREFIX + "\n" + wrap(s, 40) + SUFFIX;
	}


	public static void update(Signature sg, String name, String email, String product) throws Exception
	{
		sg.update(name.trim().getBytes(CKit.CHARSET_UTF8));
		sg.update(DIVIDER);
		sg.update(email.trim().toUpperCase(Locale.US).getBytes(CKit.CHARSET_UTF8));
		sg.update(DIVIDER);
		sg.update(product.getBytes(CKit.CHARSET_UTF8));
	}
}
