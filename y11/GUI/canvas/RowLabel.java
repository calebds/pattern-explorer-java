package y11.GUI.canvas;

import java.awt.Color;
import java.awt.Rectangle;

import y11.GUI.GUISettings;
import y11.models.AttributeValuePair;

/**
 * Represents a row label in the histogram display. Also doubles as a drill-down
 * on value button.
 * 
 * @author Caleb Sotelo
 * 
 */
public class RowLabel extends Label {

	private AttributeValuePair avp;
	private boolean canDrillDown;

	/**
	 * Creates a new RowLabel with the specified attribute/value pair and
	 * specified ability to drill down on the associated row.
	 * 
	 * @param avp
	 * @param canDrillDown
	 */
	public RowLabel(AttributeValuePair avp, boolean canDrillDown) {
		this.avp = avp;
		this.canDrillDown = canDrillDown;
	}

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

		int labelPad = (int) (GUISettings.ROW_LABEL_PADDING * scale);

		if (w > labelPad) {
			double ratio = ((double) labelPad) / w;
			int endIndex = (int) Math.floor(labelTxt.length() * ratio) - 2;
			labelTxt = labelTxt.substring(0, endIndex) + "..";
			w = metrics.stringWidth(labelTxt);
		}

		int pad = (int) (GUISettings.LABEL_INSIDE_PADDING * scale);

		x = gborder.x - pad - w;
		y = gborder.y + (gborder.height / 2);

		g.drawString(labelTxt, x, y);
		g.setColor(new Color(GUISettings.LABEL_OUTLINE_COLOR));
		g.drawRect(x - 3, y - h + 2, w + 6, h + 2);
		super.prepare(x - 3, y - h + 2, w + 6, h + 2);
		toolTipText = canDrillDown ? "Click to drill-down on value '"
				+ labelString + "'" : "Value '" + labelString + "'";
	}

	/**
	 * Gets the attribute/value associated with this RemoveButton.
	 * 
	 * @return The attribute/value pair
	 */
	public AttributeValuePair getAvp() {
		return avp;
	}

	/**
	 * Tests if the user can drill down on the row associated with this label.
	 * 
	 * @return True if the user can drill down on this row
	 */
	public boolean canDrillDown() {
		return canDrillDown;
	}

}
