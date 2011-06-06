package y11.GUI.canvas;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * An empty panel with correct dimensions for the top left corner of the
 * histogram display.
 * 
 * @author Caleb Sotelo
 * 
 */
public class CornerPanel extends CanvasComponent {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void prepare(int x, int y, int w, int h) {
		border = new Rectangle(x, y, w, h);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void paint(Graphics g) { /* don't draw anything */
	}

}
