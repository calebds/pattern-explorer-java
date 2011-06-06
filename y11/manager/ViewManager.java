package y11.manager;

import java.awt.Cursor;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.OperatorException;

import y11.GUI.GUISettings;
import y11.GUI.canvas.CanvasComponent;
import y11.GUI.canvas.ColumnLabel;
import y11.GUI.canvas.ExpandButton;
import y11.GUI.canvas.Label;
import y11.GUI.canvas.MatrixRenderer;
import y11.GUI.canvas.RemoveButton;
import y11.GUI.canvas.RowLabel;
import y11.GUI.histogram.DetailedHistogram;
import y11.GUI.histogram.Histogram;
import y11.click.Clickable;
import y11.click.ClickableRegionMap;
import y11.logging.Log;
import y11.manager.loader.DefaultViewLoader;
import y11.manager.loader.FastViewLoader;
import y11.manager.loader.LeanViewLoader;
import y11.manager.loader.ViewLoader;
import y11.manager.state.MatrixInfo;
import y11.manager.state.ViewState;
import y11.manager.util.HistoryContainer;
import y11.manager.util.PreProcessing;
import y11.matrix.UserCancelledException;
import y11.matrix.Matrix;
import y11.matrix.ProgressManager;
import y11.models.AttributeValuePair;
import y11.models.histogram.HistogramModel;
import y11.operator.PatternExplorerSettings;

/**
 * Business logic for the PatternExplorer operator. Manages user interactions
 * from PatternExplorerGUI and maintains state. Calls methods on the
 * MatrixRenderer to render and display the histograms.
 * 
 * @author Caleb Sotelo
 * 
 */
public class ViewManager {

	public static final boolean REMOVE_VALUE = true;
	public static final boolean FOCUS_VALUE = false;

	private HistoryContainer<ViewState> history; // manage the states
	private ViewLoader loader; // the current load strategy
	private MatrixRenderer matrixRenderer; // renderer from the GUI
	private ViewState currentState; // the current state
	private HashMap<String, ViewLoader> loaders; // the loading strategies
	private DetailedHistogram detailedGraph;
	private int cursor;
	private ClickableRegionMap clickMap;
	private String toolTipText = GUISettings.EMPTY_TOOLTIP_TXT;

	/**
	 * Creates a new ViewManager
	 * 
	 * @param exampleSet
	 *            The starting ExampleSet
	 * @param matrixRenderer
	 *            The MatrixRenderer for rendering the histogram display
	 */
	public ViewManager(ExampleSet exampleSet, MatrixRenderer matrixRenderer) {
		// initialize members
		this.matrixRenderer = matrixRenderer;
		clickMap = matrixRenderer.getClickMap();

		// initialize loaders
		loaders = new HashMap<String, ViewLoader>();
		loaders.put(DefaultViewLoader.NAME, DefaultViewLoader.get());
		loaders.put(FastViewLoader.NAME, FastViewLoader.get());
		loaders.put(LeanViewLoader.NAME, LeanViewLoader.get());

		loader = loaders.get(PatternExplorerSettings.START_LOADER);// DefaultViewLoader.get();

		// initialize history manager
		history = new HistoryContainer<ViewState>();

		// preprocessing
		Map numericFlags = PreProcessing.getNumericFlags(exampleSet);
		ExampleSet exampleSetNew = null;

		try {
			exampleSetNew = PreProcessing.preProcess(exampleSet);
		} catch (OperatorException e) {
			e.printStackTrace();
		}

		MatrixInfo matrixInfo = new MatrixInfo(exampleSetNew, numericFlags);

		String target = matrixInfo.getDefaultTarget();
		Zoom zoom = new Zoom();
		Sigma sigma = new Sigma();

		ProgressManager.resetProgress();
		ViewState firstState;
		try {
			firstState = loader.make(exampleSetNew, matrixInfo, zoom, sigma,
					target);
			history.addFirst(firstState);
			currentState = firstState;
		} catch (UserCancelledException e) {
			// Should never happen, user can't cancel the first operation
			e.printStackTrace();
		}
	}

	/**
	 * Renders the matrix
	 */
	public void renderMatrix() {
		matrixRenderer.setModel(loader.getMatrix());
		matrixRenderer.setScale((int) currentState.getZoomValue());
		HistogramModel.setSigma(currentState.getSigmaValue());
		detailedGraph = null;
		matrixRenderer.repaint();
		toolTipText = GUISettings.EMPTY_TOOLTIP_TXT;
	}

