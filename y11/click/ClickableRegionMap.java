package y11.click;

import java.awt.Point;
import java.util.HashMap;

/**
 * Manages all Clickable objects by allowing click and hover events to quicly
 * access the objects they correspond to.
 * 
 * @author Caleb Sotelo
 * 
 */
public class ClickableRegionMap {

	private static final String DELIMIT = "$";
	private HashMap<String, HashMap<Integer, Clickable>> bins;
	private int binSize;

	/**
	 * Creates a new ClickableRegionMap with the specified binSize
	 * 
	 * @param binSize
	 *            The binSize to set
	 */
	public ClickableRegionMap(int binSize) {
		bins = new HashMap<String, HashMap<Integer, Clickable>>();
		setBinSize(binSize);
	}

	/**
	 * Sets the binSize
	 * 
	 * @param binSize
	 *            The binSize to set
	 */
	public void setBinSize(int binSize) {
		this.binSize = binSize;
	}

	private String getBinHash(Point p) {
		return (p.x / binSize) + DELIMIT + (p.y / binSize);
	}

	private void put(String binHash, Clickable c) {
		HashMap<Integer, Clickable> bin = bins.get(binHash);
		if (bin == null) {
			bin = new HashMap<Integer, Clickable>();
			bin.put(c.hashCode(), c);
			bins.put(binHash, bin);
		} else {
			bin.put(c.hashCode(), c);
		}
	}

	/**
	 * Gets the Clickable associated with the specified point
	 * 
	 * @param p
	 *            The point to test
	 * @return The associated Clickable or null if the pont is in an empty
	 *         region
	 */
	public Clickable get(Point p) {
		HashMap<Integer, Clickable> bin = bins.get(getBinHash(p));
		if (bin != null) {
			for (Integer i : bin.keySet()) {
				Clickable c = bin.get(i);
				if (c.contains(p)) {
					return c;
				}
			}
		}
		return null;

	}

	/**
	 * Registers the specified Clickable into the map by associating the
	 * Clickable with all bins enclosed by the four vertices. This method
	 * currently uses some hardcoding and should be rewritten to work more
	 * generically.
	 * 
	 * @param c
	 *            The clickable to register
	 */
	public void register(Clickable c) {
		Point[] vertices = c.getVertices();
		int xfill = (vertices[1].x - vertices[0].x) / binSize;
		String binHash;
		for (Point p : vertices) {
			binHash = getBinHash(p);
			put(binHash, c);
		}
		Point xp = new Point(vertices[0]);
		for (int i = 0; i < xfill; i++) {
			xp.x += binSize;
			binHash = getBinHash(xp);
			put(binHash, c);
		}
		xp = new Point(vertices[2]);
		for (int i = 0; i < xfill; i++) {
			xp.x += binSize;
			binHash = getBinHash(xp);
			put(binHash, c);
		}
	}

	/**
	 * Clears the map
	 */
	public void clear() {
		bins.clear();
	}

}
