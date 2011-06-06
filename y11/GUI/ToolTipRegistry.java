package y11.GUI;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.JLabel;

/**
 * Manages tool tip functionality by mapping components to tool tip strings and
 * listening for mouse hover events on these components.
 * 
 * @author Caleb Sotelo
 * 
 */
public class ToolTipRegistry {

	private static final String TOOLTIP_PADDING = "  ";
	private HashMap<Component, String> toolTips;
	private MouseListener hoverListener;
	private JLabel tipLabel;
	private boolean enabled;

	/**
	 * Creates a new ToolTipRegistry
	 * 
	 * @param tipLabel
	 *            The JLabel on which the tooltip should be displayed
	 */
	public ToolTipRegistry(JLabel tipLabel) {
		this.tipLabel = tipLabel;
		toolTips = new HashMap<Component, String>();
		hoverListener = new HoverListener();
		enabled = true;
		setText(GUISettings.EMPTY_TOOLTIP_TXT);
	}

	/**
	 * Allows a component to register itself with a tooltip
	 * 
	 * @param c
	 *            The component to be registered
	 * @param toolTip
	 *            The tooltip string for the specified component
	 */
	public void register(Component c, String toolTip) {
		c.addMouseListener(hoverListener);
		toolTips.put(c, toolTip);
	}

	/**
	 * Enables/disables tooltips displaying
	 * 
	 * @param enabled
	 *            True for enable, false for disable
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	private void setText(String text) {
		tipLabel.setText(TOOLTIP_PADDING + text);
	}

	/**
	 * Controller for mouse hover events on registered components
	 * 
	 * @author Caleb Sotelo
	 * 
	 */
	private class HoverListener implements MouseListener {

		public void mouseEntered(MouseEvent e) {
			if (enabled) {
				setText(toolTips.get(e.getSource()));
			}
		}

		public void mouseExited(MouseEvent e) {
			if (enabled) {
				setText(GUISettings.EMPTY_TOOLTIP_TXT);
			}

		}

		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

}
