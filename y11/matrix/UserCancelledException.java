package y11.matrix;

/**
 * An exception thrown if the user cancels a long-running operation.
 * 
 * @author Caleb Sotelo
 * 
 */
public class UserCancelledException extends Exception {

	/**
	 * Creates a new UserCancelledException with the specified error message
	 * 
	 * @param err
	 *            the error message
	 */
	public UserCancelledException(String err) {
		super(err);
	}

}
