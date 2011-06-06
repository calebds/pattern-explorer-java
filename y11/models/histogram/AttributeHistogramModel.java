package y11.models.histogram;

import java.util.PriorityQueue;

import y11.manager.state.MatrixInfo;
import y11.operator.PatternExplorerSettings;

import com.rapidminer.example.Attribute;

/**
 * A histogram model for the first histogram in a column.
 * 
 * @author Caleb Sotelo
 * 
 */
public class AttributeHistogramModel extends HistogramModel {

	private static final String TYPE = "ATTRIBUTE_HISTO_MODEL";

	private int[] visibleIndices;
	private int[] compactCounts;
	private int[] fullCounts;

	/**
	 * Creates a new AttributeHistogramModel with the specified settings.
	 * 
	 * @param counts
	 * @param attribute
	 * @param mi
	 */
	public AttributeHistogramModel(int[] counts, Attribute attribute,
			MatrixInfo mi) {
		super(counts, attribute, mi);
		name = TYPE + HistogramModel.NAME_SEP + attribute.getName();
		fullCounts = counts;
		// TODO do this only once
		for (int c : counts) {
			total += c;
		}
		if (info.isExpandable(attribute)) {
			// sort the top elements
			PriorityQueue<IndexValuePair> sorter = new PriorityQueue<IndexValuePair>();
			for (int i = 0; i < counts.length; i++) {
				sorter.add(new IndexValuePair(i, counts[i]));
			}

			// set this model's counts to the condensed and sorted version
			compactCounts = new int[PatternExplorerSettings.NUM_COMPACT_VALUES];
			visibleIndices = new int[compactCounts.length];
			for (int i = 0; i < compactCounts.length; i++) {
				IndexValuePair ivp = sorter.remove();
				compactCounts[i] = ivp.value;
				visibleIndices[i] = ivp.index;
			}
			if (!info.isExpanded(attribute)) {
				this.counts = compactCounts;
			}
		}
	}

	/**
	 * Copy constructor.
	 * 
	 * @param copy
	 */
	public AttributeHistogramModel(AttributeHistogramModel copy) {
		super(copy);
		this.visibleIndices = copy.visibleIndices;
		this.compactCounts = copy.compactCounts;
		this.fullCounts = copy.fullCounts;
	}

	/**
	 * Pairs an integer index with an attribute value, used for sorting in
	 * expand/compact operations.
	 * 
	 * @author Caleb Sotelo
	 * 
	 */
	private class IndexValuePair implements Comparable {

		int index;
		int value;

		IndexValuePair(int index, int value) {
			this.index = index;
			this.value = value;
		}

		public int compareTo(Object o) {
			IndexValuePair other = (IndexValuePair) o;
			if (index == other.value) {
				return 0;
			} else {
				return value > other.value ? -1 : 1;
			}
		}
	}

	/**
	 * Gets an array of the visible indices for this model.
	 * 
	 * @return
	 */
	public int[] getVisibleIndices() {
		return visibleIndices;
	}

	/**
	 * Computes and returns the relative height of the i'th bar in this model.
	 * 
	 * @param i
	 * @return
	 */
	public double getRelativeHeight(int i) {
		return ((double) counts[i]) / maxCount;
	}

	/**
	 * Gets the name of the attribute for this model.
	 */
	public String getAttributeName() {
		return attribute.getName();
	}

	/**
	 * Tests if this model is expandable.
	 * 
	 * @return
	 */
	public boolean isExpandable() {
		return info.isExpandable(attribute);
	}

	/**
	 * Tests if this model is expanded.
	 * 
	 * @return
	 */
	public boolean isExpanded() {
		return info.isExpanded(attribute);
	}

	/**
	 * Modifies this model to expand or compact it.
	 * 
	 * @param mi
	 * @return
	 */
	public HistogramModel modifyExpand(MatrixInfo mi) {
		AttributeHistogramModel modify = new AttributeHistogramModel(this);
		modify.info = mi;
		return modify.toggleExpand();
	}

	private HistogramModel toggleExpand() {
		if (info.isExpanded(attribute)) {
			counts = fullCounts;
		} else {
			counts = compactCounts;
		}
		return this;
	}

}
