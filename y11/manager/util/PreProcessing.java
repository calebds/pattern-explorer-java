package y11.manager.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import y11.operator.PatternExplorerOperator;
import y11.operator.PatternExplorerSettings;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.Statistics;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.preprocessing.discretization.DiscretizationModel;

/**
 * Manages a pipeline of preprocessing routines for completion before the input
 * dataset is ready for PatternExplorer.
 * 
 * @author Caleb Sotelo
 * 
 */
public class PreProcessing {

	/**
	 * Pipelines the specified ExampleSet through preprocessing routines
	 * 
	 * @param exampleSet
	 * @return
	 * @throws OperatorException
	 */
	public static ExampleSet preProcess(ExampleSet exampleSet)
			throws OperatorException {
		exampleSet = PreProcessing.binDiscretization(exampleSet);
		return PreProcessing.missingValueReplenishment(exampleSet);
	}

	/**
	 * Bins all numeric attributes into NUMERIC_BINS bins with equivalent ranges
	 * from minus infinity to positive infinity. Uses code from
	 * BinDiscretizationOperator.
	 * 
	 * @param exampleSet
	 * @return
	 * @throws OperatorException
	 * 
	 */
	public static ExampleSet binDiscretization(ExampleSet exampleSet)
			throws OperatorException {
		DiscretizationModel model = new DiscretizationModel(exampleSet);

		exampleSet.recalculateAllAttributeStatistics();
		int numberOfBins = PatternExplorerOperator.getParamBins();
		HashMap<Attribute, double[]> ranges = new HashMap<Attribute, double[]>();

		for (Attribute attribute : exampleSet.getAttributes()) {
			if (attribute.isNumerical()) { // skip nominal and date attributes
				double[] binRange = new double[numberOfBins];
				double min = exampleSet.getStatistics(attribute,
						Statistics.MINIMUM);
				double max = exampleSet.getStatistics(attribute,
						Statistics.MAXIMUM);
				for (int b = 0; b < numberOfBins - 1; b++) {
					binRange[b] = min
							+ (((double) (b + 1) / (double) numberOfBins) * (max - min));
				}
				binRange[numberOfBins - 1] = Double.POSITIVE_INFINITY;
				ranges.put(attribute, binRange);
			}
		}

		// determine number of digits
		int numberOfDigits = -1; // automatic number of digits

		model.setRanges(ranges, "range",
				DiscretizationModel.RANGE_NAME_INTERVAL, numberOfDigits);
		return model.apply(exampleSet);

	}

	/**
	 * Replenish missing values with value MISSING_VALUE. Uses code from
	 * MissingValueReplenishment operator.
	 * 
	 * @param eSet
	 * @return
	 */
	public static ExampleSet missingValueReplenishment(ExampleSet eSet) {
		eSet.recalculateAllAttributeStatistics();

		Iterator<Example> reader = eSet.iterator();
		while (reader.hasNext()) {
			Example example = reader.next();
			for (Attribute attribute : eSet.getAttributes()) {
				double value = example.getValue(attribute);
				if (Double.isNaN(value)) {
					example.setValue(attribute, attribute.getMapping()
							.mapString(PatternExplorerSettings.MISSING_VALUE_NAME));
				}
			}
			// TODO PatternExplorerOperator.get().doCheckForStop();
		}
		return eSet;
	}

	/**
	 * Returs a map of the attributes to a boolean that is true if the attribute
	 * should be treated numerically after it is discretized.
	 * 
	 * @param exampleSet
	 * @return
	 */
	public static Map getNumericFlags(ExampleSet exampleSet) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Iterator<Attribute> all = exampleSet.getAttributes().allAttributes();
		while (all.hasNext()) {
			Attribute a = all.next();
			if (a.isNumerical()) {
				map.put(a.getName(), null);
			}
		}
		return map;
	}

}
