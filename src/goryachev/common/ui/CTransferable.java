// Copyright (c) 2010-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.table.ZTable;
import goryachev.common.util.CKit;
import goryachev.common.util.CMap;
import goryachev.common.util.Parsers;
import goryachev.common.util.Rex;
import goryachev.common.util.SB;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JTable;


public class CTransferable
	implements Transferable
{
	public static final DataFlavor HTML_STRING = UI.createDataFlavor("text/html;class=java.lang.String");
	public static final DataFlavor HTML_READER = UI.createDataFlavor("text/html;class=java.io.Reader");
	public static final DataFlavor PLAIN_STRING = UI.createDataFlavor("text/plain;class=java.lang.String");
	public static final DataFlavor PLAIN_READER = UI.createDataFlavor("text/plain;class=java.io.Reader");
	public static final DataFlavor RTF_STREAM = UI.createDataFlavor("text/rtf;class=java.io.InputStream");
	public static final DataFlavor LOCAL_STRING = UI.createLocalObjectFlavor(String.class);
	public static final DataFlavor STRING = DataFlavor.stringFlavor;
	
	private CMap<DataFlavor,Object> data = new CMap();
	
	
	public CTransferable()
	{
	}
	
	
	public static boolean isPlainTextFlavor(DataFlavor f)
	{
		if(STRING.equals(f))
		{
			return true;
		}
		else if(LOCAL_STRING.equals(f))
		{
			return true;
		}
		else if(PLAIN_STRING.equals(f))
		{
			return true;
		}
		else if(PLAIN_READER.equals(f))
		{
			return true;
		}
		return false;
	}
	
	
	public void addTextPlain(String s)
	{
		add(PLAIN_STRING, s);
		add(DataFlavor.stringFlavor, s);
	}
	
	
//	public void addTextPlain(JTextComponent t)
//	{
//		int pos0 = t.getSelectionStart();
//		int pos1 = t.getSelectionEnd();
//		
//		if(pos0 != pos1)
//		{
//			try
//			{
//				addTextPlain(t.getDocument().getText(pos0, pos1 - pos0));
//			}
//			catch(Exception e)
//			{
//				Log.err(e);
//			}
//		}
//	}
	
	
	public void addTextHtml(String s)
	{
		add(HTML_STRING, s);
	}
	
	
	public void addTextRtf(String s)
	{
		add(RTF_STREAM, s);
	}
	
	
	public void add(DataFlavor f, Object x)
	{
		if(data.put(f, x) != null)
		{
			throw new Rex("flavor is already present: " + f);
		}
	}
	

	public DataFlavor[] getTransferDataFlavors()
	{
		Set<DataFlavor> fs = data.keySet();
		return fs.toArray(new DataFlavor[fs.size()]);
	}


	public boolean isDataFlavorSupported(DataFlavor f)
	{
		return data.containsKey(f);
	}


	public Object getTransferData(DataFlavor f) throws UnsupportedFlavorException, IOException
	{
		Object x = data.get(f);
		if(x == null)
		{
			throw new UnsupportedFlavorException(f);
		}
		else if(x instanceof String)
		{
			String s = (String)x;
			if(String.class.equals(f.getRepresentationClass()))
			{
				return s;
			}
			else if(Reader.class.equals(f.getRepresentationClass()))
			{
				return new StringReader(s);
			}
		}
		return x;
	}


	public void addTable(JComponent comp)
	{
		if(comp instanceof JTable)
		{
			JTable t = (JTable)comp;
			int[] rows;
			int[] cols;

			if(!t.getRowSelectionAllowed() && !t.getColumnSelectionAllowed())
			{
				return;
			}

			if(!t.getRowSelectionAllowed())
			{
				int rowCount = t.getRowCount();
				rows = new int[rowCount];
				for(int i=0; i<rowCount; i++)
				{
					rows[i] = i;
				}
			}
			else
			{
				rows = t.getSelectedRows();
			}
			
			if(rows == null)
			{
				return;
			}
			if(rows.length == 0)
			{
				return;
			}

			if(!t.getColumnSelectionAllowed())
			{
				int colCount = t.getColumnCount();
				cols = new int[colCount];
				for(int i=0; i<colCount; i++)
				{
					cols[i] = i;
				}
			}
			else
			{
				cols = t.getSelectedColumns();
			}
			
			if(cols == null)
			{
				return;
			}
			if(cols.length == 0)
			{
				return;
			}

			SB text = new SB();
			SB html = new SB();
			html.a("<html>\n<body>\n<table>\n");

			for(int r=0; r<rows.length; r++)
			{
				html.a("\t<tr>\n");
				
				for(int c=0; c<cols.length; c++)
				{
					String val = getCellText(t, rows[r], cols[c]);
					if(val == null)
					{
						val = "";
					}
			
					if(c > 0)
					{
						text.a("\t\t");
					}
					text.a(val);
					
					html.a("\t\t<td>").a(val).a("</td>\n");
				}
				
				text.a("\n");
				html.a("</tr>\n");
			}

			html.a("</table>\n</body>\n</html>");

			addTextPlain(text.toString());
			addTextHtml(html.toString());
		}
	}
	
	
	protected String getCellText(JTable t, int row, int col)
	{
		if(t instanceof ZTable)
		{
			return ((ZTable)t).getCellText(row, col);
		}
		else
		{
			Object x = t.getValueAt(row, col);
			return Parsers.parseStringNotNull(x);
		}
	}
	
	
	public static boolean isHtmlFlavor(DataFlavor f)
	{
		String mime = f.getMimeType();
		if(mime.startsWith("text/html"))
		{
			return true;
		}
		return false;
	}


	public static String readString(Reader rd) throws Exception
	{
		SB sb = new SB();
		char[] buf = new char[16384];
		
		try
		{
			for(;;)
			{
				int count = rd.read(buf);
				if(count < 0)
				{
					return sb.toString();
				}
				
				String s = new String(buf, 0, count);
				sb.a(s);
			}
		}
		finally
		{
			CKit.close(rd);
		}
	}
}