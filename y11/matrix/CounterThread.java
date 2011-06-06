package y11.matrix;

import y11.manager.state.MatrixInfo;
import y11.manager.state.ViewState;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;

/**
 * A thread dedicated to computing histogram models for a subset of columns.
 * 
 * @author Caleb Sotelo
 * 
 */
public class CounterThread extends Thread {

	private int index;
	private int delta;
	private ExampleSet exampleSet;
	private MatrixInfo matrixInfo;
	private Attribute target;
	private Matrix matrix;
	private Barrier barrier;

	/**
	 * Creates a new CounterThread with the specified settings.
	 * 
	 * @param index
	 *            the attribute index to start on
	 * @param delta
	 *            the number of attributes to skip
	 * @param view
	 * @param matrix
	 * @param barrier
	 */
	public CounterThread(int index, int delta, ViewState view, Matrix matrix,
			Barrier barrier) {
		this(index, delta, view, matrix);
		this.barrier = barrier;
	}

	/**
	 * Creates a new CounterThread with the specified settings and shared
	 * Barrier.
	 * 
	 * @param index
	 * @param delta
	 * @param view
	 * @param matrix
	 */
	public CounterThread(int index, int delta, ViewState view, Matrix matrix) {
		this.index = index;
		this.delta = delta;
		this.exampleSet = view.getExampleSet();
		this.matrixInfo = view.getMatrixInfo();
		this.target = matrixInfo.getAttribute(view.getTarget());
		this.matrix = matrix;
	}

	/**
	 * Starts this CounterThread.
	 */
	@Override
	public void run() {
		try {
			count();
			barrier.signal();
		} catch (UserCancelledException e) {
			// thread dies here ..
		}
	}

	/**
	 * Generates histogram models for a subset of attributes by counting through
	 * the ExampleSet
	 * 
	 * @throws UserCancelledException
	 */
	public void count() throws UserCancelledException {
		int numAttributes = matrixInfo.getNumAttributes(); // total # of columns
		// int tVals = matrixInfo.getNumValues(target); // total # of rows
		int tVals = target.getMapping().size();
		int[][] counts; // the counts for this column
		Attribute a; // the current attribute (columns) being processed
		int vals; // the attribute's number of values
		// this thread counts every delta'th attribute beginning at i
		for (int i = index; i < numAttributes; i += delta) {

			a = matrixInfo.getAttribute(i);
			vals = a.getMapping().getValues().size();

			if (a == target) {
				// special routine to count target distrn
				int[] tCounts = new int[vals];
				for (Example e : exampleSet) {
					tCounts[(int) e.getValue(a)]++;
				}
				matrix.addTargetCounts(tCounts);

			} else {
				// routine to count all regular attributes
				counts = new int[tVals][vals];
				// count through examples
				for (Example e : exampleSet) {
					counts[(int) e.getValue(target)][(int) e.getValue(a)]++;
				}
				matrix.addAttributeCounts(a, counts);
			}
			ProgressManager.makeProgress();
		}
	}
}
