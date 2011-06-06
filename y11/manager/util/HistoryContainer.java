package y11.manager.util;

import java.util.LinkedList;

import y11.manager.state.ViewState;

/**
 * A generic container for remembering and traversing states of history.
 * 
 * @author Caleb Sotelo
 * 
 * @param <T>
 *            Object representing snapshots of history.
 */
public class HistoryContainer<T> {

	private LinkedList<T> states; // underlying container

	private boolean canStepBack;
	private boolean canStepForward;
	private boolean canReset;
	private int currentIndex; // index of the current state

	/**
	 * Creates a new empty history container.
	 */
	public HistoryContainer() {
		states = new LinkedList<T>();
		canStepBack = false;
		canStepForward = false;
		canReset = false;

	}

	/**
	 * Adds the first element.
	 * 
	 * @param t
	 */
	public void addFirst(T t) {
		states.add(t);
	}

	/**
	 * Commits a new state to memory, and traverses to it.
	 * 
	 * @param t
	 *            The state to remember.
	 */
	public void add(T t) {
		// forget entries after insertion point
		while (states.indexOf(states.getLast()) > currentIndex) {
			states.removeLast();
		}
		states.add(t);
		currentIndex++;
		canStepForward = false;
		canStepBack = (currentIndex > 0);
		canReset = (currentIndex > 0);
	}

	/**
	 * Traverses backward, if possible.
	 */
	public boolean stepBack() {
		if (canStepBack) {
			currentIndex--;
			canStepForward = true;
			if (currentIndex == 0) {
				canStepBack = false;
			}
			return true;
		}
		return false;
	}

	/**
	 * Traverses forward, if possible.
	 */
	public boolean stepForward() {
		if (canStepForward) {
			currentIndex++;
			canStepBack = true;
			if (currentIndex == states.size() - 1) {
				canStepForward = false;
			}
			return true;
		}
		return false;
	}

	/**
	 * Resets the history, saving only the first state.
	 */
	public boolean reset() {
		if (canReset) {
			currentIndex = 0;
			T t = states.getFirst();
			states.clear();
			states.addFirst(t);
			canStepBack = false;
			canStepForward = false;
			canReset = false;
			ViewState.resetNumStates();
			return true;
		}
		return false;
	}

	/**
	 * Gets the current state.
	 * 
	 * @return the current state.
	 */
	public T getCurrent() {
		return states.get(currentIndex);
	}

	/**
	 * Tests if a backwards traversal is possible.
	 * 
	 * @return true if a step back is possible
	 */
	public boolean canStepBack() {
		return canStepBack;
	}

	/**
	 * Tests if a forward traversal is possible.
	 * 
	 * @return true if a step forward is possible
	 */
	public boolean canStepForward() {
		return canStepForward;
	}

	/**
	 * Tests if a reset is possible.
	 * 
	 * @return true if a reset is possible
	 */
	public boolean canReset() {
		return canReset;
	}

	/**
	 * Gets the size of this history container.
	 * 
	 * @return the size of this container
	 */
	public int size() {
		return states.size();
	}
}
