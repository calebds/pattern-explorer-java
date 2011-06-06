package y11.manager;

import y11.GUI.GUISettings;

/**
 * Represents a statistical significance threshold setting, used for history
 * recording.
 * 
 * @author Caleb Sotelo
 * 
 */
public class Sigma {

	public static final double FACTOR = 100; // conversion factor

	private double threshold;

	/**
	 * Creates a new Sigma with the default settings.
	 */
	public Sigma() {
		threshold = GUISettings.SIGMA_SLIDER_INIT / FACTOR;
	}

	/**
	 * Creates a new Sigma with the specified settings.
	 * 
	 * @param threshold
	 *            The threshold to set for this Sigma
	 */
	public Sigma(double threshold) {
		this.threshold = threshold;
	}

	/**
	 * Gets the threshold value associated with this Sigma
	 * 
	 * @return The threshold
	 */
	public double getThreshold() {
		return threshold;
	}
}
