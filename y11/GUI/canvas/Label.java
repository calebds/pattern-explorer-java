package y11.GUI.canvas;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import y11.GUI.GUISettings;

/**
 * Abstract parent of all text labels to be rendered on the histogram display.
 * 
 * @author Caleb Sotelo
 * 
 */
public abstract class Label extends CanvasComponent {

	/**
	 * Font configuration object statically available to all labels
	 */
	public static FontMetrics metrics;
	
	protected static Graphics g;
	protected String labelString;
	
	private static Font labelFont;
	private static Font sigmaFont;


	/**
	 * Draws the title for the histogram display
	 * 
	 * @param graphics
	 *            Graphics object for drawing
	 * @param title
	 *            The text to display for the title
	 */
	public static void drawTitle(Graphics graphics, String title) {
		g = graphics;
		g.setColor(new Color(GUISettings.TITLE_LABEL_COLOR));
		Font font = new Font(GUISettings.LABEL_FONT_FAM,
				GUISettings.LABEL_FONT_STYLE, GUISettings.TITLE_FONT_SIZE);
		g.setFont(font);
		g.drawString(title, GUISettings.MATRIX_TITLE_X, GUISettings.MATRIX_TITLE_Y);
	}

	/**
	 * Creates and configures the font that will be used for writing labels
	 */
	public static void initLabelFont() {
		int size = Math.min((int) (scale * GUISettings.LABEL_FONT_SIZE),
				GUISettings.MAX_LABEL_FONT_SIZE);
		Font font = new Font(GUISettings.LABEL_FONT_FAM,
				GUISettings.LABEL_FONT_STYLE, size);
		labelFont = font;
	}

	/**
	 * Sets the current font to the font for writing labels
	 */
	public static void setLabelFont() {
		g.setColor(new Color(GUISettings.LABEL_COLOR));
		g.setFont(labelFont);
		metrics = g.getFontMetrics(labelFont);
	}

	/**
	 * Creates and configures the font that will be used for writing
	 * significance values
	 */
	public static void initSigmaFont() {
		int size = Math.min((int) (scale * GUISettings.SIGMA_LABEL_FONT_SIZE),
				GUISettings.MAX_LABEL_FONT_SIZE);
		Font font = new Font(GUISettings.LABEL_FONT_FAM,
				GUISettings.LABEL_FONT_STYLE, size);
		metrics = g.getFontMetrics(font);
		sigmaFont = font;
	}

	/**
	 * Sets the current font to the font for writing significance values
	 */
	public static void setSigmaFont() {
		g.setColor(new Color(GUISettings.SIGMA_LABEL_COLOR));
		g.setFont(sigmaFont);
		metrics = g.getFontMetrics(sigmaFont);
	}

	/**
	 * Gets the text belonging to this label
	 * 
	 * @return The text of this label
	 */
	public String getText() {
		return labelString;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void prepare(int x, int y, int w, int h) {
		border = new Rectangle(x, y, w, h);
		clickMap.register(this);
	}

	/**
	 * Draws the label. In subclasses, super.draw() should be called to
	 * initialize drawing.  TODO remove hard-coded padding.
	 * 
	 * @param labelTxt
	 *            The text for this label
	 * @param gborder
	 *            The border of the graph corresponding to this label
	 */
	public void draw(String labelTxt, Rectangle gborder) {
		Label.setLabelFont();
		this.labelString = labelTxt;
	}

	/**
	 * Initializes the fonts
	 * 
	 * @param graphics
	 *            The graphics object to modify
	 */
	public static void initFonts(Graphics graphics) {
		g = graphics;
		initLabelFont();
		initSigmaFont();
	}

}
