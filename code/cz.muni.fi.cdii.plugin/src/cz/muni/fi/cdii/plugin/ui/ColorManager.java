package cz.muni.fi.cdii.plugin.ui;

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
		
		FONT_COLOR (250,250,250),
		BORDER_COLOR (10,10,10),
		SELECTED_BORDER_COLOR (250,10,10),
		CLASS_NODE_COLOR (40,103,201),
		CLASS_SELECTED_NODE_COLOR (123,153,199),
		BEAN_NODE_COLOR (42,201,85),
		BEAN_SELECTED_NODE_COLOR (130,191,146);
		
		private int red;
		private int green;
		private int blue;
		
		GraphColorEnum(int red, int green, int blue) {
			this.red = red;
			this.green = green;
			this.blue = blue;
		}
		
		public RGB getRGB() {
			return new RGB(this.red, this.green, this.blue);
		}
	}
	
	private Map<GraphColorEnum, Color> colors;
	
	public ColorManager() {
		this.colors = new HashMap<>();
		for (int i = 0; i < GraphColorEnum.values().length; i++) {
			GraphColorEnum colorEnum = GraphColorEnum.values()[i];
			this.colors.put(colorEnum, new Color(null, colorEnum.getRGB()));
		}
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
		return this.colors.get(colorName);
	}
	
	
}
