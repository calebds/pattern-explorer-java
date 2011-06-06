package y11.manager.state;

import y11.manager.Sigma;
import y11.manager.Zoom;

import com.rapidminer.example.ExampleSet;

/**
 * View state for LeanViewLoader loading strategy. Does not maintains a
 * reference to the Matrix for each history state.
 * 
 * @author Caleb Sotelo
 * 
 */
public class LeanViewState extends ViewState {

	/**
	 * See documentation for super constructor.
	 * 
	 * @param exampleSet
	 * @param matrixInfo
	 * @param zoom
	 * @param sigma
	 * @param target
	 */
	public LeanViewState(ExampleSet exampleSet, MatrixInfo matrixInfo,
			Zoom zoom, Sigma sigma, String target) {
		super(exampleSet, matrixInfo, zoom, sigma, target);
	}

	/**
	 * See documentation for super constructor.
	 * 
	 * @param view
	 * @param matrixInfo
	 * @param exampleSet
	 */
	public LeanViewState(ViewState view, MatrixInfo matrixInfo,
			ExampleSet exampleSet) {
		super(view, matrixInfo, exampleSet);
	}

	/**
	 * See documentation for super constructor.
	 * 
	 * @param view
	 * @param matrixInfo
	 */
	public LeanViewState(ViewState view, MatrixInfo matrixInfo) {
		super(view, matrixInfo);
	}

	/**
	 * See documentation for super constructor.
	 * 
	 * @param view
	 * @param zoom
	 */
	public LeanViewState(ViewState view, Zoom zoom) {
		super(view, zoom);
	}

	/**
	 * See documentation for super constructor.
	 * 
	 * @param view
	 * @param sigma
	 */
	public LeanViewState(ViewState view, Sigma sigma) {
		super(view, sigma);
	}

	/**
	 * See documentation for super constructor.
	 * 
	 * @param view
	 * @param target
	 */
	public LeanViewState(ViewState view, String target) {
		super(view, target);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLeanViewState() {
		return true;
	}

}
