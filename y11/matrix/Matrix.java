package y11.matrix;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.set.MappedExampleSet;

import y11.GUI.canvas.CornerPanel;
import y11.GUI.canvas.CanvasComponent;
import y11.GUI.histogram.AttributeHistogram;
import y11.GUI.histogram.DefaultHistogram;
import y11.GUI.histogram.TargetHistogram;
import y11.logging.Log;
import y11.manager.state.MatrixInfo;
import y11.manager.state.ViewState;
import y11.models.AttributeValuePair;
import y11.models.histogram.AttributeHistogramModel;
import y11.models.histogram.DefaultHistogramModel;
import y11.models.histogram.HistogramModel;
import y11.models.histogram.TargetHistogramModel;
import y11.operator.PatternExplorerOperator;

/**
 * Contains histograms with their backing models. Spawns CounterThreads for
 * counting.
 * 
 * @author Caleb Sotelo
 * 
 */
public class Matrix {

	private static final int SLEEP_TIME = 20;
	private static final int TARGET_COL = 0;
	private static final int ATTRIB_ROW = 0;
	private static final int ATTRIB_OFFSET = 1;

	private HistogramModel[][] models;
	private CanvasComponent[][] graphs;
	private ViewState view;
	private Attribute target;
	private MatrixInfo info;

	private int length;
	private int height;

	/**
	 * Creates a new Matrix from the specified ViewState.
	 * 
	 * @param view
	 */
	public Matrix(ViewState view) {
		this.view = view;
		this.info = view.getMatrixInfo();
		this.target = info.getAttribute(view.getTarget());
		this.length = info.getNumAttributes();
		this.height = info.getNumValuesTarget(target) + 1;
		models = new HistogramModel[length][height];
	}

	/**
	 * Copy constructor. Creates a new Matrix from the specified Matrix.
	 * 
	 * @param copy
	 */
	public Matrix(Matrix copy) {
		this.view = copy.view;
		this.info = copy.info;
		this.target = copy.target;
		this.length = copy.length;
		this.height = copy.height;

		this.models = new HistogramModel[length][height];
		for (int i = 0; i < models.length; i++) {
			for (int j = 0; j < models[0].length; j++) {
				models[i][j] = copy.models[i][j];
			}
		}

	}

	/**
	 * Create and adds the target value models to this Matrix. Called by
	 * CounterThread.
	 * 
	 * @param tCountsLong
	 *            the target counts
	 * 
	 */
	public void addTargetCounts(int[] tCountsLong) {
		String value;
		int maxCount = 0;
		int[] tCounts = info.compact(target, tCountsLong);
		for (int i = 0; i < tCounts.length; i++) {
			int[] count = { tCounts[i] };
			value = info.getValue(target, i);
			// value = target.getMapping().getValues().get(i);
			TargetHistogramModel tModel = new TargetHistogramModel(count,
					target, value, info);
			models[TARGET_COL][i + ATTRIB_OFFSET] = tModel;
			maxCount = Math.max(maxCount, tModel.getCount(0));
		}
		for (int i = ATTRIB_OFFSET; i < models[TARGET_COL].length; i++) {
			models[TARGET_COL][i].setMaxColCount(maxCount);
		}
	}

	/**
	 * Create and adds the default models, then calculates and adds the
	 * attribute model. Called by CounterThread.
	 * 
	 * @param a
	 * @param countsLong
	 */
	public void addAttributeCounts(Attribute a, int[][] countsLong) {
		int[][] counts = info.compact(target, a, countsLong);
		int[] aCounts = new int[counts[0].length];
		int index = info.getCol(a, target) + 1;
		String value;
		int maxColCount = 0;
		for (int i = 0; i < counts.length; i++) {
			// ... summing attribute counts
			for (int j = 0; j < counts[i].length; j++) {
				aCounts[j] += counts[i][j];
			}
			// create default model
			// value = target.getMapping().getValues().get(i);
			value = info.getValue(target, i);
			DefaultHistogramModel dModel = new DefaultHistogramModel(counts[i],
					a, target, value, info);
			models[index][i + ATTRIB_OFFSET] = dModel;
			maxColCount = Math.max(dModel.getMaxCount(), maxColCount);
		}

		AttributeHistogramModel aModel = new AttributeHistogramModel(aCounts,
				a, info);
		models[index][ATTRIB_ROW] = aModel;

		// setting max column count
		for (int i = ATTRIB_OFFSET; i < models[index].length; i++) {
			models[index][i].setMaxColCount(maxColCount);
			if (info.isExpandable(a) && !info.isExpanded(a)) {
				int[] visibleIndices = ((AttributeHistogramModel) models[index][ATTRIB_ROW])
						.getVisibleIndices();
				((DefaultHistogramModel) models[index][i])
						.modifyThisCompact(visibleIndices);
			}
			((DefaultHistogramModel) models[index][i]).setIsSignificant(aModel);
		}

	}

	/**
	 * Use to print a Matrix to System.out
	 */
	private void dump() {
		for (int i = 0; i < models[0].length; i++) {
			for (int j = 0; j < models.length; j++) {
				if (i == 0 && j == 0) {
					System.out.print("\t");
					continue;
				}
				models[j][i].print();
			}
			System.out.println();
		}
	}

