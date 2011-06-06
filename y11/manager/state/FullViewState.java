package y11.manager.state;

import y11.manager.Sigma;
import y11.manager.Zoom;
import y11.matrix.Matrix;

import com.rapidminer.example.ExampleSet;

/**
 * View state for FastViewLoader loading strategy. Maintains a reference to
 * the Matrix for each history state.
 * 
 * @author Caleb Sotelo
 * 
 */
public class FullViewState extends LeanViewState {

	private Matrix matrix;

	/**
	 * See documentation for super constructor.
	 * 
	 * @param exampleSet
	 * @param matrixInfo
	 * @param zoom
	 * @param sigma
	 * @param target
	 */
	public FullViewState(ExampleSet exampleSet, MatrixInfo matrixInfo,
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
	public FullViewState(ViewState view, MatrixInfo matrixInfo,
			ExampleSet exampleSet) {
		super(view, matrixInfo, exampleSet);
	}

	/**
	 * See documentation for super constructor.
	 * 
	 * @param view
	 * @param matrixInfo
	 */
	public FullViewState(ViewState view, MatrixInfo matrixInfo) {
		super(view, matrixInfo);
	}

	/**
	 * See documentation for super constructor.
	 * 
	 * @param view
	 * @param zoom
	 */
	public FullViewState(ViewState view, Zoom zoom) {
		super(view, zoom);
	}

	/**
	 * See documentation for super constructor.
	 * 
	 * @param view
	 * @param sigma
	 */
	public FullViewState(ViewState view, Sigma sigma) {
		super(view, sigma);
	}

	/**
	 * See documentation for super constructor.
	 * 
	 * @param view
	 * @param target
	 */
	public FullViewState(ViewState view, String target) {
		super(view, target);
	}

	/**
	 * Gets this ViewState's Matrix.
	 * 
	 * @return the Matrix
	 */
	public Matrix getMatrix() {
		return matrix;
	}

	/**
	 * Sets this ViwState's Matrix.
	 * 
	 * @param matrix
	 */
	public void setMatrix(Matrix matrix) {
		this.matrix = matrix;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFullViewState() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLeanViewState() {
		return false;
	}

}
