// Copyright © 2012-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;


/** Base class for Object-level serialization */
public abstract class PersistenceHandler
	extends PrimitiveHandler
{
	public abstract Object load(PrimitiveInput in) throws Exception;

	public abstract void store(Object x, PrimitiveOutput out) throws Exception;
	
	//
	
	private String typeID;
	private static ThreadLocal<PersistenceHandler> currentlyLoading = new ThreadLocal();
	
	
	public PersistenceHandler()
	{
		super(Persister.OBJECT);
	}

	
	public String getTypeID()
	{
		return typeID;
	}
	
	
	public boolean isPrimitive()
	{
		return false;
	}
	
	
	// in Persister.register()
	void setTypeID(String id)
	{
		this.typeID = id;
	}
	
	
	protected void storeWithID(Object x, PrimitiveOutputStream out) throws Exception
	{
		out.writeRawByte(Persister.OBJECT);
		out.writeRawString(typeID);
		store(x, out);
	}


	public Object read(PrimitiveInputStream in) throws Exception
	{
		// this is a hack designed to make it easier to write classes that extend KRecord
		// so it only has to have a no-arg constructor.  The problem is that any such object 
		// needs to be added to a transaction, if one exists, but not when this object is being
		// loaded from the store.
		currentlyLoading.set(this);
		Object x = load(in);
		currentlyLoading.set(null);
		return x;
	}


	public void write(Object x, PrimitiveOutputStream out) throws Exception
	{
		store(x, out);
	}
	
	
	public static Object getCurrentlyLoadingHack()
	{
		return currentlyLoading.get();
	}
}
