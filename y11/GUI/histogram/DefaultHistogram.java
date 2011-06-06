package y11.GUI.histogram;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import y11.GUI.GUISettings;
import y11.GUI.canvas.Label;
import y11.models.histogram.DefaultHistogramModel;
import y11.models.histogram.HistogramModel;

/**
 * A histogram that is neither first in its row or column.
 * 
 * @author Caleb Sotelo
 * 
 */
public class DefaultHistogram extends Histogram {

	/**
	 * Creates a new DefualtHistogram from the specified model.
	 * 
	 * @param m
	 */
	public DefaultHistogram(HistogramModel m) {
		super(m);
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint(Graphics g) {
		DefaultHistogramModel m = (DefaultHistogramModel) model;
		g.setColor(new Color(GUISettings.HISTOGRAM_DEFAULT_BORDER_COLOR));
		g.drawRect(border.x, border.y, border.width, border.height);
		Color barColor = new Color(GUISettings.HISTOGRAM_DEFAULT_BAR_COLOR);
		Color sigColor = new Color(GUISettings.HISTOGRAM_SIGMA_BAR_COLOR);
		Rectangle rect;
		for (int i = 0; i < bars.length; i++) {
			rect = bars[i];
			g.setColor(m.isSignificant(i) ? sigColor : barColor);
			g.fillRect(rect.x, rect.y, rect.width, rect.height);
			if (m.isSignificant(i)) {
				if (!(scale < GUISettings.LABEL_MIN_SCALE)) {
					Label.setSigmaFont();
					String sig = m.getSignificance(i);
					int sx = (int) (rect.x + (((double) rect.width / 2) - ((double) Label.metrics
							.stringWidth(sig) / 2)));
					int sh = Label.metrics.getHeight();
					g.drawString(m.getSignificance(i), sx, rect.y + rect.height
							+ sh - GUISettings.SIGMA_LABEL_PADDING);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void prepare(int x, int y, int w, int h) {
		DefaultHistogramModel m = (DefaultHistogramModel) model;
		super.prepare(x, y, w, h);
		bars = new Rectangle[m.size()];
		int in_pad = (int) (scale * GUISettings.HISTOGRAM_INTERNAL_BAR_PADDING);
		int bar_w = (int) (scale * GUISettings.HISTOGRAM_DEFAULT_BAR_WIDTH);
		int bar_x = x
				+ ((int) (scale * GUISettings.HISTOGRAM_INTERNAL_SIDE_PADDING));
		for (int i = 0; i < bars.length; i++) {
			int bh = (int) ((h - GUISettings.HISTOGRAM_INTERNAL_TOP_PADDING) * m
					.getRelativeHeight(i));
			// Dbg.d("rel height", m.getRelativeHeight(i));
			bars[i] = new Rectangle(bar_x, y + (h - bh), bar_w, bh);
			bar_x += in_pad + bar_w;
		}
	}
}