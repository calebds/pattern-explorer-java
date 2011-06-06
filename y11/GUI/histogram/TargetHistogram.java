package y11.GUI.histogram;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import y11.GUI.GUISettings;
import y11.GUI.canvas.RemoveButton;
import y11.GUI.canvas.RowLabel;
import y11.models.AttributeValuePair;
import y11.models.histogram.HistogramModel;
import y11.models.histogram.TargetHistogramModel;

/**
 * A histogram that is the first in its row.
 * 
 * @author Caleb Sotelo
 * 
 */
public class TargetHistogram extends HeaderHistogram {

	private RemoveButton removeBtn;

	/**
	 * Creates a new TargetHistogram from the specified model.
	 * 
	 * @param m
	 */
	public TargetHistogram(HistogramModel m) {
		super(m);
		TargetHistogramModel thm = (TargetHistogramModel) model;
		AttributeValuePair avp = new AttributeValuePair(thm.getAttributeName(),
				thm.getValueName(), thm.getCount());
		label = new RowLabel(avp, thm.canDrillDown());
		if (thm.canDrillDown()) {
			removeBtn = new RemoveButton(avp);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void paint(Graphics g) {
		g.setColor(new Color(GUISettings.HISTOGRAM_META_BORDER_COLOR));
		g.drawRect(border.x, border.y, border.width, border.height);
		g.setColor(new Color(GUISettings.HISTOGRAM_META_BAR_COLOR));
		for (Rectangle r : bars) {
			g.fillRect(r.x, r.y, r.width, r.height);
		}

		String labelTxt = ((TargetHistogramModel) model).getValueName();
		label.draw(labelTxt, border);
		if (((TargetHistogramModel) model).canDrillDown()) {
			removeBtn.draw(labelTxt, label.getBorder());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void prepare(int x, int y, int w, int h) {
		TargetHistogramModel thm = (TargetHistogramModel) model;
		super.prepare(x, y, w, h);
		bars = new Rectangle[thm.size()];
		int bar_h = (int) (h - 2 * (scale * GUISettings.HISTOGRAM_TARGET_SIDE_PADDING));
		int bar_w = (int) ((w - GUISettings.HISTOGRAM_INTERNAL_TOP_PADDING) * thm
				.getRelativeHeight());
		int bar_x = x + (w - bar_w);
		int bar_y = y
				+ (int) (scale * GUISettings.HISTOGRAM_TARGET_SIDE_PADDING);
		// only one bar
		bars[0] = new Rectangle(bar_x, bar_y, bar_w, bar_h);
	}
}
