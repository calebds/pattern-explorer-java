package y11.operator;

import java.util.List;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeInt;
import com.rapidminer.parameter.ParameterTypeString;

/**
 * The PatternExplorer operator.
 * 
 * @author Caleb Sotelo
 */
public class PatternExplorerOperator extends Operator {

	private static int paramCores;
	private static String paramTarget;
	private static int paramBins;
	private static int maxCores = Runtime.getRuntime().availableProcessors();

	public static final String PARAMETER_CORES = "Number of threads to use for counting ("
			+ maxCores + " cores available)";
	public static final String PARAMETER_TARGET = "Name of starting target attribute (leave empty for default)";
	public static final String PARAMETER_BINS = "Number of bins for discretization of numeric attributes";

	/**
	 * Gets the PARAMETER_CORES parameter
	 * 
	 * @return
	 */
	public static int getParamCores() {
		return paramCores;
	}

	/**
	 * Gets the PARAMETER_TARGET parameter
	 * 
	 * @return
	 */
	public static String getParamTarget() {
		return paramTarget;
	}

	/**
	 * Gets the PARAMETER_BINS parameter
	 * 
	 * @return
	 */
	public static int getParamBins() {
		return paramBins;
	}

	/** Creates a new PatternExplorerOperator */
	public PatternExplorerOperator(OperatorDescription description) {
		super(description);
	}

	/** Applies the operator */
	public IOObject[] apply() throws OperatorException {

		ExampleSet exampleSet = getInput(ExampleSet.class);

		paramCores = getParameterAsInt(PARAMETER_CORES);
		paramTarget = getParameterAsString(PARAMETER_TARGET);
		paramBins = getParameterAsInt(PARAMETER_BINS);

		PatternExplorer explorer = new PatternExplorer(exampleSet);

		return new IOObject[] { exampleSet, explorer };

	}

	/** Specify parameters for user input */
	public List<ParameterType> getParameterTypes() {

		List<ParameterType> types = super.getParameterTypes();
		ParameterType type = new ParameterTypeInt(PARAMETER_CORES,
				PARAMETER_CORES, 1, 100, maxCores);
		type.setExpert(false);
		types.add(type);
		type = new ParameterTypeString(PARAMETER_TARGET, PARAMETER_TARGET, "");
		type.setExpert(false);
		types.add(type);
		type = new ParameterTypeInt(PARAMETER_BINS, PARAMETER_BINS, 1, 100,
				PatternExplorerSettings.DEFAULT_NUMERIC_BINS);
		type.setExpert(false);
		types.add(type);
		return types;
	}

	/** Requires an example set as input */
	public Class<?>[] getInputClasses() {
		return new Class[] { ExampleSet.class };
	}

	/** No output is produced */
	public Class<?>[] getOutputClasses() {
		return null;
	}
}
