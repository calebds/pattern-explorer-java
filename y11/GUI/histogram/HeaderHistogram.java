package y11.GUI.histogram;

import y11.GUI.canvas.Label;
import y11.models.histogram.HistogramModel;

/**
 * Abstract parent of all histograms that are the first in their rows or
 * columns.
 * 
 * @author Caleb Sotelo
 * 
 */
public abstract class HeaderHistogram extends Histogram {

	protected Label label;

	/**
	 * Creates a new HeaderHistogram.
	 * 
	 * @param m
	 */
	public HeaderHistogram(HistogramModel m) {
		super(m);
	}

}
