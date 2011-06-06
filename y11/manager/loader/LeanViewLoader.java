package y11.manager.loader;

import y11.manager.Sigma;
import y11.manager.Zoom;
import y11.manager.state.FullViewState;
import y11.manager.state.LeanViewState;
import y11.manager.state.MatrixInfo;
import y11.manager.state.ViewState;
import y11.matrix.UserCancelledException;
import y11.matrix.Matrix;

import com.rapidminer.example.ExampleSet;

/**
 * A leaner loading strategy. Saves memory by recalculating the matrix for
 * history traversals, at the cost of computing time.
 * 
 * @author Caleb Sotelo
 * 
 */
public class LeanViewLoader extends ViewLoader {

	public static final String NAME = "LEAN_VIEW_LOADER";
	private static final ViewLoader THIS = new LeanViewLoader(NAME);

	/**
	 * Gets the only LeanViewLoader instance.
	 * 
	 * @return the ViewLoader instance
	 */
	public static ViewLoader get() {
		return THIS;
	}

	/**
	 * Creates a new LeanViewLoader with the specified name
	 * 
	 * @param name
	 *            the ViewLoader name
	 */
	public LeanViewLoader(String name) {
		super(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLeanViewLoader() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViewState make(ExampleSet exampleSet, MatrixInfo matrixInfo,
			Zoom zoom, Sigma sigma, String target)
			throws UserCancelledException {
		FullViewState v = new FullViewState(exampleSet, matrixInfo, new Zoom(),
				new Sigma(), target);
		setMatrix(Matrix.createMatrix(v));
		v.setMatrix(getMatrix());
		return v;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViewState make(ViewState view, MatrixInfo matrixInfo,
			ExampleSet exampleSet) throws UserCancelledException {
		LeanViewState v = new LeanViewState(view, matrixInfo, exampleSet);
		setMatrix(Matrix.createMatrix(v));
		return v;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViewState make(ViewState view, MatrixInfo matrixInfo,
			String attribute) {
		LeanViewState v = new LeanViewState(view, matrixInfo);
		// setMatrix(Matrix.createMatrix(v));
		setMatrix(Matrix.createMatrixII(getMatrix(), matrixInfo, v, attribute));
		return v;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViewState make(ViewState view, Zoom zoom) {
		return new LeanViewState(view, zoom);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViewState make(ViewState view, Sigma sigma)
			throws UserCancelledException {
		LeanViewState v = new LeanViewState(view, sigma);
		setMatrix(Matrix.createMatrix(v));
		return v;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViewState make(ViewState view, String target)
			throws UserCancelledException {
		LeanViewState v = new LeanViewState(view, target);
		setMatrix(Matrix.createMatrix(v));
		return v;
	}

}
