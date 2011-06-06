package y11.manager.state;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import y11.logging.Log;
import y11.models.AttributeValuePair;
import y11.operator.PatternExplorerOperator;
import y11.operator.PatternExplorerSettings;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.AbstractAttribute;

/**
 * Stores information about how the matrix should be displayed, such as the
 * order of attributes, the order of attribute values, and whether columns are
 * expanded.
 * 
 * @author Caleb Sotelo
 * 
 */
public class MatrixInfo {

	private static final int DEFAULT_INDEX = 0;
	private Vector<ColumnInfo> columnsOrdered;
	private HashMap<String, ColumnInfo> columnsNamed;
	private HashMap<Attribute, ColumnInfo> columns;
	private int numAttributes;

	/**
	 * Creates a new MatrixInfo with the specified settings.
	 * 
	 * @param exampleSetNew
	 *            the starting ExampleSet
	 * @param map
	 *            maps all attribute names to a boolean that is true if the
	 *            attribute should be treated numerically.
	 */
	public MatrixInfo(ExampleSet exampleSetNew, Map map) {
		columnsOrdered = new Vector<ColumnInfo>();
		columns = new HashMap<Attribute, ColumnInfo>();
		columnsNamed = new HashMap<String, ColumnInfo>();
		Iterator<Attribute> all = exampleSetNew.getAttributes().allAttributes();
		boolean isNumerical;
		while (all.hasNext()) {
			Attribute attribute = all.next();
			isNumerical = map.containsKey(attribute.getName());
			ColumnInfo col = new ColumnInfo(attribute, columnsOrdered.size(),
					isNumerical);
			columnsOrdered.add(col);
			columns.put(attribute, col);
			columnsNamed.put(attribute.getName(), col);
		}
		numAttributes = columnsOrdered.size();
	}

	/**
	 * Soft copy constructor. Creates new references for the data structures
	 * without copying the data, to save memory.
	 * 
	 * @param mi
	 *            the MatrixInfo to copy
	 */
	public MatrixInfo(MatrixInfo mi) {
		columnsOrdered = new Vector<ColumnInfo>(mi.columnsOrdered);
		columnsNamed = new HashMap<String, ColumnInfo>(mi.columnsNamed);
		columns = new HashMap<Attribute, ColumnInfo>(mi.columns);
		numAttributes = mi.numAttributes;
	}

	/**
	 * Expand / contract constructor
	 * 
	 * @param mi
	 *            the MatrixInfo to copy
	 * @param columnName
	 *            the name of the attribute to expand or contract
	 */
	public MatrixInfo(MatrixInfo mi, String columnName) {

		// new mx info
		this(mi);

		// create modified column info
		ColumnInfo col = columnsNamed.get(columnName);
		ColumnInfo mod = col.modifyExpand();
		updateColumn(mod);
	}

	/**
	 * Drill-down constructor
	 * 
	 * @param mi
	 *            the MatrixInfo to copy
	 * @param avp
	 *            the attribute / value pair of the row in question
	 * @param removeSingle
	 *            true if this value should be removed, otherwise all other
	 *            values will be removed
	 */
	public MatrixInfo(MatrixInfo mi, AttributeValuePair avp,
			boolean removeSingle) {

		// new mx info
		this(mi);

		// create modified column info
		ColumnInfo col = columnsNamed.get(avp.getAttribute());
		ColumnInfo mod = col.modifyDrillDown(avp.getValue(), removeSingle);
		updateColumn(mod);

	}

	/**
	 * Replace existing columnInfo with a modified one
	 * 
	 * @param mod
	 */
	private void updateColumn(ColumnInfo mod) {
		columnsOrdered.set(mod.index, mod);
		columnsNamed.put(mod.name, mod);
		columns.put(mod.attribute, mod);
	}

	/**
	 * Gets a default target attribute if a user-specified one is lacking.
	 * 
	 * @return the default target attribute
	 */
	public String getDefaultTarget() {
		String userTarget = PatternExplorerOperator.getParamTarget();
		if (columnsNamed.containsKey(userTarget)) {
			return userTarget;
		} else {
			if (!userTarget.equals("")) {
				Log.out("Target '" + userTarget
						+ "' not found, using default attribute.",
						Log.WARNING_LEVEL);
			}
			return columnsOrdered.get(DEFAULT_INDEX).name;
		}
	}