	private void moveToNewState(ViewState newState) {
		history.add(newState);
		currentState = newState;
		renderMatrix();
		System.gc(); // put out the trash
		if (loader.isDefaultViewLoader()) {
			((DefaultViewLoader) loader).optimize();
		}
	}

	private void moveToExistingState(ViewState existingState, boolean isReset)
			throws UserCancelledException {
		loader.load(existingState, currentState, isReset);
		currentState = existingState;
		renderMatrix();

		System.gc(); // put out the trash
	}

	/**
	 * Gets the current cursor
	 * 
	 * @return the cursor
	 */
	public int getCursor() {
		return cursor;
	}

	/**
	 * Gets the current tool tip text
	 * 
	 * @return the ToolTipText
	 */
	public String getToolTipText() {
		return toolTipText;
	}

	/**
	 * Tests if the loader is default
	 * 
	 * @return true if the loader is default loader
	 */
	public boolean isDefaultOptEnabled() {
		return loader.isDefaultViewLoader();
	}

	/**
	 * Tests if the loader is memory
	 * 
	 * @return true if the loader is lean loader
	 */
	public boolean isMemOptEnabled() {
		return loader.isLeanViewLoader();
	}

	/**
	 * Tests if the loader is time
	 * 
	 * @return true if the loader is fast loader
	 */
	public boolean isTimeOptEnabled() {
		return loader.isFastViewLoader();
	}

	/**
	 * Gets the current sigma slider value
	 * 
	 * @return the sigma slider value
	 */
	public double getCurrentSigmaSliderValue() {
		return currentState.getSigmaValue() * Sigma.FACTOR;
	}

	/**
	 * Converts the specified slider value to a threshold value
	 * 
	 * @param sliderVal
	 *            the slider value
	 * @return The sigma threshold
	 */
	public double convertSigmaSliderToThreshold(double sliderVal) {
		return sliderVal / Sigma.FACTOR;
	}

	/**
	 * Gets the current zoom slider value
	 * 
	 * @return
	 */
	public double getCurrentZoomSliderValue() {
		double scale = currentState.getZoomValue();
		double sliderVal;
		if (scale < GUISettings.ZOOM_DEFAULT_SCALE) {
			sliderVal = GUISettings.ZOOM_SLIDER_INIT
					- (GUISettings.ZOOM_DEFAULT_SCALE - scale);
		} else if (scale > GUISettings.ZOOM_DEFAULT_SCALE) {
			sliderVal = (scale / 10) - 10 + GUISettings.ZOOM_SLIDER_INIT;
		} else {
			sliderVal = GUISettings.ZOOM_SLIDER_INIT;
		}
		return sliderVal;
	}

	/**
	 * Converts the specified zoom slider value to a scale
	 * 
	 * @param sliderVal
	 *            The slider value
	 * @return The scale
	 */
	public double convertZoomSliderToScale(double sliderVal) {
		double scale;
		if (sliderVal < GUISettings.ZOOM_SLIDER_INIT) {
			scale = GUISettings.ZOOM_DEFAULT_SCALE
					- (GUISettings.ZOOM_SLIDER_INIT - sliderVal);
		} else if (sliderVal > GUISettings.ZOOM_SLIDER_INIT) {
			scale = 10 * (sliderVal - GUISettings.ZOOM_SLIDER_INIT + 10);
		} else {
			scale = GUISettings.ZOOM_DEFAULT_SCALE;
		}
		return scale;
	}

	/**
	 * Gets the currently selected target
	 * 
	 * @return The name of the target attribute
	 */
	public String getSelectedTarget() {
		return currentState.getTarget();
	}

	/**
	 * Gets the list of all attributes which are valid targets
	 * 
	 * @return An array of attribute names
	 */
	public String[] getTargetList() {
		return currentState.getMatrixInfo().getAttributeNames();
	}

	/**
	 * Tests if the history controls can step back
	 * 
	 * @return True if step back is possible.
	 */
	public boolean canStepBack() {
		return history.canStepBack();
	}

	/**
	 * Tests if the history controls can step forward
	 * 
	 * @return True if step forward is possible.
	 */
	public boolean canStepForward() {
		return history.canStepForward();
	}

