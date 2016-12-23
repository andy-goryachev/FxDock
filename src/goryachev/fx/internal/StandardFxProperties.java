// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.internal;
import goryachev.fx.CssPseudo;
import javafx.scene.control.ScrollPane;


/**
 * Standard Fx Css Properties.
 * 
 * border:  -fx-border-color, -fx-border-insets, -fx-border-radius, -fx-border-style, -fx-border-width, -fx-border-image-insets, -fx-border-image-repeat, -fx-border-image-slice, -fx-border-image-source, -fx-border-image-width
 * background:  -fx-background-color, -fx-background-image, -fx-background-insets, -fx-background-position, -fx-background-radius, -fx-background-repeat, -fx-background-size
 * 
 * text input:
 * -fx-highlight-fill 0x0096c9ff
 * -fx-highlight-text-fill 0xffffffff
 */
public class StandardFxProperties
{
	public static final CssPseudo DISABLED = new CssPseudo(":disabled");
	public static final CssPseudo FOCUSED = new CssPseudo(":focused");
	public static final CssPseudo HOVER = new CssPseudo(":hover");
	public static final CssPseudo PRESSED = new CssPseudo(":pressed");
	
	public static final String TRANSPARENT = "transparent";
	

	// B
	public static FxCssProp backgroundColor(Object x) { return new FxCssProp("-fx-background-color", CssTools.toColor(x)); }
	public static FxCssProp backgroundImage(Object x) { return new FxCssProp("-fx-background-image", CssTools.toValue(x)); }
	public static FxCssProp backgroundInsets(Object x) { return new FxCssProp("-fx-background-insets", CssTools.toValue(x)); }
	public static FxCssProp backgroundRadius(Object x) { return new FxCssProp("-fx-background-radius", CssTools.toValue(x)); }
	/** A series of paint values or sets of four paint values, separated by commas. For each item in the series, if a single paint value is specified, then that paint is used as the border for all sides of the region; and if a set of four paints is specified, they are used for the top, right, bottom, and left borders of the region, in that order. If the border is not rectangular, only the first paint value in the set is used. */
	public static FxCssProp borderColor(Object x) { return new FxCssProp("-fx-border-color", CssTools.toColor(x)); }
	/** A series of paint values or sets of four paint values, separated by commas. For each item in the series, if a single paint value is specified, then that paint is used as the border for all sides of the region; and if a set of four paints is specified, they are used for the top, right, bottom, and left borders of the region, in that order. If the border is not rectangular, only the first paint value in the set is used. */
	public static FxCssProp borderColor(Object ... xs) { return new FxCssProp("-fx-border-color", CssTools.toColors(xs)); }
	/** A series of width or sets of four width values, separated by commas. For each item in the series, a single width value means that all border widths are the same; and if a set of four width values is specified, they are used for the top, right, bottom, and left border widths, in that order. If the border is not rectangular, only the first width value is used. Each item in the series of widths applies to the corresponding item in the series of border colors.  */
	public static FxCssProp borderWidth(Object x) { return new FxCssProp("-fx-border-width", CssTools.toValue(x)); }
	/** A series of width or sets of four width values, separated by commas. For each item in the series, a single width value means that all border widths are the same; and if a set of four width values is specified, they are used for the top, right, bottom, and left border widths, in that order. If the border is not rectangular, only the first width value is used. Each item in the series of widths applies to the corresponding item in the series of border colors.  */
	public static FxCssProp borderWidth(Object ... xs) { return new FxCssProp("-fx-border-width", CssTools.toValues(xs)); }

	// C
	public static FxCssProp cellSize(Object x) { return new FxCssProp("-fx-cell-size", x); }
	
	// F
	public static FxCssProp fitToHeight(boolean x) { return new FxCssProp("-fx-fit-to-height", x); }
	public static FxCssProp fitToWidth(boolean x) { return new FxCssProp("-fx-fit-to-width", x); }
	public static FxCssProp fontSize(Object x) { return new FxCssProp("-fx-font-size", x); }
	/** [ normal | bold | bolder | lighter | 100 | 200 | 300 | 400 | 500 | 600 | 700 | 800 | 900 ] */
	public static FxCssProp fontWeight(Object x) { return new FxCssProp("-fx-font-weight", x); }
	// H
	public static FxCssProp hBarPolicy(ScrollPane.ScrollBarPolicy x) { return new FxCssProp("-fx-hbar-policy", CssTools.toValue(x)); }
	// M
	public static FxCssProp maxHeight(double x) { return new FxCssProp("-fx-max-height", x); }
	public static FxCssProp maxWidth(double x) { return new FxCssProp("-fx-max-width", x); }
	public static FxCssProp minHeight(double x) { return new FxCssProp("-fx-min-height", x); }
	public static FxCssProp minWidth(double x) { return new FxCssProp("-fx-min-width", x); }
	// O
	public static FxCssProp opacity(double x) { return new FxCssProp("-fx-opacity", x); }
	// P
	public static FxCssProp padding(Object x) { return new FxCssProp("-fx-padding", CssTools.toValue(x)); }
	public static FxCssProp prefHeight(double x) { return new FxCssProp("-fx-pref-height", x); }
	public static FxCssProp prefWidth(double x) { return new FxCssProp("-fx-pref-width", x); }
	// R
	public static FxCssProp regionBackground(Object x) { return new FxCssProp("-fx-region-background", CssTools.toValue(x)); }
	// S
	public static FxCssProp shape(Object x) { return new FxCssProp("-fx-shape", x); }
	// T
	public static FxCssProp textFill(Object x) { return new FxCssProp("-fx-text-fill", CssTools.toColor(x)); }
	public static FxCssProp translateX(double x) { return new FxCssProp("-fx-translate-x", x); }
	public static FxCssProp translateY(double x) { return new FxCssProp("-fx-translate-y", x); }
	
	// V
	public static FxCssProp vBarPolicy(ScrollPane.ScrollBarPolicy x) { return new FxCssProp("-fx-vbar-policy", CssTools.toValue(x)); }
	
	
	//
	
	
	public static FxCssProp prop(String name, Object val) { return new FxCssProp(name, val); }
}
