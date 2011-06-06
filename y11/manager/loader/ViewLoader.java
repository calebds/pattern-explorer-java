package y11.manager.loader;

import com.rapidminer.example.ExampleSet;

import y11.logging.Log;
import y11.manager.Sigma;
import y11.manager.Zoom;
import y11.manager.state.FullViewState;
import y11.manager.state.MatrixInfo;
import y11.manager.state.ViewState;
import y11.matrix.UserCancelledException;
import y11.matrix.Matrix;
import y11.matrix.ProgressManager;

/**
 * Abstract parent of all history loading strategies. Loads views after history
 * traversals and makes new views using the strategy of the concrete child.
 * 
 * @author Caleb
 * 
 */
public abstract class ViewLoader {

	private Matrix matrix; // the current matrix
	private String name;

	/**
	 * Creates a new loader with the specified name.
	 * 
	 * @param name
	 *            the name of this loader
	 */
	public ViewLoader(String name) {
		this.name = name;
	}

	/**
	 * Gets the name of this loader.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the matrix saved by this loader
	 * 
	 * @return
	 */
	public Matrix getMatrix() {
		return matrix;
	}

	/**
	 * Sets this loader's matrix
	 * 
	 * @param matrix
	 *            the matrix to set
	 */
	public void setMatrix(Matrix matrix) {
		this.matrix = matrix;
	}

	/**
	 * Tests if this loader is a DefaultViewLoader
	 * 
	 * @return True if this loader is a DefaultViewLoader
	 */
	public boolean isDefaultViewLoader() {
		return false;
	}

	/**
	 * Tests if this loader is a FastViewLoader
	 * 
	 * @return True if this loader is a FastViewLoader
	 */
	public boolean isFastViewLoader() {
		return false;
	}

	/**
	 * Tests if this loader is a LeanViewLoader
	 * 
	 * @return True if this loader is a LeanViewLoader
	 */
	public boolean isLeanViewLoader() {
		return false;
	}

	/**
	 * Makes the first state. The first state always saves the master reference
	 * to the exampleSet.
	 * 
	 * @param exampleSet
	 * @param matrixInfo
	 * @param zoom
	 * @param sigma
	 * @param target
	 * @return
	 * @throws UserCancelledException
	 */
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
	 * Makes a view after drill-down, using this loader's strategy.
	 * 
	 * @param view
	 * @param exampleSet
	 * @return the ViewState created
	 * @throws UserCancelledException
	 */
	public abstract ViewState make(ViewState view, MatrixInfo matrixInfo,
			ExampleSet exampleSet) throws UserCancelledException;

	/**
	 * Makes a new view after expansion/contraction or attribute hide, using
	 * this loader's strategy.
	 * 
	 * @param view
	 * @param matrixInfo
	 * @return the ViewState created
	 */
	public abstract ViewState make(ViewState view, MatrixInfo matrixInfo,
			String attribute);

	/**
	 * Makes a new view after zoom change, using this loader's strategy.
	 * 
	 * @param view
	 * @param zoom
	 * @return
	 */
	public abstract ViewState make(ViewState view, Zoom zoom);

	/**
	 * Makes a new view after sigma threshold change, using this loader's
	 * strategy.
	 * 
	 * @param view
	 * @param sigma
	 * @return the ViewState created
	 * @throws UserCancelledException
	 */
	public abstract ViewState make(ViewState view, Sigma sigma)
			throws UserCancelledException;

	/**
	 * Makes a new view after target change, using this loader's strategy.
	 * 
	 * @param view
	 * @param target
	 * @return
	 * @throws UserCancelledException
	 */
	public abstract ViewState make(ViewState view, String target)
			throws UserCancelledException;

	/**
	 * Determine the view strategy for the state to load, then calculates the
	 * new matrix and/or loads it accordingly.
	 * 
	 * @param view
	 * @throws UserCancelledException
	 */
	public void load(ViewState view, ViewState lastView, boolean isReset)
			throws UserCancelledException {
		if (view.isFullViewState()) {
			setMatrix(((FullViewState) view).getMatrix());
			ProgressManager.setDidNotRun();
		} else if (view.isLeanViewState()) {
			if (view.getZoomValue() == lastView.getZoomValue()) {
				setMatrix(Matrix.createMatrix(view));
			} else {
				ProgressManager.setDidNotRun();
			}
		}
	}

}