	/**
	 * Tests if the history controls can reset
	 * 
	 * @return True if reset is possible.
	 */
	public boolean canReset() {
		return history.canReset();
	}

	/**
	 * Gets the currently selected history loading strategy
	 * 
	 * @return The name of the load strategy
	 */
	public Object getSelectedHistOpt() {
		return loader.getName();
	}

	/**
	 * Gets the current detail graph
	 * 
	 * @return The detail graph component
	 */
	public JPanel getDetailedGraph() {
		if (detailedGraph == null) {
			return null;
		} else {
			return detailedGraph.getChartPanel();
		}
	}

	/**
	 * Changes the target to the attribute specified. Operation can be canceled
	 * by the user.
	 * 
	 * @param target
	 *            The target to switch to
	 */
	public void doChangeTarget(String target) {
		Log.out("changing target to '" + target + "' ..");
		ViewState newState;
		try {
			newState = loader.make(currentState, target);
			moveToNewState(newState);
			Log.out(".. completed");
		} catch (UserCancelledException e) {
			// operation canceled so don't do anything
		}

	}

	/**
	 * Changes the sigma value to the value calculated from the specified sigma
	 * slider value.
	 * 
	 * @param sliderVal
	 *            The slider value to use
	 */
	public void doChangeSigma(double sliderVal) {
		double threshold = this.convertSigmaSliderToThreshold(sliderVal);
		HistogramModel.setSigma(threshold);
		matrixRenderer.repaint();
	}

	/**
	 * Changes the sigma value to the value calculated from the specified sigma
	 * slider value, and adds a new entry to history.
	 * 
	 * @param sliderVal
	 *            The slider value to use
	 */
	public void doChangeAndSetSigma(double sliderVal) {
		double threshold = this.convertSigmaSliderToThreshold(sliderVal);
		Sigma newSigma = new Sigma(threshold);
		ViewState newState;
		try {
			newState = loader.make(currentState, newSigma);
			moveToNewState(newState);
			Log.out("changed statistical significance threshold to "
					+ threshold + " ..");
		} catch (UserCancelledException e) {
			// operation canceled so don't do anything
		}

	}

	/**
	 * Changes the scale to the value calculated from the specified zoom slider
	 * value.
	 * 
	 * @param sliderVal
	 *            The slider value to use
	 */
	public void doChangeZoom(int sliderVal) {
		double scale = convertZoomSliderToScale(sliderVal);
		matrixRenderer.setScale((int) scale);
		matrixRenderer.repaint();
	}

	/**
	 * Changes the zoom scale to the value calculated from the specified zoom
	 * slider value, and adds a new entry to history.
	 * 
	 * @param sliderVal
	 *            The slider value to use
	 */
	public void doChangeAndSetZoom(int sliderVal) {
		double scale = convertZoomSliderToScale(sliderVal);
		Zoom zoom = new Zoom(scale);
		ViewState newState = loader.make(currentState, zoom);
		moveToNewState(newState);
		Log.out("changed zoom scale to " + scale);
	}

	/**
	 * Drills down on the data by removing or focusing on a single value of the
	 * target (histogram row), and adds a new entry to history. This operation
	 * can be cancelled by the user.
	 * 
	 * @param avp
	 *            The attribute/value pair in question
	 * @param removeSingle
	 *            If true, removes the row specified, otherwise removes all oher
	 *            rows.
	 */
	public void doDrillDown(AttributeValuePair avp, boolean removeSingle) {
		Log.out((removeSingle ? "removing target value"
				: "focusing on target value")
				+ " '" + avp.getValue() + "' ..");
		ExampleSet exampleSet;
		exampleSet = Matrix.createDrilledDownExampleSet(currentState
				.getExampleSet(), avp, removeSingle);
		MatrixInfo matrixInfo = new MatrixInfo(currentState.getMatrixInfo(),
				avp, removeSingle);
		ViewState newState;
		try {
			newState = loader.make(currentState, matrixInfo, exampleSet);
			moveToNewState(newState);
			Log.out(".. completed");
		} catch (UserCancelledException e) {
			// operation cancelled so don't do anything
		}
	}

