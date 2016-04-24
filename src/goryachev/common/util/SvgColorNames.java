// Copyright (c) 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.awt.Color;


// http://www.w3.org/TR/SVG/types.html#ColorKeywords
public class SvgColorNames
{
	private CMap<String,Color> colors = new CMap();
	
	
	public SvgColorNames()
	{
		a("aliceblue", 240, 248, 255);;
		a("antiquewhite", 250, 235, 215);
		a("aqua",  0, 255, 255);
		a("aquamarine", 127, 255, 212);
		a("azure", 240, 255, 255);
		a("beige", 245, 245, 220);
		a("bisque", 255, 228, 196);
		a("black",  0, 0, 0);
		a("blanchedalmond", 255, 235, 205);
		a("blue",  0, 0, 255);
		a("blueviolet", 138, 43, 226);
		a("brown", 165, 42, 42);
		a("burlywood", 222, 184, 135);
		a("cadetblue",  95, 158, 160);
		a("chartreuse", 127, 255, 0);
		a("chocolate", 210, 105, 30);
		a("coral", 255, 127, 80);
		a("cornflowerblue", 100, 149, 237);
		a("cornsilk", 255, 248, 220);
		a("crimson", 220, 20, 60);
		a("cyan",  0, 255, 255);
		a("darkblue",  0, 0, 139);
		a("darkcyan",  0, 139, 139);
		a("darkgoldenrod", 184, 134, 11);
		a("darkgray", 169, 169, 169);
		a("darkgreen",  0, 100, 0);
		a("darkgrey", 169, 169, 169);
		a("darkkhaki", 189, 183, 107);
		a("darkmagenta", 139, 0, 139);
		a("darkolivegreen",  85, 107, 47);
		a("darkorange", 255, 140, 0);
		a("darkorchid", 153, 50, 204);
		a("darkred", 139, 0, 0);
		a("darksalmon", 233, 150, 122);
		a("darkseagreen", 143, 188, 143);
		a("darkslateblue",  72, 61, 139);
		a("darkslategray",  47, 79, 79);
		a("darkslategrey",  47, 79, 79);
		a("darkturquoise",  0, 206, 209);
		a("darkviolet", 148, 0, 211);
		a("deeppink", 255, 20, 147);
		a("deepskyblue",  0, 191, 255);
		a("dimgray", 105, 105, 105);
		a("dimgrey", 105, 105, 105);
		a("dodgerblue",  30, 144, 255);
		a("firebrick", 178, 34, 34);
		a("floralwhite", 255, 250, 240);
		a("forestgreen",  34, 139, 34);
		a("fuchsia", 255, 0, 255);
		a("gainsboro", 220, 220, 220);
		a("ghostwhite", 248, 248, 255);
		a("gold", 255, 215, 0);
		a("goldenrod", 218, 165, 32);
		a("gray", 128, 128, 128);
		a("grey", 128, 128, 128);
		a("green",  0, 128, 0);
		a("greenyellow", 173, 255, 47);
		a("honeydew", 240, 255, 240);
		a("hotpink", 255, 105, 180);
		a("indianred", 205, 92, 92);
		a("indigo",  75, 0, 130);
		a("ivory", 255, 255, 240);
		a("khaki", 240, 230, 140);
		a("lavender", 230, 230, 250);
		a("lavenderblush", 255, 240, 245);
		a("lawngreen", 124, 252, 0);
		a("lemonchiffon", 255, 250, 205);
		a("lightblue", 173, 216, 230);
		a("lightcoral", 240, 128, 128);
		a("lightcyan", 224, 255, 255);
		a("lightgoldenrodyellow", 250, 250, 210);
		a("lightgray", 211, 211, 211);
		a("lightgreen", 144, 238, 144);
		a("lightgrey", 211, 211, 211);
		a("lightpink", 255, 182, 193);
		a("lightsalmon", 255, 160, 122);
		a("lightseagreen",  32, 178, 170);
		a("lightskyblue", 135, 206, 250);
		a("lightslategray", 119, 136, 153);
		a("lightslategrey", 119, 136, 153);
		a("lightsteelblue", 176, 196, 222);
		a("lightyellow", 255, 255, 224);
		a("lime",  0, 255, 0);
		a("limegreen",  50, 205, 50);
		a("linen", 250, 240, 230);
		a("magenta", 255, 0, 255);
		a("maroon", 128, 0, 0);
		a("mediumaquamarine", 102, 205, 170);
		a("mediumblue",  0, 0, 205);
		a("mediumorchid", 186, 85, 211);
		a("mediumpurple", 147, 112, 219);
		a("mediumseagreen",  60, 179, 113);
		a("mediumslateblue", 123, 104, 238);
		a("mediumspringgreen",  0, 250, 154);
		a("mediumturquoise",  72, 209, 204);
		a("mediumvioletred", 199, 21, 133);
		a("midnightblue",  25, 25, 112);
		a("mintcream", 245, 255, 250);
		a("mistyrose", 255, 228, 225);
		a("moccasin", 255, 228, 181);
		a("navajowhite", 255, 222, 173);
		a("navy",  0, 0, 128);
		a("oldlace", 253, 245, 230);
		a("olive", 128, 128, 0);
		a("olivedrab", 107, 142, 35);
		a("orange", 255, 165, 0);
		a("orangered", 255, 69, 0);
		a("orchid", 218, 112, 214);
		a("palegoldenrod", 238, 232, 170);
		a("palegreen", 152, 251, 152);
		a("paleturquoise", 175, 238, 238);
		a("palevioletred", 219, 112, 147);
		a("papayawhip", 255, 239, 213);
		a("peachpuff", 255, 218, 185);
		a("peru", 205, 133, 63);
		a("pink", 255, 192, 203);
		a("plum", 221, 160, 221);
		a("powderblue", 176, 224, 230);
		a("purple", 128, 0, 128);
		a("red", 255, 0, 0);
		a("rosybrown", 188, 143, 143);
		a("royalblue",  65, 105, 225);
		a("saddlebrown", 139, 69, 19);
		a("salmon", 250, 128, 114);
		a("sandybrown", 244, 164, 96);
		a("seagreen",  46, 139, 87);
		a("seashell", 255, 245, 238);
		a("sienna", 160, 82, 45);
		a("silver", 192, 192, 192);
		a("skyblue", 135, 206, 235);
		a("slateblue", 106, 90, 205);
		a("slategray", 112, 128, 144);
		a("slategrey", 112, 128, 144);
		a("snow", 255, 250, 250);
		a("springgreen",  0, 255, 127);
		a("steelblue",  70, 130, 180);
		a("tan", 210, 180, 140);
		a("teal",  0, 128, 128);
		a("thistle", 216, 191, 216);
		a("tomato", 255, 99, 71);
		a("turquoise",  64, 224, 208);
		a("violet", 238, 130, 238);
		a("wheat", 245, 222, 179);
		a("white", 255, 255, 255);
		a("whitesmoke", 245, 245, 245);
		a("yellow", 255, 255, 0);
		a("yellowgreen", 154, 205, 50);
	}
	
	
	private void a(String name, int r, int g, int b)
	{
		colors.put(name, new Color(r, g, b));
	}
	
	
	public Color lookupColor(String name)
	{
		return colors.get(name.toLowerCase());
	}
}
