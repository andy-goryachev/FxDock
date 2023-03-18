// Copyright © 2022-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.table;
import goryachev.common.util.CKit;
import goryachev.fx.CssLoader;
import goryachev.fx.FX;
import goryachev.fx.FxObject;
import goryachev.fx.internal.CssHack;
import goryachev.fx.internal.CssTools;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.text.Font;


/**
 * This Table uses canvas to paint monospaced rich text,
 * similarly to FxTextEditor.
 * 
 * Renders styled text encapsulated in IStyledText, 
 * or any other object's toString representation as plain text.
 */
public class FxTextTable<T>
	extends FxTable<T>
{
	protected final FxObject<Font> fontProperty = new FxObject(Font.font("Monospace", 12));
	protected final FxObject<Insets> cellPaddingProperty = new FxObject<>();
	private static final Object TABLE_FONT_KEY = new Object();
	private static final Object TABLE_CELL_PADDING_KEY = new Object();
	private static final Object TABLE_ROW_HEIGHT_KEY = new Object();
	// TODO find out where the borders are used, if any
	public static final double FUDGE = 2;
	
	
	public FxTextTable()
	{
		init();
	}
	
	
	public FxTextTable(ObservableList<T> items)
	{
		super(items);
		init();
	}
	
	
	private void init()
	{
		FX.addChangeListener(fontProperty, this::updateFontHeightPadding);
		FX.addChangeListener(cellPaddingProperty, this::updateFontHeightPadding);
		FX.addChangeListener(getColumns(), this::updateColumnCellFactory);
	}
	
	
	protected void updateColumnCellFactory(ListChangeListener.Change ch)
	{
		while(ch.next())
		{
			ch.
				getAddedSubList().
				forEach((c) ->
				{
					if(c instanceof FxTableColumn)
					{
						FxTableColumn tc = (FxTableColumn)c;
						if(tc.getRenderer() == null)
						{
							tc.setRenderer((tcell, item) ->
							{
								// FIX not?
								HPos align = tc.getAlignment().getHpos();
								return new CanvasTextTableCell(this, item, align);
							});
						}
					}
					else
					{
						throw new Error("not an FxTableColumn: " + c); 
					}
				});
		}
	}
	
	
	public FxObject<Font> fontProperty()
	{
		return fontProperty;
	}
	
	
	public Font getFont()
	{
		return fontProperty.get();
	}
	

	public void setFont(Font f)
	{
		fontProperty.set(f);
	}
	
	
	public void setCellPadding(Insets m)
	{
		cellPaddingProperty().set(m);
	}
	
	
	public Insets getCellPadding()
	{
		return cellPaddingProperty.get();
	}
	
	
	public FxObject<Insets> cellPaddingProperty()
	{
		return cellPaddingProperty;
	}
	
	
	/** font, row height, and cell padding are related.  this method updates individual styles when necessary */
	protected void updateFontHeightPadding()
	{
		// https://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html#cell
		// https://bugs.openjdk.java.net/browse/JDK-8092402
		// open since 2011

		//		.table-row-cell 
//		{
//			-fx-cell-size:2.5px;
//		    -fx-padding: 0;
//		}
//		
//		.table-cell 
//		{
//		    -fx-padding: 0;
//		}
		
		boolean updated = false;
		
		// row height: set property overrides (font height + cell padding)
		Font f = getFont();
		double targetHeight = f.getSize() + FUDGE; // TODO + padding
		double currentHeight = currentRowHeight();
		if(targetHeight != currentHeight)
		{
			updateRowHeight(targetHeight);
			updated = true;
		}
		
		// font
		Font targetFont = getFont();
		Font currentFont = currentFont();
		if(CKit.notEquals(targetFont, currentFont))
		{
			updateFont(targetFont);
			updated = true;
		}
		
		// cell padding
		Insets targetPadding = getCellPadding();
		Insets currentPadding = currentCellPadding();
		if(CKit.notEquals(targetPadding, currentPadding))
		{
			updateCellPadding(targetPadding);
			updated = true;
		}
		
		if(updated)
		{
			CssLoader.updateStyles();
		}
	}
	
	
	protected double currentRowHeight()
	{
		CssHack<Double> h = CssHack.get(table, TABLE_ROW_HEIGHT_KEY);
		if(h == null)
		{
			return 0;
		}
		else
		{
			return h.doubleValue();
		}
	}
	
	
	// FIX getting https://stackoverflow.com/questions/47939990/java-error-on-css
	// https://stackoverflow.com/questions/27311222/javafx-getting-class-cast-exception-in-css-for-blend-mode
	// https://bugs.openjdk.java.net/browse/JDK-8088468
	// https://bugs.openjdk.java.net/browse/JDK-8097038  *loop
	protected void updateRowHeight(double size)
	{
		CssHack.remove(table, TABLE_ROW_HEIGHT_KEY);

		// FIX table row height is incorrect with fractional font sizes
		if(size > 0)
		{
			String val = CssTools.format("%spt", size);
			String name = CssHack.generateName("FxTableRowHeight", val);
			String css = String.format(".%s .table-row-cell { -fx-cell-size:%s; }", name, val);
			
			CssHack<Double> h = new CssHack<Double>(name, css, size, size);
			h.attachTo(table, TABLE_ROW_HEIGHT_KEY);
		}
	}
	
	
	protected Insets currentCellPadding()
	{
		CssHack<Insets> h = CssHack.get(table, TABLE_CELL_PADDING_KEY);
		if(h == null)
		{
			return null;
		}
		else
		{
			return h.getValue();
		}
	}
	
	
	protected void updateCellPadding(Insets p)
	{
		CssHack.remove(table, TABLE_CELL_PADDING_KEY);

		if(p != null)
		{
			String val = CssTools.format("%1s %2s %3s %4s", p.getTop(), p.getRight(), p.getBottom(), p.getLeft());
			String name = CssHack.generateName("FxTableCellPadding", val);
			String css = String.format(".%1$s .table-row-cell { -fx-padding:%2$s; }\n.%1$s .table-cell { -fx-padding:%2$s; }", name, val);

			CssHack<Insets> h = new CssHack<Insets>(name, css, p, 0);
			h.attachTo(table, TABLE_CELL_PADDING_KEY);
		}
	}
	
	
	protected Font currentFont()
	{
		CssHack<Font> h = CssHack.get(table, TABLE_FONT_KEY);
		if(h == null)
		{
			return null;
		}
		else
		{
			return h.getValue();
		}
	}
	
	
	protected void updateFont(Font f)
	{
		CssHack.remove(table, TABLE_FONT_KEY);
		
		if(f != null)
		{
			String family = f.getFamily();
			String style = f.getStyle();
			double size = f.getSize();
			
			String nm = String.format("%s %s %.1f", family, style, size);
			String name = CssHack.generateName("FxTableFont", nm);
			String css = CssTools.format(".%1s .table-cell { -fx-font-family:%2s; -fx-font-style:%3s; -fx-font-size:%4spt; }", name, family, style, size);

			CssHack<Font> h = new CssHack<Font>(name, css, f, 0);
			h.attachTo(table, TABLE_FONT_KEY);
		}
	}
}
