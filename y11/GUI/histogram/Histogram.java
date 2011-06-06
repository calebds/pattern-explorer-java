package y11.GUI.histogram;

import java.awt.Rectangle;

import y11.GUI.GUISettings;
import y11.GUI.canvas.CanvasComponent;
import y11.models.histogram.HistogramModel;

/**
 * Abstract parent of all histogram graphical components.
 * 
 * @author Caleb Sotelo
 * 
 */
public abstract class Histogram extends CanvasComponent {

	// this histogram's data model
	protected HistogramModel model;

	// this histogram's bars
	protected Rectangle[] bars;

	/**
	 * Creates a new graph from the specified model.
	 * 
	 * @param m
	 */
	public Histogram(HistogramModel m) {
		model = m;
	}

	/**
	 * Gest this graph's model.
	 * 
	 * @return
	 */
	public HistogramModel getModel() {
		return model;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void prepare(int x, int y, int w, int h) {
		border = new Rectangle(x, y, w, h);
		clickMap.register(this);
		toolTipText = GUISettings.DETAIL_TIP;
	}

	/**
	 * Calulates the width of a histogram with the given number of bars.
	 * 
	 * @param numValues
	 * @return the width in pixels
	 */
	public static int calcWidth(int numValues) {
		int w = (2 * GUISettings.HISTOGRAM_INTERNAL_SIDE_PADDING)
				+ ((numValues - 1) * GUISettings.HISTOGRAM_INTERNAL_BAR_PADDING)
				+ (numValues * GUISettings.HISTOGRAM_DEFAULT_BAR_WIDTH);
		return w;
	}

}
