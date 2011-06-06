package y11.GUI.canvas;

import java.awt.Color;
import java.awt.Rectangle;

import y11.GUI.GUISettings;

/**
 * A button component for expanding/contracting columns with many values on the
 * histogram display.
 * 
 * @author Caleb Sotelo
 * 
 */
public class ExpandButton extends Label {

	private boolean expanded;
	private String attribute;

	/**
	 * Sets the state of the button to expanded or contracted
	 * 
	 * @param expanded
	 */
	public ExpandButton(boolean expanded) {
		this.expanded = expanded;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void draw(String labelTxt, Rectangle gborder) {
		if (scale < GUISettings.LABEL_MIN_SCALE) {
			return;
		}
		attribute = labelTxt;
		g.setColor(new Color(GUISettings.LABEL_COLOR));
		String arrow = expanded ? GUISettings.EXPANDED_BUTTON_TEXT
				: GUISettings.COMPACTED_BUTTON_TEXT;
		super.draw(arrow, gborder);
		int x, y; // lower-left corner of the label
		int h = metrics.getHeight();
		int w = metrics.stringWidth(labelString);

		int pad = (int) (GUISettings.LABEL_INSIDE_PADDING * scale);

		x = gborder.x + gborder.width - w;
		y = gborder.y + gborder.height + pad;

		g.drawString(labelString, x, y);
		g.setColor(new Color(GUISettings.LABEL_OUTLINE_COLOR));
		// g.drawRect(x - 1, y - h + 6 , w + 2, h - 3);
		super.prepare(x - 1, y - h + 6, w + 2, h - 3);
		toolTipText = (expanded ? "Show fewer values" : "Show all values")
				+ " for '" + attribute + "'";
	}

	/**
	 * Gets the name of the attribute associated with this button's column
	 * 
	 * @return The attribute name
	 */
	public String getAttribute() {
		return attribute;
	}

}
