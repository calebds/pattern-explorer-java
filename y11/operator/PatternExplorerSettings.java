package y11.operator;

import y11.manager.loader.DefaultViewLoader;
import y11.manager.loader.LeanViewLoader;

/**
 * Non GUI-related settings for tweaking. These can easily become operator
 * parameters.
 * 
 * @author Caleb Sotelo
 * 
 */
public class PatternExplorerSettings {

	/*
	 * Logging preferences.
	 */
	public static final boolean RAPIDMINER_LOGGING_ON = true;
	public static final boolean SYSTEM_OUT_LOGGING_ON = true;
	public static final boolean DEBUG_OUTPUT_ON = true;

	/*
	 * Minimum amounts of memory to reach before switching to a
	 * memory-conserving strategy.
	 */
	public static final double MIN_FREE_LOADER_BYTES = 10485760; // 10 free mb

	/*
	 * Preprocessing settings.
	 */
	public static final int NUM_COMPACT_VALUES = 6;
	public static final int DEFAULT_NUMERIC_BINS = 6;
	public static final String MISSING_VALUE_NAME = "MISSING";

	/*
	 * Hitsory loader initial settings
	 */
	public static final Object START_LOADER = LeanViewLoader.NAME;

}