	/**
	 * Multi-thread factory method for creating new Matrix. Recounts and
	 * regenerates histogram models.
	 * 
	 * @param view
	 *            the ViewState to create a Matrix from
	 * @return the Matrix
	 * @throws UserCancelledException
	 */
	public static Matrix createMatrix(ViewState view)
			throws UserCancelledException {
		Matrix matrix = new Matrix(view);

		// allocate threads
		int numCols = view.getMatrixInfo().getNumAttributes();
		int numThreads = Math.min(numCols, PatternExplorerOperator
				.getParamCores());
		// int numThreads = 1;
		long begin;
		if (numThreads > 1) {

			Barrier barrier = new Barrier(numThreads);

			// construct threads
			CounterThread[] threads = new CounterThread[numThreads];
			for (int i = 0; i < threads.length; i++) {
				threads[i] = new CounterThread(i, numThreads, view, matrix,
						barrier);
			}
			
			ProgressManager.setTotalWork(numCols);
			begin = System.nanoTime();//currentTimeMillis();

			// start threads
			for (int i = 0; i < threads.length; i++) {
				threads[i].start();
			}

			while (!barrier.isDone()) {
				try {
					Thread.sleep(SLEEP_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else {
			// do serial algorithm
			begin = System.nanoTime();//currentTimeMillis();
			ProgressManager.setTotalWork(numCols);
			CounterThread serialThread = new CounterThread(0, numThreads, view,
					matrix);
			serialThread.count();
		}
		//long time = System.currentTimeMillis() - begin;
		double time = (System.nanoTime() - begin) / 1000000.0;
		Log.dbg("[ --- time: " + time + " ms --- ]");
		return matrix.createHistograms();
	}

	/**
	 * Used to create a new Matrix when recounting is not necessary, e.g. an
	 * expand/contract operation.
	 * 
	 * @param m
	 * @param info
	 * @param view
	 * @param attribute
	 * @return the Matrix
	 */
	public static Matrix createMatrixII(Matrix m, MatrixInfo info,
			ViewState view, String attribute) {
		Matrix matrix = new Matrix(m);
		matrix.info = info;
		matrix.view = view;
		return matrix.modifyExpand(attribute);
	}

	private Matrix modifyExpand(String attribute) {
		int index = info.getCol(attribute, target) + 1;
		models[index][ATTRIB_ROW] = ((AttributeHistogramModel) models[index][ATTRIB_ROW])
				.modifyExpand(info);
		AttributeHistogramModel aModel = ((AttributeHistogramModel) models[index][ATTRIB_ROW]);
		int[] visibleIndices = aModel.getVisibleIndices();
		for (int i = ATTRIB_OFFSET; i < models[index].length; i++) {
			models[index][i] = ((DefaultHistogramModel) models[index][i])
					.modifyExpand(visibleIndices, info);
			((DefaultHistogramModel) models[index][i]).setIsSignificant(aModel);
		}
		return createHistograms();
	}

	/**
	 * Generates histograms from the matrixInfo and exampleSet
	 * 
	 * @return
	 */
	private Matrix createHistograms() {
		graphs = new CanvasComponent[length][height];
		for (int c = 0; c < length; c++) {
			for (int r = 0; r < height; r++) {
				if (c == 0 && r == 0) {
					// top-left
					graphs[c][r] = new CornerPanel();
				} else if (c == 0) {
					// left col
					graphs[c][r] = new TargetHistogram(models[c][r]);
				} else if (r == 0) {
					// top row
					graphs[c][r] = new AttributeHistogram(models[c][r]);
				} else {
					// body
					graphs[c][r] = new DefaultHistogram(models[c][r]);
				}
			}
		}
		return this;
	}

	/**
	 * Generates an example set with fewer rows that the argument. Used for
	 * drill down operations. Creates a mask and applies to the current
	 * ExampleSet.
	 * 
	 * @param exampleSet
	 * @param avp
	 * @param removeSingle
	 * @return
	 */
	public static ExampleSet createDrilledDownExampleSet(ExampleSet exampleSet,
			AttributeValuePair avp, boolean removeSingle) {

		String feature = avp.getAttribute();
		String value = avp.getValue();
		int count = avp.getCount();
		Attribute clickedFeature = exampleSet.getAttributes().get(feature);
		int mapSize = removeSingle ? exampleSet.size() - count : count;
		int[] mapping = new int[mapSize];

		int map_i = 0;
		int map_e = 0;
		for (Example e : exampleSet) {
			boolean match = e.getValueAsString(clickedFeature).equals(value);
			if (removeSingle) {
				if (!match) {
					mapping[map_i++] = map_e;
				}
			} else {
				if (match) {
					mapping[map_i++] = map_e;
				}
			}
			map_e++;
		}

		return new MappedExampleSet(exampleSet, mapping);
	}

	/**
	 * Gets the matrix of canvas components.
	 * 
	 * @return the CanvasComponents
	 */
	public CanvasComponent[][] getGraphs() {
		return graphs;
	}

	/**
	 * Gets the number of histogram bars in the i'th column of this matrix.
	 * 
	 * @param i
	 * @return
	 */
	public int getColSize(int i) {
		Attribute a = info.getAttribute(i, target);
		return info.getNumValues(a);
	}

	/**
	 * Gets the generated text for the histogram display title.
	 * 
	 * @return
	 */
	public String getTitleText() {
		int numExamples = view.getExampleSet().size();
		int numFeatures = view.getMatrixInfo().getNumAttributes() - 1;
		int numValues = view.getMatrixInfo().getTotalValues();
		String s = "";
		String target = view.getTarget();
		s += "Target: \"" + target;
		s += "\" (" + view.getMatrixInfo().getNumValuesTarget(target)
				+ " values)  //  ";
		s += numFeatures + " predictors  //  ";
		s += numExamples + " examples  //  ";
		s += numValues + " total values";
		
		return s;
	}

}