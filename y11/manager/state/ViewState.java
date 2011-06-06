package y11.manager.state;

import y11.manager.Sigma;
import y11.manager.Zoom;

import com.rapidminer.example.ExampleSet;

/**
 * Abstract parent of classes maintaining references to history state
 * information.
 * 
 * @author Caleb Sotelo
 * 
 */
public abstract class ViewState {

	private static final String NAME_PREFIX = "State_";
	private static Integer num_states = 0;

	private String name; // unique state ID for debugging

	private ExampleSet exampleSet; // the actual data
	private MatrixInfo matrixInfo; // ordering info
	private Zoom zoom; // zoom setting
	private Sigma sigma; // sigma threshold
	private String target; // the target attribute

	private ViewState() {
		name = NAME_PREFIX + ++num_states;
	}

	/**
	 * Creates a new state with the specified references.
	 * 
	 * @param exampleSet
	 * @param matrixInfo
	 * @param zoom
	 * @param sigma
	 * @param target
	 */
	public ViewState(ExampleSet exampleSet, MatrixInfo matrixInfo, Zoom zoom,
			Sigma sigma, String target) {
		this();
		this.exampleSet = exampleSet;
		this.matrixInfo = matrixInfo;
		this.zoom = zoom;
		this.sigma = sigma;
		this.target = target;
	}

	/**
	 * Copy constructor, just makes new pointers.
	 * 
	 * @param view
	 */
	public ViewState(ViewState view) {
		this();
		exampleSet = view.exampleSet;
		matrixInfo = view.matrixInfo;
		zoom = view.zoom;
		sigma = view.sigma;
		target = view.target;
	}

	/**
	 * New view after drill-down
	 * 
	 * @param view
	 * @param matrixInfo
	 * @param exampleSet
	 */
	public ViewState(ViewState view, MatrixInfo matrixInfo,
			ExampleSet exampleSet) {
		this(view);
		this.matrixInfo = matrixInfo;
		this.exampleSet = exampleSet;
	}

	/**
	 * New view after expand/contract, hide/focus.
	 * 
	 * @param view
	 * @param m
	 */
	public ViewState(ViewState view, MatrixInfo matrixInfo) {
		this(view);
		this.matrixInfo = matrixInfo;
	}

	/**
	 * New view after zoom.
	 * 
	 * @param view
	 * @param z
	 */
	public ViewState(ViewState view, Zoom zoom) {
		this(view);
		this.zoom = zoom;
	}

	/**
	 * New view after sigma change
	 * 
	 * @param view
	 * @param s
	 */
	public ViewState(ViewState view, Sigma sigma) {
		this(view);
		this.sigma = sigma;
	}

	/**
	 * New view after target change
	 * 
	 * @param view
	 * @param t
	 */
	public ViewState(ViewState view, String target) {
		this(view);
		this.target = target;
	}

	/**
	 * Gets the name of this ViewState.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets this ViewState's ExampleSet.
	 * 
	 * @return the ExampleSet
	 */
	public ExampleSet getExampleSet() {
		return exampleSet;
	}

	/**
	 * Gets this ViewState's MatrixInfo.
	 * 
	 * @return the MatrixInfo
	 */
	public MatrixInfo getMatrixInfo() {
		return matrixInfo;
	}

	/**
	 * Gets this ViewState's target attribute.
	 * 
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * Gets this ViewState's sigma threshold
	 * 
	 * @return the sigma value
	 */
	public double getSigmaValue() {
		return sigma.getThreshold();
	}

	/**
	 * Gets this ViewState's zoom value
	 * 
	 * @return the zoom value
	 */
	public double getZoomValue() {
		return zoom.getScale();
	}

	/**
	 * Resets the number of states to one. Used for resetting history.
	 */
	public static void resetNumStates() {
		num_states = 1;
	}

	/**
	 * Tests if this ViewState is a FullViewState
	 * 
	 * @return true if this ViewState is a FullViewState
	 */
	public boolean isFullViewState() {
		return false;
	}

	/**
	 * Tests if this ViewState is a FullViewState
	 * 
	 * @return true if this ViewState is a LeanViewState
	 */
	public boolean isLeanViewState() {
		return false;
	}

}
