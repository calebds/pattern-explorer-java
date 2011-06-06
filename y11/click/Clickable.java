package y11.click;

import java.awt.Point;

/**
 * Defines the interface for objects that should respond to click or hover
 * events on the histogram display. Objects are assumed to be rectangular in a
 * simple x,y coordinate system.
 * 
 * @author Caleb Sotelo
 * 
 */
public interface Clickable {

	/**
	 * Check whether the specified point is within this clickable.
	 * 
	 * @param p
	 *            The point used to test for containment
	 * @return True if this clickable contains the point p
	 */
	public boolean contains(Point p);

	/**
	 * Returns the vertices for this clickable.
	 * 
	 * @return The vertex points of this clickable in clockwise order starting
	 *         from upper left.
	 */
	public Point[] getVertices();

}