	/**
	 * Either expands or contracts the column specified, and adds a new entry to
	 * history.
	 * 
	 * @param attribute
	 *            The attribute to expand/contract
	 */
	public void doExpandContractColumn(String attribute) {
		MatrixInfo matrixInfo = new MatrixInfo(currentState.getMatrixInfo(),
				attribute);
		ViewState newState = loader.make(currentState, matrixInfo, attribute);
		moveToNewState(newState);
		Log.out("expanded/contracted attribute '" + attribute + "'");
	}

	// HISTORY

	/**
	 * Steps to previous state in history. This operation can be cancelled by
	 * the user.
	 */
	public void doStepBack() {
		Log.out("stepping back in history ..");
		if (history.stepBack()) {
			try {
				moveToExistingState(history.getCurrent(), false);
				Log.out(".. completed");
			} catch (UserCancelledException e) {
				// operation cancelled so don't do anything
			}
		}
	}

	/**
	 * Steps to next state in history. This operation can be cancelled by the
	 * user.
	 */
	public void doStepForward() {
		Log.out("stepping forward in history ..");
		if (history.stepForward()) {
			try {
				moveToExistingState(history.getCurrent(), false);
				Log.out(".. completed");
			} catch (UserCancelledException e) {
				// operation cancelled so don't do anything
			}
		}

	}

	/**
	 * Resets history to the initial state. This operation can be cancelled by
	 * the user.
	 */
	public void doReset() {
		Log.out("resetting history ..");
		if (history.reset()) {
			try {
				moveToExistingState(history.getCurrent(), true);
				Log.out(".. completed");
			} catch (UserCancelledException e) {
				// operation cancelled so don't do anything
			}
		}
	}

	/**
	 * Changes the history load strategy to the strategy specified.
	 * 
	 * @param action
	 *            The name of the strategy to use.
	 */
	public void doChangeHistOpt(String action) {
		Matrix matrix = loader.getMatrix();
		loader = loaders.get(action);
		loader.setMatrix(matrix);
		Log.out("changed history strategy to " + action);
	}

	/**
	 * Implement this functionality optionally..
	 */
	public void doTurnOffHistory() {
	}

	// MOUSE EVENTS

	/**
	 * Reponds to a click event on the histogram display and delegates control
	 * to the proper procedure. Some operations can be cancelled by the user.
	 * Those that cannot assert ProgressManager.setDidNotRun();
	 * 
	 * @param p
	 *            The point at which the event occurs
	 */
	public void doGraphClicked(Point p) {
		Clickable c = clickMap.get(p);
		if (c != null) {
			if (c instanceof Histogram) {
				Histogram h = (Histogram) c;
				detailedGraph = new DetailedHistogram(h.getModel());
				ProgressManager.setDidNotRun();
			} else if (c instanceof ColumnLabel) {
				Label lbl = (Label) c;
				doChangeTarget(lbl.getText());
			} else if (c instanceof RowLabel) {
				// drill down on value
				RowLabel lbl = (RowLabel) c;
				if (lbl.canDrillDown()) {
					doDrillDown(lbl.getAvp(), FOCUS_VALUE);
				} else {
					ProgressManager.setDidNotRun();
				}
			} else if (c instanceof RemoveButton) {
				// remove value
				RemoveButton btn = (RemoveButton) c;
				doDrillDown(btn.getAvp(), REMOVE_VALUE);
			} else if (c instanceof ExpandButton) {
				// expand or contract
				ExpandButton btn = (ExpandButton) c;
				doExpandContractColumn(btn.getAttribute());
				ProgressManager.setDidNotRun();
			} else {
				// other clickables
			}
		} else {
			ProgressManager.setDidNotRun();
		}
	}

	/**
	 * Reponds to a mouse move event on the histogram display and delegates
	 * control to the proper procedure.
	 * 
	 * @param p
	 *            The point at which the event occurs
	 */
	public void doGraphMouseMoved(Point p) {
		Clickable c = clickMap.get(p);
		if (c != null) {
			cursor = Cursor.HAND_CURSOR;
			toolTipText = ((CanvasComponent) c).getToolTipText();
		} else {
			cursor = Cursor.DEFAULT_CURSOR;
			toolTipText = GUISettings.EMPTY_TOOLTIP_TXT;
		}
	}

	/**
	 * Responds to a mouse exit event from the histogram display
	 */
	public void doGraphExited() {
		cursor = Cursor.DEFAULT_CURSOR;
	}

}