	/**
	 * Gets all attribute names as an array of Strings
	 * 
	 * @return all attribute names
	 */
	public String[] getAttributeNames() {
		String[] names = new String[numAttributes];
		for (int i = 0; i < columnsOrdered.size(); i++) {
			names[i] = columnsOrdered.get(i).name;
		}
		return names;
	}

	/**
	 * Gets the i'th attribute
	 * 
	 * @param i
	 * @return the attribute
	 */
	public Attribute getAttribute(int i) {
		return columnsOrdered.get(i).attribute;
	}

	/**
	 * Gets the attribute with the specified name
	 * 
	 * @param s
	 * @return the attribute
	 */
	public Attribute getAttribute(String s) {
		return columnsNamed.get(s).attribute;
	}

	/**
	 * Gets the number of values possible for the specified attribute, taking
	 * into account whether the column is expanded.
	 * 
	 * @param a
	 * @return the number of values
	 */
	public int getNumValues(Attribute a) {
		ColumnInfo ci = columns.get(a);
		if (ci.isExpandable()) {
			return ci.compactWidth();
		}
		return ci.numValues;
	}

	/**
	 * Gets the number of values for the specified target attirbute.
	 * 
	 * @param target
	 * @return the number of values
	 */
	public int getNumValuesTarget(Attribute target) {
		return columns.get(target).numValues;
	}

	/**
	 * Gets the number of values for the specified target name.
	 * 
	 * @param target
	 * @return the number of values
	 */
	public int getNumValuesTarget(String target) {
		return columnsNamed.get(target).numValues;
	}

	/**
	 * Gets the total number of attributes.
	 * 
	 * @return the number of attributes
	 */
	public int getNumAttributes() {
		return numAttributes;
	}

	/**
	 * Gets the column index of attribute a if t is the target.
	 * 
	 * @param a
	 * @param t
	 * @return the index of attribute a
	 */
	public int getCol(Attribute a, Attribute t) {
		int a_index = columns.get(a).index;
		int t_index = columns.get(t).index;
		return (a_index <= t_index) ? a_index : a_index - 1;
	}

	/**
	 * Gets the column index of attribute named a if t is the target.
	 * 
	 * @param a
	 * @param t
	 * @return
	 */
	public int getCol(String a, Attribute t) {
		return getCol(columnsNamed.get(a).attribute, t);
	}

	/**
	 * Returns the attribute at index i if t is the target.
	 * 
	 * @param i
	 * @param t
	 * @return
	 */
	public Attribute getAttribute(int i, Attribute t) {
		int t_index = columns.get(t).index;
		return (i < t_index) ? columnsOrdered.get(i).attribute : columnsOrdered
				.get(i + 1).attribute;

	}

	/**
	 * Compacts the specified array of values for attribute a, to ignore values
	 * removed by drill-down operations.
	 * 
	 * @param a
	 * @param tCountsLong
	 * @return
	 */
	public int[] compact(Attribute a, int[] tCountsLong) {
		return columns.get(a).condense(tCountsLong);
	}

	/**
	 * Compacts the specified 2-d array of values for target attribute t and
	 * attribute a, to ignore values removed by drill-down operations.
	 * 
	 * @param t
	 * @param a
	 * @param countsLong
	 * @return
	 */
	public int[][] compact(Attribute t, Attribute a, int[][] countsLong) {
		return columns.get(t).condense(a, countsLong, this);
	}

	/**
	 * Gets the i'th value for attribute (column) a.
	 * 
	 * @param a
	 * @param i
	 * @return
	 */
	public String getValue(Attribute a, int i) {
		return columns.get(a).getValue(i);
	}

	/**
	 * Tests whether a drill down operation is possible for attribute a.
	 * 
	 * @param a
	 * @return
	 */
	public boolean canDrillDown(Attribute a) {
		return columns.get(a).numValues > 1;
	}

	/**
	 * Tests whether a drill down operation is possible for attribute named a.
	 * 
	 * @param a
	 * @return
	 */
	public boolean canDrillDown(String a) {
		return columnsNamed.get(a).numValues > 1;
	}

