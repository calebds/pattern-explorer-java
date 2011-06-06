package y11.models.histogram;

import y11.manager.state.MatrixInfo;

import com.rapidminer.example.Attribute;

/**
 * HistrogramModel for histograms that are not the first histograms in rows or
 * columns.
 * 
 * @author Caleb Sotelo
 * 
 */
public class DefaultHistogramModel extends HistogramModel {

	private static final String TYPE = "DEFAULT_HISTO_MODEL";

	private Attribute target;
	private String value;
	private double[] sigmas;
	private int[] fullCounts;

	/**
	 * Creates a new DefaultHistogramModel with the specified settings.
	 * 
	 * @param counts
	 * @param attribute
	 * @param target
	 * @param value
	 * @param mi
	 */
	public DefaultHistogramModel(int[] counts, Attribute attribute,
			Attribute target, String value, MatrixInfo mi) {
		super(counts, attribute, mi);
		fullCounts = counts;
		this.target = target;
		this.value = value;
		name = TYPE + HistogramModel.NAME_SEP + attribute.getName() + ":"
				+ target.getName() + "." + value;
		// TODO do this only once
		for (int c : counts) {
			total += c;
		}
	}

	/**
	 * Copy constructor.
	 * 
	 * @param copy
	 */
	public DefaultHistogramModel(DefaultHistogramModel copy) {
		super(copy);
		this.target = copy.target;
		this.value = copy.value;
		this.sigmas = copy.sigmas;
		this.fullCounts = copy.fullCounts;
	}

	/**
	 * Gets the target attribute this model belongs to.
	 * 
	 * @return
	 */
	public String getTarget() {
		return target.getName();
	}

	/**
	 * Gets the name of the value representing this model's row.
	 * 
	 * @return
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Gets the predictor attribute this model belongs to.
	 * 
	 * @return
	 */
	public String getPredictor() {
		return attribute.getName();
	}

	/**
	 * Computes and returns the relative height of the i'th bar in this model.
	 * 
	 * @param i
	 * @return
	 */
	public double getRelativeHeight(int i) {
		return ((double) counts[i]) / maxColCount;
	}

	/**
	 * Runs the algorithm for determining statistical significance based on a
	 * vriable threshold (z-score) developed by Charles Elkan. Marks each bar as
	 * significant accordingly.
	 * 
	 * @param aModel
	 *            the AttributeHistogramModel to compare with.
	 */
	public void setIsSignificant(AttributeHistogramModel aModel) {
		sigmas = new double[counts.length];
		double t = total;
		double f_i, s_i, q_i;
		for (int m_i = 0; m_i < sigmas.length; m_i++) {
			f_i = ((double) aModel.getCount(m_i)) / aModel.getTotal();
			s_i = Math.sqrt(t * f_i * (1 - f_i));
			q_i = (s_i == 0.0) ? 0 : (counts[m_i] - (f_i * t)) / s_i;
			sigmas[m_i] = q_i;
		}
	}

	/**
	 * Tests if the i'th bar is significant.
	 * 
	 * @param i
	 * @return
	 */
	public boolean isSignificant(int i) {
		return Math.abs(sigmas[i]) >= sigma;
	}

	/**
	 * Gets a string representation of the numeric level of significance for the
	 * i'th bar.
	 * 
	 * @param i
	 * @return
	 */
	public String getSignificance(int i) {
		String s = "";
		double val = (int) (sigmas[i] * 10);
		val = val / 10.0;
		if (val > 0) {
			s += "+";
		}
		s += val + "\u03C3";
		return s;
	}

	/**
	 * Compacts this model.
	 * 
	 * @param visible
	 *            the visible bars.
	 */
	public void modifyThisCompact(int[] visible) {
		int[] compact = new int[visible.length];
		for (int i = 0; i < compact.length; i++) {
			compact[i] = counts[visible[i]];
		}
		counts = compact;
	}

	/**
	 * Creates an expanded or compacted copy of this model.
	 * 
	 * @param visible
	 * @param mi
	 * @return
	 */
	public HistogramModel modifyExpand(int[] visible, MatrixInfo mi) {
		DefaultHistogramModel modify = new DefaultHistogramModel(this);
		modify.info = mi;
		return modify.toggleExpand(visible);
	}

	/**
	 * Expands or compacts this model.
	 * 
	 * @param visible
	 * @return
	 */
	private HistogramModel toggleExpand(int[] visible) {
		if (info.isExpanded(attribute)) {
			counts = fullCounts;
		} else {
			modifyThisCompact(visible);
		}
		return this;
	}
}
