// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.fx.edit.FxEditor;
import goryachev.fx.internal.FxCssProp;
import javafx.scene.paint.Color;


/**
 * Common Styles.
 */
public class CommonStyles
	extends FxStyleSheet
{
	/** suppresses alternate rows styling */
	public static final CssStyle ALTERNATE_ROWS_OFF = new CssStyle("CommonStyles_ALTERNATE_ROWS_OFF");
	/** bold type face */
	public static final CssStyle BOLD = new CssStyle("CommonStyles_BOLD");
	/** disables horizontal scroll bar */
	public static final CssStyle NO_HORIZONTAL_SCROLL_BAR = new CssStyle("CommonStyles_NO_HORIZONTAL_SCROLL_BAR");

	
	public CommonStyles()
	{
		Theme theme = Theme.current();
		
		add
		(
			selector(".root").defines
			(
				// text selection
				prop("-fx-accent", FX.alpha(theme.selectedTextBG, 0.7)),
				prop("-fx-base", theme.base),
				// controls FIX
//				prop("-fx-color", theme.control),
				prop("-fx-highlight-text-fill", theme.selectedTextFG),
				// focus outline
				prop("-fx-focus-color", theme.focus),
				// focus glow
				prop("-fx-faint-focus-color", TRANSPARENT)
			),
			
			// FIX
//			checkbox(theme),						
			comboBox(theme),
			fxEditor(theme),
			menuBar(theme),
			scrollBar(theme),
			scrollPane(theme),
			table(theme),
			text(theme),
			// FIX
			//radioButton(theme),
						
			// andy's hacks
			
			selector(ALTERNATE_ROWS_OFF).defines
			(
				new FxCssProp("-fx-control-inner-background-alt", "-fx-control-inner-background")
			),

			// FX.style() bold 
			selector(BOLD).defines
			(
				fontWeight("bold")
			),
			
			// disables horizontal scroll bar
			// FIX does not disable completely
			selector(NO_HORIZONTAL_SCROLL_BAR).defines
			(
				selector(".scroll-bar:horizontal").defines
				(
					maxHeight(0),
					padding(0),
					opacity(0)
				),
				selector(".scroll-bar:horizontal *").defines
				(
					maxHeight(0),
					padding(0),
					opacity(0)
				)
			)
		);
	}
	
	
	protected Object checkbox(Theme theme)
	{
		return selector(".check-box").defines
		(
			labelPadding("0.0em 0.0em 0.0em 0.416667em"),
			textFill("-fx-text-background-color"),
			
			selector(HOVER, "> .box").defines
			(
				color(theme.control) // may be change a bit
			),
			selector(ARMED).defines
			(
				color(Color.GREEN) // "-fx-pressed-base")
			),
			
			selector(" > .box").defines
			(
//				backgroundColor(commas(R, G, B)),
//				backgroundRadius(commas(3, 2, 1)),
//				backgroundInsets(commas(3, 2, 1)),
//				padding("0.166667em 0.166667em 0.25em 0.25em"),
				
				backgroundColor(theme.control),
				backgroundInsets(3),
				backgroundRadius(3),
				padding("0.5em"),
				
				selector("> .mark").defines
				(
					backgroundColor(null),
					padding("0.4em"),
					//shape("M19 3H5c-1.11 0-2 .9-2 2v14c0 1.1.89 2 2 2h14c1.11 0 2-.9 2-2V5c0-1.1-.89-2-2-2zm-9 14l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z")
					shape("M49,119 L64,106 L100,139 L176,66 L188,78 L100,166 z")
				)
			),
			
			selector(FOCUSED, "> .box").defines
			(
//				backgroundColor("-fx-focus-color, -fx-inner-border, -fx-body-color, -fx-faint-focus-color, -fx-body-color"),
//				backgroundInsets("-0.2, 1, 2, -1.4, 2.6"),
//				backgroundRadius("3, 2, 1, 4, 1")
				
				backgroundColor(commas(theme.focus, TRANSPARENT, theme.control)),
				backgroundInsets(commas(0, 1, 3)),
				backgroundRadius(commas(3, 2, 1)),
				padding("0.5em")
				
//				selector("> .mark").defines
//				(
//					backgroundColor(null),
//					padding("0.4em")
//				)
			),
			
			selector(SELECTED, "> .box > .mark").defines
			(
				backgroundColor(theme.outline),
				backgroundInsets(0)
			),
			
			selector(":indeterminate > .box").defines
			(
				padding(0),
				
				selector("> .mark").defines
				(
					shape("M0,0H10V2H0Z"),
					scaleShape(false),
					padding("0.5em")
				)
			)
		);
	}
	
	
	protected Object comboBox(Theme theme)
	{
		return selector(".combo-box-base").defines
		(
			backgroundRadius(0),
			
			selector(EDITABLE).defines
			(
				selector("> .text-field").defines
				(
					backgroundColor(theme.textBG),
					backgroundInsets(1),
					backgroundRadius(0)
				),
				
				selector(FOCUSED).defines
				(
					backgroundColor(theme.focus),
					backgroundInsets(0),
					backgroundRadius(0),
					
					selector("> .text-field").defines
					(
						backgroundColor(theme.textBG),
						backgroundInsets(spaces(1, 0, 1, 1)),
						backgroundRadius(0),
						effect(null)
					)
				)
			),
			
			selector(FOCUSED).defines
			(
				backgroundRadius(0),
				shadow()
			)
		);
	}
	
	
	protected Object fxEditor(Theme theme)
	{
		return selector(FxEditor.PANEL).defines
		(
			backgroundColor(commas(theme.textBG, theme.textBG)),
			backgroundInsets(commas(0, 1)),
			backgroundRadius(0),

			// FIX does not work
			selector(FOCUSED).defines
			(
				backgroundColor(commas(theme.focus, theme.textBG)),
				backgroundInsets(commas(0, 1))
			)
		);
	}
	
	
	protected Object menuBar(Theme theme)
	{
		Color bg = FX.alpha(theme.focus, 0.8);
		
		return new Object[]
		{			
			selector(".menu-bar").defines
			(
				backgroundColor("-fx-background"),
				backgroundInsets(0),
				backgroundRadius(0),
				
				selector("> .container").defines
				(
					selector("> .menu-button:hover").defines
					(
						backgroundColor(bg) // TODO own color?
					),
				
					selector("> .menu-button:focused").defines
					(
						backgroundColor(bg)
					),
					
					selector("> .menu-button:showing").defines
					(
						backgroundColor(bg)
					)
				)
			),
			
			selector(".menu-item:focused").defines
			(
				backgroundColor(bg)
			)
		};
	}
	
	
	protected Object radioButton(Theme theme)
	{
		// FIX
		return new Object[]
		{
			selector(".radio-button").defines
			(
	//			padding(10),
				
				selector(".text").defines
				(
					fill("-fx-text-base-color")
				)
			),
			
			selector(".radio-button>.radio, .radio-button>.radio.unfocused, .radio-button:disabled>.radio, .radio-button:selected>.radio").defines
			(
				borderRadius(100),
				borderColor("gray"), // FIX
				borderWidth(2),
				backgroundRadius(100),
				backgroundColor(TRANSPARENT),
				padding(3)
			)
		};
	}
	
	
	protected Object shadow()
	{
		return effect("dropshadow(two-pass-box, rgba(0, 0, 0, 0.4), 12, 0, 2, 2)");
	}
	
	
	protected Object scrollBar(Theme theme)
	{
		Color fg = FX.alpha(theme.control, 0.5);
		double w = 7;
		double sp = 3;
		double r = 3;
		
		double w2 = w + sp + sp;
		
		return selector(".scroll-bar").defines
		(
			selector(":vertical").defines
			(
				maxWidth(w2),
				padding(0),
				
				selector(".thumb").defines
				(
					backgroundColor(fg),
					backgroundInsets(sp),
					backgroundRadius(r),
					maxWidth(w)
				),
				
				selector(".increment-button").defines
				(
					minWidth(0),
					maxWidth(0),
					maxHeight(0),
					prefWidth(w2),
					prefHeight(0)
				),
				
				selector(".decrement-button").defines
				(
					minWidth(0),
					maxWidth(0),
					maxHeight(0),
					prefWidth(w2),
					prefHeight(0)
				),
				
				selector(".increment-arrow").defines
				(
					minWidth(0),
					maxWidth(0),
					maxHeight(0),
					prefWidth(0),
					prefHeight(0)
				),
				
				selector(".decrement-arrow").defines
				(
					minWidth(0),
					maxWidth(0),
					maxHeight(0),
					prefWidth(0),
					prefHeight(0)
				)
			),
			
			selector(":horizontal").defines
			(
				maxHeight(w2),
				padding(0),
				
				selector(".thumb").defines
				(
					backgroundColor(fg),
					backgroundInsets(sp),
					backgroundRadius(r),
					maxHeight(w)
				),
				
				selector(".increment-button").defines
				(
					minWidth(0),
					maxWidth(0),
					maxHeight(0),
					prefHeight(w2),
					prefWidth(0)
				),
				
				selector(".decrement-button").defines
				(
					minWidth(0),
					maxWidth(0),
					maxHeight(0),
					prefHeight(w2),
					prefWidth(0)
				),
				
				selector(".increment-arrow").defines
				(
					minWidth(0),
					maxWidth(0),
					maxHeight(0),
					prefHeight(0),
					prefWidth(0)
				),
				
				selector(".decrement-arrow").defines
				(
					minWidth(0),
					maxWidth(0),
					maxHeight(0),
					prefHeight(0),
					prefWidth(0)
				)
			)
		);
	}
	
	
	protected Object scrollPane(Theme theme)
	{
		// scroll pane
		return selector(".scroll-pane").defines
		(
//			backgroundColor(theme.textBG),
//			backgroundInsets(2),
//			
//			new Selector(FOCUSED).defines
//			(
//				backgroundColor(commas(theme.focus, theme.textBG)),
//				backgroundInsets(commas(0, 1))
//			),
			
			new Selector(" > .viewport").defines
			(
				backgroundColor(theme.textBG)
			)
		);
	}
	
	
	protected Object table(Theme theme)
	{
		Color c = FX.alpha(theme.selectedTextBG, 0.15);
		Color s = FX.mix(theme.selectedTextBG, theme.textFG, 0.9);
		
		return new Object[]
		{
			selector(".table-row-cell:filled:selected").defines
			(
				backgroundColor(c),
				backgroundInsets(spaces(0, 0, 1, 0))
//				,
//				prop("-fx-table-cell-border-color", G) //"derive(" + CssTools.toColor(c) + ", 20%)")
			),
			
			selector(".table-row-cell:filled").defines
			(
//				backgroundColor(R),
//				backgroundInsets(spaces(0, 0, 1, 0))
//				,
//				prop("-fx-table-cell-border-color", G) //"derive(" + CssTools.toColor(c) + ", 20%)")
			),
			
			// hide empty table rows
			selector(".table-row-cell:empty").defines
			(
				backgroundColor(TRANSPARENT),
				borderColor(TRANSPARENT)
			),
			
			selector(".table-view > .virtual-flow > .clipped-container > .sheet > .table-row-cell .table-cell:selected").defines
			(
				backgroundColor(commas("-fx-table-cell-border-color", "-fx-background")),
				backgroundInsets(commas(0, spaces(0, 0, 1, 0)))
			)
		};
	}
	
	
	protected Object text(Theme theme)
	{
		return new Object[]
		{
			// text smoothing
			selector(".text").defines
			(
				prop("-fx-font-smoothing-type", "gray")
			),
			
			// text area
			// FIX change insets
			selector(".text-area").defines
			(
				backgroundColor(theme.textBG),
				
				selector(".content").defines
				(
					backgroundColor(theme.textBG),
					backgroundRadius(0)
				),
				
				selector(FOCUSED, ".content").defines
				(
					backgroundColor(theme.textBG),
					backgroundRadius(0),
					backgroundInsets(0)
				)
			),
			
			// fix text selection colors
			selector(".text-input").defines
			(
				backgroundInsets(commas(0, 1)),
				backgroundColor(commas(theme.outline, theme.textBG)),
				backgroundRadius(commas(0, 0)),
				
				new Selector(FOCUSED).defines
				(
					textFill(theme.textFG),
					prop("-fx-highlight-text-fill", theme.selectedTextFG),
					backgroundInsets(commas(0, 1)),
					backgroundColor(commas(theme.focus, theme.textBG)),
					backgroundRadius(commas(0, 0)),
					// TODO provide a method
					// BlurType blurType, Color color, double radius, double spread, double offsetX, double offsetY
					//effect("dropshadow(two-pass-box, rgba(0, 0, 0, 0.4), 12, 0, 2, 2)")
					shadow()
				)
			)
		};
	}
}