	/**
	 * Tests whether attribute a is expandable.
	 * 
	 * @param a
	 * @return
	 */
	public boolean isExpandable(Attribute a) {
		return columns.get(a).isExpandable();
	}

	/**
	 * Tests whether attribute a is expanded.
	 * 
	 * @param a
	 * @return
	 */
	public boolean isExpanded(Attribute a) {
		return columns.get(a).expanded;
	}

	/**
	 * Gets the total number of values in the dataset.
	 * 
	 * @return The total number of attribute values
	 */
	public int getTotalValues() {
		int total = 0;
		for (ColumnInfo c : columnsOrdered) {
			total += c.numValues;
		}
		return total;
	}

	/**
	 * Maintains info for columns in the histogram display.
	 * 
	 * @author Caleb Sotelo
	 * 
	 */
	private class ColumnInfo {

		Attribute attribute;
		String name;
		int numValues;
		int index;
		// maps new indeces to original indeces
		HashMap<Integer, Integer> values;
		boolean numeric;
		boolean expanded;

		// copy ctor
		ColumnInfo(ColumnInfo ci) {
			this.attribute = (AbstractAttribute) ci.attribute.clone();
			this.name = ci.name;
			this.numValues = ci.numValues;
			this.index = ci.index;
			this.values = new HashMap<Integer, Integer>(ci.values);
			this.numeric = ci.numeric;
			this.expanded = ci.expanded;
		}

		ColumnInfo(Attribute attribute, int index, boolean isNumerical) {
			this.attribute = attribute;
			this.name = attribute.getName();
			this.index = index;
			this.expanded = false;

			this.numeric = isNumerical;
			this.numValues = attribute.getMapping().getValues().size();

			this.values = new HashMap<Integer, Integer>();
			for (int i = 0; i < numValues; i++) {
				values.put(i, i);
			}
		}

		ColumnInfo modifyExpand() {
			ColumnInfo mod = new ColumnInfo(this);
			mod.toggleExpanded();
			return mod;
		}

		ColumnInfo modifyDrillDown(String value, boolean removeSingle) {
			ColumnInfo mod = new ColumnInfo(this);
			return mod.removeValue(value, removeSingle);
		}

		ColumnInfo removeValue(String value, boolean removeSingle) {
			int remove = attribute.getMapping().getIndex(value);
			if (removeSingle) {
				int old;
				boolean mark = false;
				int i;
				for (i = 0; i < values.size(); i++) {
					old = values.get(i);
					if (mark) {
						values.put(i - 1, old);
					} else {
						if (old == remove) {
							mark = true;
						}
					}
				}
				// remove last entry (it's a duplicate)
				values.remove(i - 1);
			} else {
				values.clear();
				values.put(0, remove);
			}
			numValues = values.size();
			return this;
		}

		int[] condense(int[] tCountsLong) {
			if (numValues == attribute.getMapping().size()) {
				return tCountsLong;
			}

			int[] tCounts = new int[numValues];

			//
			for (int i = 0; i < tCounts.length; i++) {
				tCounts[i] = tCountsLong[values.get(i)];
			}

			return tCounts;
		}

		int[][] condense(Attribute a, int[][] countsLong, MatrixInfo info) {
			ColumnInfo att = info.columns.get(a);
			if (numValues == attribute.getMapping().size()) {
				if (att.numValues == a.getMapping().size()) {
					return countsLong;
				}
			}
			int aVals = att.numValues;
			int[][] counts = new int[numValues][aVals];

			//
			for (int n = 0; n < counts.length; n++) {
				int cl_i = values.get(n);
				for (int i = 0; i < aVals; i++) {
					counts[n][i] = countsLong[cl_i][att.values.get(i)];
				}
			}
			return counts;
		}

		String getValue(int i) {
			int old = values.get(i);
			return attribute.getMapping().getValues().get(old);
		}

		int compactWidth() {
			if (!expanded) {
				return PatternExplorerSettings.NUM_COMPACT_VALUES;
			}
			return numValues;
		}

		boolean isExpandable() {
			return !numeric
					&& (numValues > PatternExplorerSettings.NUM_COMPACT_VALUES);
		}

		void toggleExpanded() {
			expanded = !expanded;
		}

	}

}
