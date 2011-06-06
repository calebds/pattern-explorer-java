package y11.logging;

import y11.operator.PatternExplorerSettings;

import com.rapidminer.tools.LogService;

/**
 * Debug utility for the PatternExplorer operator. Outputs messages to
 * System.out and/or the Rapidminer logging console. Messages can be specified
 * with 3 levels of importance.
 * 
 * @author Caleb Sotelo
 * 
 */
public class Log {

	/**
	 * Informational level importance
	 */
	public static final int INFO_LEVEL = 2;

	/**
	 * Warning level importance
	 */
	public static final int WARNING_LEVEL = 5;

	/**
	 * Error level importance
	 */
	public static final int ERROR_LEVEL = 6;

	// string settings
	private static final String INFO_STR = "";
	private static final String WARNING_STR = "WARNING:  ";
	private static final String ERROR_STR = "ERROR: ";
	private static final String OUT_PREFIX = "[ ";
	private static final String OUT_SUFFIX = " ]";

	/**
	 * If logging is enabled, prints a formatted string to System.out and/or the
	 * Rapidminer logging console.
	 * 
	 * @param message
	 *            The message to be printed
	 * @param level
	 *            The importance level, can be INFO_LEVEL, WARNING_LEVEL, or
	 *            ERROR_LEVEL
	 */
	public static void out(String message, int level) {

		if ((PatternExplorerSettings.RAPIDMINER_LOGGING_ON || PatternExplorerSettings.SYSTEM_OUT_LOGGING_ON) == false)
			return;

		// set importance
		String importance = "";
		if (level == WARNING_LEVEL) {
			importance = WARNING_STR;
		} else if (level == ERROR_LEVEL) {
			importance = ERROR_STR;
		} else {
			importance = INFO_STR;
		}

		String out = OUT_PREFIX + importance + message + OUT_SUFFIX;

		if (PatternExplorerSettings.RAPIDMINER_LOGGING_ON) {
			LogService.getGlobal().log(out, level);
		}
		if (PatternExplorerSettings.SYSTEM_OUT_LOGGING_ON) {
			System.out.println(out);
		}

	}

	public static void dbg(String string) {
		if (PatternExplorerSettings.DEBUG_OUTPUT_ON) {
			System.err.println(string);
		}

	}

	/**
	 * Convenience method for logging with INFO_LEVEL importance
	 * 
	 * @param message
	 *            The message to be printed
	 */
	public static void out(String message) {
		out(message, INFO_LEVEL);
	}

}
