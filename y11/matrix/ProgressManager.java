package y11.matrix;

import y11.logging.Log;

/**
 * Manages the state of a long-running operation.
 * 
 * @author Caleb Sotelo
 * 
 */
public class ProgressManager {

	/**
	 * The measure of oepration completeness.
	 */
	public static final int COMPLETE = 100;

	private static int totalWork;
	private static volatile int progress;
	private static boolean canceled = false;
	private static boolean didNotRun = false;

	/**
	 * Resets the progress for a new operation
	 */
	public static void resetProgress() {
		totalWork = progress = 0;
		canceled = false;
		didNotRun = false;
	}

	/**
	 * Sets the total amount of work for the ensuing operation. Used by the
	 * progress bar.
	 * 
	 * @param numCols
	 *            the total work
	 */
	public static void setTotalWork(int numCols) {
		totalWork = numCols;
	}

	/**
	 * Increases the progress by an increment when a CounterThread is done
	 * processing a column.
	 * 
	 * @throws UserCancelledException
	 *             if the user cancelled the operation
	 */
	public static synchronized void makeProgress()
			throws UserCancelledException {
		if (isCanceled()) {
			throw new UserCancelledException("User cancelled operation!");
		}
		progress++;
	}

	/**
	 * Returns the current progress. Used by the progress bar.
	 * 
	 * @return the current progress
	 */
	public static int getProgress() {
		return (int) (((double) progress / totalWork) * COMPLETE);
	}

	/**
	 * Tests if the operation is complete.
	 * 
	 * @return true if the operation has completed
	 */
	public static boolean isComplete() {
		return ((progress != 0) && (progress == totalWork)) || didNotRun;
	}

	/**
	 * Cancels the currently running operation.
	 */
	public static void cancelOperation() {
		canceled = true;
		Log.out(".. user cancelled the operation");
	}

	/**
	 * Thread-safe method for testing if an operation has been canceled.
	 * 
	 * @return true if the operation has been canceled.
	 */
	public static synchronized boolean isCanceled() {
		return canceled;
	}

	/**
	 * Allows a client operation to notify the progress bar thread that the
	 * operation is not a long-running one.
	 */
	public static void setDidNotRun() {
		didNotRun = true;
	}
}
