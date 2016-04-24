// Copyright (c) 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.dialogs.license;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CDialog;
import goryachev.common.ui.CScrollPane;
import goryachev.common.ui.CSplitPane;
import goryachev.common.ui.CTextFieldWithPrompt;
import goryachev.common.ui.ClipboardTools;
import goryachev.common.ui.Menus;
import goryachev.common.ui.Theme;
import goryachev.common.ui.table.CTableSelector;
import goryachev.common.ui.table.ZColumnHandler;
import goryachev.common.ui.table.ZModel;
import goryachev.common.ui.table.ZTable;
import goryachev.common.ui.text.CDocument;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.Document;


public class MultiPageDialog
	extends CDialog
{
	public final CTextFieldWithPrompt searchField;
	public final ZModel<Entry> model;
	public final ZTable table;
	public final CTableSelector selector;
	public final JTextPane textField;
	public final CSplitPane split;
	
	//
	
	protected static class Entry
	{
		public String title;
		public Document text;
	}
	
	//
	
	public MultiPageDialog(Component parent, String name)
	{
		super(parent, name, true);
		
		setSize(700, 700);
		borderless();
		
		searchField = new CTextFieldWithPrompt();
		searchField.setPrompt(Menus.find);
		searchField.setPreferredSize(new Dimension(200, -1));
		
		model = new ZModel();
		model.addColumn("y", new ZColumnHandler<Entry>()
		{
			public Object getCellValue(Entry x) { return x.title; }
		});
		
		table = new ZTable(model);
		table.setTableHeader(null);
		
		selector = new CTableSelector(table)
		{
			public void tableSelectionChangeDetected()
			{
				Entry en = model.getSelectedEntry(this);
				openPage(en);
			}
		};
		
		textField = new JTextPane();
		textField.setEditable(false);
		
		CScrollPane tableScroll = new CScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		tableScroll.setBorder(CBorder.NONE);
		tableScroll.setViewportBorder(CBorder.NONE);
		tableScroll.getViewport().setBackground(Theme.TEXT_BG);
		
		CScrollPane detailScroll = new CScrollPane(textField, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		detailScroll.setBorder(CBorder.NONE);
		detailScroll.setViewportBorder(CBorder.NONE);
		detailScroll.getViewport().setBackground(Theme.TEXT_BG);
		
		split = new CSplitPane(true, tableScroll, detailScroll);
		split.setDividerLocation(200);
		split.setBorder(null);
		
		panel().setCenter(split);
	}
	

	public void onWindowOpened()
	{
		table.changeSelection(0, 0, false, false);
	}


	protected void setToolbar(JComponent c)
	{
		panel().setNorth(c);
	}
	
	
	protected void onPrint()
	{
		// TODO
	}
	
	
	protected void onCopy()
	{
		ClipboardTools.copy(textField.getText());
	}
	
	
	protected void onSave()
	{
		// TODO
	}
	
	
	protected void openPage(Entry en)
	{
		if(en != null)
		{
			textField.setDocument(en.text);
			textField.setCaretPosition(0);
		}
	}
	
	
	// TODO add page html
	public void addPage(String title, String text)
	{
		CDocument d = new CDocument();
		d.add(text);
		addPage(title, d);
	}
	
	
	public void addPage(String title, Document text)
	{
		Entry en = new Entry();
		en.title = title;
		en.text = text;
		
		model.addItem(en);
	}
	
	
	public void autoResizeSplit()
	{
		int w = table.getColumnsPreferredWidth();
		split.setDividerLocation(w);
	}
}
