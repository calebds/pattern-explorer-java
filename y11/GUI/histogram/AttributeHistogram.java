package y11.GUI.histogram;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import y11.GUI.GUISettings;
import y11.GUI.canvas.ColumnLabel;
import y11.GUI.canvas.ExpandButton;
import y11.models.histogram.AttributeHistogramModel;
import y11.models.histogram.HistogramModel;

/**
 * A histogram that is the first in its column.
 * 
 * @author Caleb Sotelo
 * 
 */
public class AttributeHistogram extends HeaderHistogram {

	private ExpandButton expandButton;

	/**
	 * Creates a new AttributeHistogram from the specified model.
	 * 
	 * @param m
	 */
	public AttributeHistogram(HistogramModel m) {
		super(m);
		label = new ColumnLabel();
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint(Graphics g) {
		AttributeHistogramModel m = (AttributeHistogramModel) model;
		g.setColor(new Color(GUISettings.HISTOGRAM_META_BORDER_COLOR));
		g.drawRect(border.x, border.y, border.width, border.height);
		g.setColor(new Color(GUISettings.HISTOGRAM_META_BAR_COLOR));
		for (Rectangle r : bars) {
			g.fillRect(r.x, r.y, r.width, r.height);
		}

		String labelTxt = m.getAttributeName();
		label.draw(labelTxt, border);
		if (m.isExpandable()) {
			expandButton = new ExpandButton(m.isExpanded());
			expandButton.draw(labelTxt, border);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void prepare(int x, int y, int w, int h) {
		AttributeHistogramModel m = (AttributeHistogramModel) model;
		super.prepare(x, y, w, h);
		bars = new Rectangle[m.size()];
		int in_pad = (int) (scale * GUISettings.HISTOGRAM_INTERNAL_BAR_PADDING);
		int bar_w = (int) (scale * GUISettings.HISTOGRAM_DEFAULT_BAR_WIDTH);
		int bar_x = x
				+ ((int) (scale * GUISettings.HISTOGRAM_INTERNAL_SIDE_PADDING));
		for (int i = 0; i < bars.length; i++) {
			int bh = (int) ((h - GUISettings.HISTOGRAM_INTERNAL_TOP_PADDING) * m
					.getRelativeHeight(i));
			bars[i] = new Rectangle(bar_x, y + (h - bh), bar_w, bh);
			bar_x += in_pad + bar_w;
		}
	}
}