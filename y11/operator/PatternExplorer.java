package y11.operator;

import y11.GUI.PatternExplorerGUI;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.gui.RapidMinerGUI;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.ResultObjectAdapter;

/**
 * PatternExplorer entry point. Sets limited GUI preferences for RapidMiner.
 * 
 * @author Caleb Sotelo
 */
public class PatternExplorer extends ResultObjectAdapter {

	private PatternExplorerGUI view;

	/**
	 * Creates a new PatternExplorer. Entry point for the operator.
	 * 
	 * @param exampleSet
	 */
	public PatternExplorer(ExampleSet exampleSet) {
		// this try-catch block is useful for debugging
		try {
			// turn off the logging viewer by default to save space
			if (RapidMinerGUI.getMainFrame().TOGGLE_LOGGING_VIEWER.isSelected()) {
				RapidMinerGUI.getMainFrame().TOGGLE_LOGGING_VIEWER.doClick();
			}
			view = new PatternExplorerGUI(exampleSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns a label that displays the {@link #toResultString()} result
	 * encoded as html.
	 * 
	 * @param container
	 *            - currently unused
	 * @return The visualization component of the operator, an extension of
	 *         JPanel
	 */
	public java.awt.Component getVisualizationComponent(IOContainer container) {
		return view;
	}

	public String getExtension() {
		return "no_file";
	}

	public String getFileDescription() {
		return "no_file";
	}

	public boolean isSavable() {
		return false;
	}
}