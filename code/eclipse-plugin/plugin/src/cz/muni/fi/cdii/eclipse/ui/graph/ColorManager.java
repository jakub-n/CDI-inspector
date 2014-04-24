package cz.muni.fi.cdii.eclipse.ui.graph;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/**
 * It provides managed {@link Color} instance for graph elements. 
 *
 */
public class ColorManager {
	
	public static enum GraphColorEnum {
		FONT_COLOR (10, 10, 10),
		BORDER_COLOR (10,10,10),
		SELECTED_BORDER_COLOR (250,10,10),
		CLASS_NODE_COLOR (40,103,201),
		CLASS_SELECTED_NODE_COLOR (123,153,199),
		BEAN_NODE_COLOR (42,201,85),
		BEAN_SELECTED_NODE_COLOR (130,191,146),
		
		TYPE_NODE("#F0B2D1"), // #CC0066
		BEAN_NODE("#FFB2B2"), // #FF0000
		FIELD_NODE("#C2C2F0"), // #3333CC
		METHOD_NODE("#B2E0E0"), // #009999
		
		TYPE_NEIGHBOR_NODE("#660033"),
        BEAN_NEIGHBOR_NODE("#800000"),
        FIELD_NEIGHBOR_NODE("#1A1A66"),
        METHOD_NEIGHBOR_NODE("#004C4C"),
        
        TYPE_CONNECTION("#999999"),
        INJECT_CONNECTION("#335CD6"), // #0033CC
        PRODUCES_CONNECTION("#D63333"), // #CC0000
		
		FONT(10,10,10),
		FONT_NEIGHBOR(250,250,250);
		
		
		
		private int red;
		private int green;
		private int blue;
		
		GraphColorEnum(int red, int green, int blue) {
			this.red = red;
			this.green = green;
			this.blue = blue;
		}
		
		private GraphColorEnum(String htmlColor) {
		    checkFormat(htmlColor);
		    String hexDigits = htmlColor.startsWith("#") ? htmlColor.substring(1) : htmlColor;
		    this.red = parseColorCode(hexDigits, 0);
		    this.green = parseColorCode(hexDigits, 2);
		    this.blue = parseColorCode(hexDigits, 4);
		}
		
		private int parseColorCode(String hexDigits, int shift) {
            String digitCouple = hexDigits.substring(0 + shift, 2 + shift);
            int value = Integer.parseInt(digitCouple, 16);
            return value;
        }

        private void checkFormat(final String htmlColor) {
		    boolean inputValid = htmlColor.matches("^#?[0-9a-fA-F]{6}$");
		    if (!inputValid) {
		        throw new IllegalArgumentException("invalid input: '" + htmlColor + "'");
		    }
        }

        public RGB getRGB() {
			return new RGB(this.red, this.green, this.blue);
		}
	}
	
	private Map<GraphColorEnum, Color> colors;
	
	public ColorManager() {
		this.colors = new HashMap<>();
	}
	
	/**
	 * Call this to dispose all stored colors.
	 */
	public void dispose() {
		for (Color color : this.colors.values()) {
			if (!color.isDisposed()) {
				color.dispose();
			}
		}
	}
	
	/**
	 * Color returned by this method are automatically disposed and should not be disposed by users.
	 * @param colorName color name
	 * @return Color instance, if {@code colorName} is from constants of this interface, 
	 * {@code null} otherwise.
	 */
	public Color getNamedColor(GraphColorEnum colorName) {
	    Color color = this.colors.get(colorName);
	    if (color == null) {
	        color = new Color(null, colorName.getRGB());
	        this.colors.put(colorName, color);
	    }
		return color;
	}
	
	
}
