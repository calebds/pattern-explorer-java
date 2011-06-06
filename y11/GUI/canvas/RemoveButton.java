package y11.GUI.canvas;

import java.awt.Color;
import java.awt.Rectangle;

import y11.GUI.GUISettings;
import y11.models.AttributeValuePair;

/**
 * A button component for removing rows in the histogram display.
 * 
 * @author Caleb Sotelo
 * 
 */
public class RemoveButton extends Label {

	private AttributeValuePair avp;

	/**
	 * Creates a new RemoveButton associated with the specified attribute/value
	 * pair
	 * 
	 * @param avp
	 */
	public RemoveButton(AttributeValuePair avp) {
		this.avp = avp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void draw(String labelTxt, Rectangle gborder) {
		if (scale < GUISettings.LABEL_MIN_SCALE)
			return;
		g.setColor(new Color(GUISettings.LABEL_COLOR));
		super.draw(GUISettings.DELETE_VALUE_TXT, gborder);
		int x, y; // lower-left corner of the label
		int h = metrics.getHeight();
		int w = metrics.stringWidth(labelString);

		int labelPad = (int) (GUISettings.ROW_LABEL_PADDING * scale);

		if (w > labelPad) {
			double ratio = ((double) labelPad) / w;
			int endIndex = (int) Math.floor(labelString.length() * ratio) - 2;
			labelString = labelString.substring(0, endIndex) + "..";
			w = metrics.stringWidth(labelString);
		}

		int pad = (int) (GUISettings.LABEL_INSIDE_PADDING * scale);

		x = gborder.x - pad - w;
		y = gborder.y + gborder.height - 4;

		g.drawString(labelString, x, y);
		g.setColor(new Color(GUISettings.LABEL_OUTLINE_COLOR));
		g.drawRect(x - 4, y - h + 2, w + 7, h + 2);
		super.prepare(x - 4, y - h + 2, w + 7, h + 2);
		toolTipText = "Click to remove value '" + labelTxt + "'";
	}

	/**
	 * Gets the attribute/value associated with this RemoveButton
	 * 
	 * @return The attribute/value pair
	 */
	public AttributeValuePair getAvp() {
		return avp;
	}

}
