// Copyright ©2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.test.TF;
import goryachev.common.test.Test;


/**
 * TestSettingsProviderBase.
 */
public class TestSettingsProviderBase
{
	public static void main(String[] args)
	{
		TF.run();
	}
	
	
//	@Test
	public void test1() throws Exception
	{
		test(new Object[] { "a", "b" });
	}
	
	
	@Test
	public void testEncoding() throws Exception
	{
		test 
		(
			null,
			"a",
			"=",
			"\\",
			",",
			",,,,",
			"k=v",
			"k=v,a,b",
			new Object[] { "a", "b" },
			new Object[] { "a", "\\,=,\\..,,,\\2E", null, null, "\\", "\\2E" }
		);
	}
	
		
	public void test(Object ... tests) throws Exception
	{
		for(Object x: tests)
		{
			if(x == null)
			{
				ts(null);
			}
			else if(x instanceof String)
			{
				ts((String)x);
			}
			else if(x instanceof Object[])
			{
				ta((Object[])x);
			}
			else
			{
				throw new Error("?" + x);
			}
		}
	}
	
	
	protected void ts(String text) throws Exception
	{
		SettingsProviderBase p = p();
		p.setString(text, text);
		String saved = p.asString();
		
		SettingsProviderBase p2 = p();
		p2.loadFromString(saved);
		String saved2 = p2.asString();
		
		TF.eq(saved, saved2);
		compare(p, p2);
	}
	
	
	protected void ta(Object[] patterns) throws Exception
	{
		SStream s = new SStream();
		for(Object x: patterns)
		{
			s.add(x);
		}
		
		String k = "array";
		SettingsProviderBase p = p();
		p.setStream(k, s);
		String saved = p.asString();
		
		SettingsProviderBase p2 = p();
		p2.loadFromString(saved);
		String saved2 = p2.asString();
		
		TF.eq(saved, saved2);
		compare(p, p2);
	}
	
	
	protected SettingsProviderBase p()
	{
		return new SettingsProviderBase()
		{
			public void save()
			{
			}
		};
	}
	
	
	protected void compare(SettingsProviderBase a, SettingsProviderBase b)
	{
		TF.eq(a.size(), b.size());
		
		for(String k: a.getKeys())
		{
			TF.isTrue(b.containsKey(k));
			
			Object va = a.getValue(k);
			Object vb = b.getValue(k);
			TF.eq(va, vb);
		}
	}
}
