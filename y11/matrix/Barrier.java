package y11.matrix;

/**
 * A simple barrier object that signals when registered threads have completed.
 * 
 * @author Caleb Sotelo
 * 
 */
public class Barrier {

	int quantity;
	int total;

	/**
	 * Creates a new Barrier object that manages t number of threads.
	 * 
	 * @param t
	 */
	public Barrier(int t) {
		this.total = t;
	}

	/**
	 * Thread-safe method for a thread to signal when it has completed.
	 */
	public synchronized void signal() {
		quantity++;
	}

	/**
	 * Tests if all the threads watched by this Barrier object have completed
	 * 
	 * @return tru if all threads have completed
	 */
	public boolean isDone() {
		return quantity == total;
	}
}
