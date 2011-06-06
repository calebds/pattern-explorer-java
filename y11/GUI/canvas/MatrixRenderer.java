package y11.GUI.canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import y11.GUI.GUISettings;
import y11.GUI.histogram.Histogram;
import y11.click.ClickableRegionMap;
import y11.matrix.Matrix;

/**
 * The histogram display component. Paints all components to be rendered on the
 * display including graphs and labels.
 * 
 * @author Caleb Sotelo
 * 
 */
public class MatrixRenderer extends JPanel {

	private Matrix model;
	private double scale;

	private Dimension dim;
	private ClickableRegionMap clickMap;
	private int grid_sz;

	/**
	 * Creates a new MatrixRenderer with default settings.
	 */
	public MatrixRenderer() {
		setBackground(new Color(GUISettings.MATRIX_BG_COLOR));
		dim = new Dimension();
		grid_sz = GUISettings.ZOOM_DEFAULT_SCALE * 2;
		clickMap = new ClickableRegionMap(grid_sz);
		CanvasComponent.setClickMap(clickMap);
	}

	/**
	 * Gets the click map used for mapping click events to display components.
	 * 
	 * @return The ClickableRegionMap
	 */
	public ClickableRegionMap getClickMap() {
		return clickMap;
	}

	/**
	 * Gets the dimensions of the display. Used for configuring the scroll bars.
	 * 
	 * @return The Dimension of this histrogram display.
	 */
	public Dimension getDim() {
		return dim;
	}

	/**
	 * {@inheritDoc}
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(new Color(0x000000));

		Label.initFonts(g);
		CanvasComponent[][] graphs = model.getGraphs();
		CanvasComponent.setScale(scale);

		int x, y, size, pad_x, pad_y;
		int w = 0, h = 0;

		int cols = graphs.length;
		int rows = graphs[0].length;

		int outer_y_pad = GUISettings.MATRIX_TITLE_PADDING
				+ GUISettings.MATRIX_OUTER_PADDING
				+ (int) (scale * GUISettings.ATTRIB_LABEL_PADDING);
		int outer_x_pad = GUISettings.MATRIX_OUTER_PADDING
				+ (int) (scale * (GUISettings.ROW_LABEL_PADDING + GUISettings.DELETE_BTN_PADDING));

		y = outer_y_pad;
		x = outer_x_pad;
		size = (int) (scale * GUISettings.HISTOGRAM_DEFAULT_SIZE);
		pad_x = (int) Math.max(scale * GUISettings.HISTOGRAM_PADDING_X,
				GUISettings.MATRIX_MIN_PADDING);
		pad_y = (int) Math.max(scale * GUISettings.HISTOGRAM_PADDING_Y,
				GUISettings.MATRIX_MIN_PADDING);

		h = size;

		Label.drawTitle(g, model.getTitleText());

		// default matrix
		for (int c = 0; c < cols; c++) {

			// target histograms are always the same relative width and height
			if (c > 0) {
				w = (int) (scale * Histogram.calcWidth(model.getColSize(c - 1)));
			} else {
				w = size;
			}

			y = outer_y_pad;

			for (int r = 0; r < rows; r++) {
				graphs[c][r].prepare(x, y, w, h);
				graphs[c][r].paint(g);
				y += h + pad_y;
			}

			x += w + pad_x;
		}

		dim.width = x + GUISettings.MATRIX_OUTER_PADDING
				- GUISettings.HISTOGRAM_PADDING_X;
		dim.height = y + GUISettings.MATRIX_OUTER_PADDING
				- GUISettings.HISTOGRAM_PADDING_Y;
		setPreferredSize(dim);
		revalidate();
	}

	/**
	 * Sets the model for this histogram display.
	 * 
	 * @param model
	 *            the matrix to set
	 */
	public void setModel(Matrix model) {
		this.model = model;
		clickMap.clear();
	}

	/**
	 * Sets the scale of this histogram display
	 * 
	 * @param scale
	 *            the scale to set
	 */
	public void setScale(int scale) {
		this.scale = ((double) scale) / GUISettings.ZOOM_DEFAULT_SCALE;
		clickMap.setBinSize(scale);
	}

}
