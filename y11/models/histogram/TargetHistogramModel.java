package y11.models.histogram;

import y11.manager.state.MatrixInfo;

import com.rapidminer.example.Attribute;

/**
 * A histogram model for the first histogram in a row.
 * 
 * @author Caleb Sotelo
 * 
 */
public class TargetHistogramModel extends HistogramModel {

	private static final String TYPE = "TARGET_HISTO_MODEL";
	private String value;

	/**
	 * Creates a new TargetHistogramModel with the specified settings.
	 * 
	 * @param counts
	 * @param t
	 * @param value
	 * @param mi
	 */
	public TargetHistogramModel(int[] counts, Attribute t, String value,
			MatrixInfo mi) {
		super(counts, t, mi);
		this.value = value;
		name = TYPE + HistogramModel.NAME_SEP + t.getName() + "." + value;
	}

	/**
	 * Gets the height of the one bar in this model.
	 * 
	 * @return
	 */
	public int getCount() {
		return super.getCount(0);
	}

	/**
	 * Computes and returns the relative height of the one bar in this model.
	 * 
	 * @return
	 */
	public double getRelativeHeight() {
		return ((double) counts[0]) / maxColCount;
	}

	/**
	 * Gets the name of the value represented by this model.
	 * 
	 * @return
	 */
	public String getValueName() {
		return value;
	}

	/**
	 * Tests if a drill down operation can occur on this row.
	 * 
	 * @return
	 */
	public boolean canDrillDown() {
		return info.canDrillDown(attribute);
	}
}
