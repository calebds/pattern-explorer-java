package y11.models.histogram;

import y11.manager.state.MatrixInfo;

import com.rapidminer.example.Attribute;

/**
 * Abstract parent of all histogram models. Maintains information about
 * histograms such as their backing counts, parent attribute and a reference to
 * the MatrixInfo.
 * 
 * @author Caleb Sotelo
 * 
 */
public abstract class HistogramModel {

	public static final String NAME_SEP = "-";
	private static boolean PRINT_NAMES = false;
	protected static double sigma;

	protected int[] counts;
	protected Attribute attribute;
	protected String name;
	protected int maxCount;
	protected int maxColCount;
	protected int total;
	protected MatrixInfo info;

	/**
	 * Creates a new HistogramModel with the specifie settings.
	 * 
	 * @param counts
	 * @param a
	 * @param mi
	 */
	public HistogramModel(int[] counts, Attribute a, MatrixInfo mi) {
		setCounts(counts);
		this.attribute = a;
		this.info = mi;
	}

	/**
	 * Copy constructor. Creates a new HistogramModel from the specified
	 * HistogramModel.
	 * 
	 * @param copy
	 */
	public HistogramModel(HistogramModel copy) {

		this.attribute = copy.attribute;
		this.name = copy.name;
		this.maxCount = copy.maxCount;
		this.maxColCount = copy.maxColCount;
		this.total = copy.total;
		this.info = copy.info;
		this.counts = new int[copy.counts.length];

		for (int i = 0; i < counts.length; i++) {
			this.counts[i] = copy.counts[i];
		}
	}

	/**
	 * Sets the statistical significance threshold used for this model.
	 * 
	 * @param s
	 */
	public static void setSigma(double s) {
		sigma = s;
	}

	/**
	 * Gets the number of histogram bars in this model.
	 * 
	 * @return
	 */
	public int size() {
		return counts.length;
	}

	/**
	 * Sets the backing counts for this model.
	 * 
	 * @param counts
	 */
	private void setCounts(int[] counts) {
		this.counts = counts;
		for (int c : counts) {
			maxCount = Math.max(c, maxCount);
		}
	}

	/**
	 * Gets the i'th count from this backing model. (Height of the i'th bar).
	 * 
	 * @param i
	 * @return
	 */
	public int getCount(int i) {
		return counts[i];
	}

	/**
	 * Gets the backing counts of this model.
	 * 
	 * @return
	 */
	public int[] getCounts() {
		return counts;
	}

	/**
	 * Gets the largest count from this model (tallest bar).
	 * 
	 * @return
	 */
	public int getMaxCount() {
		return maxCount;
	}

	/**
	 * Sets the maximum count of the column this model belongs to.
	 * 
	 * @param maxColCount
	 */
	public void setMaxColCount(int maxColCount) {
		this.maxColCount = maxColCount;
	}

	/**
	 * Use for printing matrices.
	 */
	public void print() {
		System.out.print(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = PRINT_NAMES ? name + " " : "";
		s += "[";
		for (int i = 0; i < counts.length; i++) {
			s += counts[i];
			if (i != counts.length - 1) {
				s += ",";
			}
		}
		s += "] ";
		return s;
	}

	/**
	 * Gets the sum of all counts in this model.
	 * 
	 * @return
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * Gets the name of the attribute this model belongs to.
	 * 
	 * @return
	 */
	public String getAttributeName() {
		return attribute.getName();
	}

	/**
	 * Gets the name of the attribute value at index i.
	 * 
	 * @param i
	 * @return
	 */
	public String getValueName(int i) {
		return info.getValue(attribute, i);
	}

}
