package y11.manager.loader;

import y11.manager.Sigma;
import y11.manager.Zoom;
import y11.manager.state.FullViewState;
import y11.manager.state.MatrixInfo;
import y11.manager.state.ViewState;
import y11.matrix.UserCancelledException;
import y11.matrix.Matrix;

import com.rapidminer.example.ExampleSet;

/**
 * A faster loading strategy. Saves a reference to the matrix in each state, to
 * reduce computing time, at the cost of memory.
 * 
 * @author Caleb Sotelo
 * 
 */
public class FastViewLoader extends ViewLoader {

	public static final String NAME = "FAST_VIEW_LOADER";
	private static final ViewLoader THIS = new FastViewLoader(NAME);

	/**
	 * Gets the only FastViewLoader instance.
	 * 
	 * @return the ViewLoader instance
	 */
	public static ViewLoader get() {
		return THIS;
	}

	/**
	 * Creates a new FastViewLoader with the specified name.
	 * 
	 * @param name
	 *            the name of the ViewLoader
	 */
	public FastViewLoader(String name) {
		super(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFastViewLoader() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViewState make(ViewState view, MatrixInfo matrixInfo,
			ExampleSet exampleSet) throws UserCancelledException {
		FullViewState v = new FullViewState(view, matrixInfo, exampleSet);
		setMatrix(Matrix.createMatrix(v));
		v.setMatrix(getMatrix());
		return v;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViewState make(ViewState view, MatrixInfo matrixInfo,
			String attribute) {
		FullViewState v = new FullViewState(view, matrixInfo);
		// setMatrix(Matrix.createMatrix(view));
		setMatrix(Matrix.createMatrixII(getMatrix(), matrixInfo, v, attribute));
		v.setMatrix(getMatrix());
		return v;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViewState make(ViewState view, Zoom zoom) {
		FullViewState v = new FullViewState(view, zoom);
		v.setMatrix(getMatrix()); // no need for new matrix, just changed sizes
		return v;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViewState make(ViewState view, Sigma sigma)
			throws UserCancelledException {
		FullViewState v = new FullViewState(view, sigma);
		setMatrix(Matrix.createMatrix(v));
		v.setMatrix(getMatrix());
		return v;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViewState make(ViewState view, String target)
			throws UserCancelledException {
		FullViewState v = new FullViewState(view, target);
		setMatrix(Matrix.createMatrix(v));
		v.setMatrix(getMatrix());
		return v;
	}

}
