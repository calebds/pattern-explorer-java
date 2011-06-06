package y11.manager.loader;

import y11.manager.Sigma;
import y11.manager.Zoom;
import y11.manager.state.MatrixInfo;
import y11.manager.state.ViewState;
import y11.matrix.UserCancelledException;
import y11.matrix.Matrix;
import y11.operator.PatternExplorerSettings;

import com.rapidminer.example.ExampleSet;

/**
 * A default view strategy. Maintains a FastViewLoader and a LeanViewLoader,
 * switching between them based on the availability of memory.
 * 
 * @author Caleb Sotelo
 * 
 */
public class DefaultViewLoader extends ViewLoader {

	public static final String NAME = "DEFAULT_VIEW_LOADER";
	private static final ViewLoader THIS = new DefaultViewLoader(NAME);
	private ViewLoader loader;

	/**
	 * Creates a new DefaultViewLoader with the specified name.
	 * 
	 * @param name
	 *            the name of the ViewLoader
	 */
	public DefaultViewLoader(String name) {
		super(name);
		loader = FastViewLoader.get();
	}

	/**
	 * Selects the optimal loader to use based on the available amount of
	 * memory. If a minimum amount of memory has been attained, the loader is
	 * changed from FastViewLoader (default) to LeanViewLoader. The threshold
	 * can be changed in PatternExplorerSettings.
	 */
	public void optimize() {
		double freeMem = Runtime.getRuntime().freeMemory();
		if (freeMem <= PatternExplorerSettings.MIN_FREE_LOADER_BYTES) {
			loader = LeanViewLoader.get();
		} else {
			loader = FastViewLoader.get();
		}
	}

	/**
	 * Gets the only DefaultViewLoader instance.
	 * 
	 * @return the ViewLoader instance
	 */
	public static ViewLoader get() {
		return THIS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDefaultViewLoader() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Matrix getMatrix() {
		return loader.getMatrix();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMatrix(Matrix matrix) {
		loader.setMatrix(matrix);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViewState make(ViewState view, MatrixInfo matrixInfo,
			ExampleSet exampleSet) throws UserCancelledException {
		return loader.make(view, matrixInfo, exampleSet);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViewState make(ViewState view, MatrixInfo matrixInfo,
			String attribute) {
		return loader.make(view, matrixInfo, attribute);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViewState make(ViewState view, Zoom zoom) {
		return loader.make(view, zoom);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViewState make(ViewState view, Sigma sigma)
			throws UserCancelledException {
		return loader.make(view, sigma);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViewState make(ViewState view, String target)
			throws UserCancelledException {
		return loader.make(view, target);
	}

}
