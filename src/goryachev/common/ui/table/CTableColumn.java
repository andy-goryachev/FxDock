// Copyright (c) 2007-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.ui.CAlignment;
import java.util.Comparator;
import java.util.EventObject;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


// TODO change CTreeTable to use a different column class
// and kill this
public abstract class CTableColumn<T>
	extends CTableRenderer
{
	// configures table header 
	public void configureHeader(TableColumn c) { }

	// configure header renderer
	public void configureHeaderRenderer(CTableHeaderRenderer r) { }
	
	// returns cell text, which will be used for text search and highlighting
	public abstract String getText(T item);
	
	// override for icons, colors, etc.
	public void configureRenderer(T item) { }
	
	// returns sortable object - uses CTableSorter
	// FIX ModelWrapper
	// FIX return Object instead
	// FIX seems to appear in sorting but without formatting
	public Comparable<?> getSortable(T item) { return getText(item); }
	
	// FIX replace with getSortingObject and use a normal comparator
	public Comparator<?> getSorter() { return null; }
	
	public TableCellRenderer getRenderer() { return this; }
	
	public boolean isSortable() { return true; }
	
	// implement set() if editable (only for CTable)
	public boolean isEditable(int row) { return isEditable(); }
	
	// implement set() if editable (only for CTable)
	public boolean isEditable() { return false; }
	
	// override for editable columns
	// and also return true from isEditable()
	public void set(T d, Object value) { }

	// override, although unused	
	public Class<?> getType() { return Object.class; }
	
	// TODO kill
	protected final void getFixedWidth() { }
	protected final void get(T d) { }
	
	
	//
	

	private String name;
	private int minWidth;
	private int prefWidth;
	private int maxWidth;
	private CAlignment align;
	private DefaultCellEditor editor;
	
	
	public CTableColumn(int minWidth, int prefWidth, int maxWidth, CAlignment align, String name)
	{
		this.name = name;
		this.minWidth = minWidth;
		this.prefWidth = prefWidth;
		this.maxWidth = maxWidth;
		this.align = align;
	}
	
	
	public CTableColumn(int prefWidth, String name)
	{
		this(-1, prefWidth, -1, CAlignment.LEADING, name);
	}
	
	
	public CTableColumn(CAlignment align, String name, int fixedWidth)
	{
		this(fixedWidth, fixedWidth, fixedWidth, align, name);
	}
	
	
	public CTableColumn(String name, int fixedWidth)
	{
		this(fixedWidth, fixedWidth, fixedWidth, CAlignment.LEADING, name);
	}
	
	
	public CTableColumn(CAlignment align, String name)
	{
		this(-1, -1, -1, align, name);
	}

	
	public CTableColumn(int prefWidth, CAlignment align, String name)
	{
		this(-1, prefWidth, -1, align, name);
	}
	
	
	public CTableColumn(String name)
	{
		this(-1, -1, -1, CAlignment.LEADING, name);
	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	public int getHorizontalAlignment()
	{
		return align.getAlignment();
	}
	

	public int getMinimumWidth()
	{
		return minWidth;
	}
	
	
	public int getMaximumWidth()
	{
		return maxWidth;
	}
	
	
	public int getPreferredWidth()
	{
		return prefWidth;
	}
	
	
	public JComponent getEditor()
	{
		return new JTextField();
	}


	public final TableCellEditor getCellEditor()
	{
		if(editor == null)
		{
			JComponent ed = getEditor();
			if(ed instanceof JTextField)
			{
				editor = new DefaultCellEditor((JTextField)ed)
				{
					// http://stackoverflow.com/questions/6790150/editable-jtables-action-of-first-key-typed-in-a-text-field
					public boolean shouldSelectCell(EventObject ev)
					{
						return true;
					}
				};
				ed.setBorder(BORDER);
			}
			else if(ed instanceof JComboBox)
			{
				editor = new DefaultCellEditor((JComboBox)ed);
				ed.setBorder(BORDER);
			}
			else if(ed instanceof JCheckBox)
			{
				editor = new DefaultCellEditor((JCheckBox)ed);
				ed.setBorder(BORDER);
			}
		}
		return editor;
	}
	

	public int getMinWidth()
	{
		return minWidth;
	}


	public void setMinWidth(int w)
	{
		minWidth = w;
	}
	

	public int getMaxWidth()
	{
		return maxWidth;
	}


	public void setMaxWidth(int w)
	{
		maxWidth = w;
	}


	public int getPrefWidth()
	{
		return prefWidth;
	}


	public void setPrefWidth(int w)
	{
		prefWidth = w;
	}


	public void setAlignment(CAlignment a)
	{
		align = a;
	}


	public CAlignment getAlignment()
	{
		return align;
	}
}