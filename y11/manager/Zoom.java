package y11.manager;

import y11.GUI.GUISettings;

/**
 * Represents a zoom setting, used for history recording.
 * 
 * @author Caleb Sotelo
 * 
 */
public class Zoom {

	private double scale;

	/**
	 * Creates a new Zoom with default settings.
	 */
	public Zoom() {
		scale = GUISettings.ZOOM_DEFAULT_SCALE;
	}

	/**
	 * Creates a new Zoom with the specified settings.
	 * 
	 * @param scale
	 *            The zoom scale to set
	 */
	public Zoom(double scale) {
		this.scale = scale;
	}

	/**
	 * Gets the scale associated with this Zoom
	 * 
	 * @return The scale
	 */
	public double getScale() {
		return scale;
	}

	/**
	 * Sets the scale for this Zoom
	 * 
	 * @param scale
	 *            the scale to set
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}
}
