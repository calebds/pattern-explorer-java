package y11.GUI.canvas;

import java.awt.Color;
import java.awt.Rectangle;

import y11.GUI.GUISettings;

/**
 * Represents a column label in the histogram display.
 * 
 * @author Caleb Sotelo
 * 
 */
public class ColumnLabel extends Label {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void draw(String labelTxt, Rectangle gborder) {
		if (scale < GUISettings.LABEL_MIN_SCALE)
			return;
		g.setColor(new Color(GUISettings.LABEL_COLOR));
		super.draw(labelTxt, gborder);
		int x, y; // lower-left corner of the label

		int h = metrics.getHeight();
		int w = metrics.stringWidth(labelTxt);

		if (w > gborder.width) {
			double ratio = ((double) gborder.width) / w;
			int endIndex = (int) Math.floor(labelTxt.length() * ratio) - 2;
			labelTxt = labelTxt.substring(0, endIndex) + "..";
			w = metrics.stringWidth(labelTxt);
		}
		int pad = (int) (GUISettings.LABEL_INSIDE_PADDING * scale);

		x = gborder.x + ((gborder.width - w) / 2);
		y = gborder.y - pad;

		g.drawString(labelTxt, x, y);
		g.setColor(new Color(GUISettings.LABEL_OUTLINE_COLOR));
		g.drawRect(x - 3, y - h + 2, w + 6, h + 2);
		toolTipText = "Click to change '" + labelString + "' to target";
		super.prepare(x - 3, y - h + 2, w + 6, h + 2);
	}

}
