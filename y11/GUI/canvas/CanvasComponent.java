package y11.GUI.canvas;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;

import y11.click.Clickable;
import y11.click.ClickableRegionMap;

/**
 * Abstract ancestor of any component to be displayed on the histogram panel
 * that should support user interaction. Components are all assumed to be
 * rectangular in the regular x/y coordinate plane.
 * 
 * @author Caleb Sotelo
 * 
 */
public abstract class CanvasComponent extends Component implements Clickable {

	/**
	 * Renders the contents of the component. To be called before re-drawing the
	 * component.
	 * 
	 * @param x
	 *            X-coordinate of the upper-left vertex
	 * @param y
	 *            Y-coordinate of the upper-left vertext
	 * @param w
	 *            Width of the component
	 * @param h
	 *            Height of the component
	 */
	public abstract void prepare(int x, int y, int w, int h);

	protected Rectangle border;
	protected static double scale;
	protected static ClickableRegionMap clickMap;
	protected String toolTipText = " ";

	/**
	 * Sets the current scale statically available to all canvas components
	 * 
	 * @param s
	 *            The scale to set
	 */
	public static void setScale(double s) {
		scale = s;
	}

	/**
	 * Sets the ClickableRegionMap statically available to all canvas components
	 * 
	 * @param map
	 */
	public static void setClickMap(ClickableRegionMap map) {
		clickMap = map;
	}

	/**
	 * Returns the tool tip text for this component
	 * 
	 * @return The tool tip text
	 */
	public String getToolTipText() {
		return toolTipText;
	}

	/**
	 * Returns the rectangle representing the outside border of this component
	 * 
	 * @return The Rectangle border
	 */
	public Rectangle getBorder() {
		return border;
	}

	/**
	 * {@inheritDoc}
	 */
	public Point[] getVertices() {
		Point[] vertices = new Point[4];
		Point nw = border.getLocation();
		vertices[0] = nw; // nw
		vertices[1] = new Point(nw.x + border.width, nw.y); // ne
		vertices[2] = new Point(nw.x, nw.y + border.height); // sw
		vertices[3] = new Point(nw.x + border.width, nw.y + border.height); // se
		return vertices;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean contains(Point p) {
		return border.contains(p);
	}

}
