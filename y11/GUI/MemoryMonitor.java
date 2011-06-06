package y11.GUI;

import javax.swing.JLabel;

/**
 * Manages the memory display. Adapted from
 * com.rapidminer.gui.tools.SystemMonitor
 * 
 * @author Caleb Sotelo
 * 
 */
public class MemoryMonitor {

	private long delay = 500;

	private static final String[] MEMORY_UNITS = { "b", "kB", "MB", "GB", "TB" };

	private long memory;

	private double currentlyUsed = 0;

	private JLabel view;

	public MemoryMonitor(JLabel view) {
		this.view = view;
	}

	public void startMonitorThread() {
		new Thread("PatternExplorer-MemoryMonitor-Thread") {

			{
				setDaemon(true);
			}

			public void run() {
				setPriority(MIN_PRIORITY);
				while (true) {
					currentlyUsed = Runtime.getRuntime().totalMemory()
							- Runtime.getRuntime().freeMemory();
					memory = (long) currentlyUsed;

					long maxmemory = Runtime.getRuntime().maxMemory();
					double pct = ((double) memory / (double) maxmemory) * 100;
					pct = ((double) ((int) (pct * 100))) / 100;

					String used = humanReadable(memory);
					String max = humanReadable(maxmemory);
					view.setText("Memory Usage:        " + used + " / " + max
							+ "        (" + pct + "%)");
					view.repaint();

					try {
						sleep(delay);
					} catch (InterruptedException e) {
					}
				}
			}
		}.start();
	}

	public String humanReadable(long bytes) {
		long result = bytes;
		long rest = 0;
		int unit = 0;
		while (result > 1024) {
			rest = result % 1024;
			result /= 1024;
			unit++;
			if (unit >= MEMORY_UNITS.length - 1)
				break;
		}
		if ((result < 10) && (unit > 0)) {
			return result + "." + (10 * rest / 1024) + " " + MEMORY_UNITS[unit];
		} else {
			return result + " " + MEMORY_UNITS[unit];
		}
	}
}
