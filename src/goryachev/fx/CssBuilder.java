// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.SB;


/**
 * CssBuilder.
 */
public class CssBuilder
{
	enum State
	{
		IDLE,
		PROPERTY,
		SELECTOR,
		VALUE
	}
	
	private final Object[] items;
	private State state;
	private SB sb;
	
	
	public CssBuilder(Object[] items)
	{
		this.items = items;
	}
	
	
	public String build()
	{
		state = State.IDLE;
		
		sb = new SB(16384);
		
		for(Object x: items)
		{
			if(x instanceof CssID)
			{
				writeID((CssID)x);
				state = State.SELECTOR;
			}
			else if(x instanceof CssProperty)
			{
				writeProperty((CssProperty)x);
				state = State.PROPERTY;
			}
			else if(x instanceof CssPseudo)
			{
				writePseudo((CssPseudo)x);
				state = State.SELECTOR;
			}
			else if(x instanceof CssStyle)
			{
				writeStyle((CssStyle)x);
				state = State.SELECTOR;
			}
			else if(x instanceof String)
			{
				state = writeString(x.toString());
			}
			else
			{
				writePropertyValue(x);
				state = State.PROPERTY;
			}
		}
		
		switch(state)
		{
		case IDLE:
			break;
		case PROPERTY:
			sb.a("\n}\n");
			break;
		case SELECTOR:
			sb.a("\n{\n}\n");
			break;
		case VALUE:
			throw new Error("missing property value");
		default:
			throw new Error("?" + state);
		}
		
		return sb.toString();
	}
	
	
	protected void writeStyle(CssStyle st)
	{
		switch(state)
		{
		case IDLE:
			sb.a(".").a(st.getName());
			break;
		case PROPERTY:
			sb.a("\n}\n\n.").a(st.getName());
			break;
		case SELECTOR:
			sb.a(" .").a(st.getName());
			break;
		default:
			throw new Error("unexpected style: " + st);
		}
	}
	
	
	protected void writePseudo(CssPseudo p)
	{
		switch(state)
		{
		case SELECTOR:
			sb.a(p.getName());
			break;
		default:
			throw new Error("unexpected pseudo style: " + p);
		}
	}
	
	
	protected void writeID(CssID id)
	{
		switch(state)
		{
		case IDLE:
			sb.a("#").a(id.getID());
			break;
		case PROPERTY:
			sb.a("\n#").a(id.getID());
			break;
		case SELECTOR:
			sb.a(" #").a(id.getID());
			break;
		default:
			throw new Error("unexpected id: " + id);
		}
	}
	
	
	protected void writeProperty(CssProperty p)
	{
		switch(state)
		{
		case PROPERTY:
			sb.a("\n\t").a(p.getName()).a(": ").a(p.getValue()).a(";");
			break;
		case SELECTOR:
			sb.a("\n{\n\t").a(p.getName()).a(": ").a(p.getValue()).a(";");
			break;
		default:
			throw new Error("unexpected property: " + p);
		}
	}
	
	
	protected void writePropertyValue(Object x)
	{
		switch(state)
		{
		case VALUE:
			sb.a(x).a(";");
			break;
		default:
			throw new Error("unexpected property value: " + x);
		}
	}
	
	
	protected State writeString(String s)
	{
		if(s == null)
		{
			return state;
		}
		
		// TODO check if no more than one colon and no more than one semicolon, and also that colon is before semicolon
		
		boolean colon = s.contains(":");
		boolean semicolon = s.contains(";");
		
		if(colon || semicolon)
		{
			// it's a property
			switch(state)
			{
			case PROPERTY:
				sb.a("\n\t").a(s);
				break;
			case SELECTOR:
				sb.a("\n{\n\t").a(s);
				break;
			case VALUE:
				if(colon)
				{
					throw new Error("expected property name and value: " + s);
				}
				else
				{
					sb.a(" ").a(s);
				}
			default:
				throw new Error("unexpected property: " + s);
			}
			
			return semicolon ? State.PROPERTY : State.VALUE;
		}
		else
		{
			// it's a selector
			switch(state)
			{
			case IDLE:
				sb.a(s);
				break;
			case PROPERTY:
				sb.a("\n}\n\n\t").a(s);
				break;
			case SELECTOR:
				sb.a(" ").a(s);
				break;
			default:
				throw new Error("unexpected selector: " + s);
			}
			
			return State.SELECTOR;
		}
	}
}
