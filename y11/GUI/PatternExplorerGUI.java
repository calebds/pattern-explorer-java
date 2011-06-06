package y11.GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.ProgressMonitor;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import y11.GUI.canvas.MatrixRenderer;
import y11.logging.Log;
import y11.manager.ViewManager;
import y11.manager.Zoom;
import y11.manager.loader.DefaultViewLoader;
import y11.manager.loader.FastViewLoader;
import y11.manager.loader.LeanViewLoader;
import y11.matrix.ProgressManager;

import com.rapidminer.example.ExampleSet;

/**
 * The View for the PatternExplorer operator. Manages all user interactions and
 * display elements. Calls methods on the ViewManager (model). Settings can be
 * changed in class GUISettings.
 * 
 * @author Caleb Sotelo
 * 
 */
public class PatternExplorerGUI extends JPanel {

	// swing components
	private JButton backButton;
	private JScrollPane chartPanelScrollPane;
	private JPanel detailPanel;
	private JButton forwardButton;
	private JLabel historyLabel;
	private JSplitPane sideSplitPane;
	private JSplitPane mainSplitPane;
	private JLabel memStatsLabel;
	private JButton resetButton;
	private JPanel optionsPanel;
	private JLabel sigmaSliderLabel;
	private JLabel sigmaLevelLabel;
	private JSlider sigmaSlider;
	private JComboBox targetSelect;
	private JLabel targetSelectLabel;
	private JLabel zoomScaleLabel;
	private JSlider zoomSlider;
	private JLabel zoomSliderLabel;
	private JLabel toolTipLabel;
	private JRadioButton historyOptDefault;
	private JRadioButton historyOptTime;
	private JRadioButton historyOptMem;
	private ButtonGroup historyOptions;
	private JScrollPane detailScrollPane;

	// listeners
	private ActionListener targetSelectController;
	private ChangeListener sigmaSliderController;
	private ChangeListener zoomSliderController;
	private ActionListener backButtonController;
	private ActionListener forwardButtonController;
	private ActionListener resetButtonController;
	private ActionListener historyOptController;
	private MouseListener graphsClickController;
	private MouseMotionListener graphsMoveController;

	// members
	private ViewManager manager; // business logic
	private MatrixRenderer renderer; // component on which histograms are drawn
	private MemoryMonitor memMonitor;
	private ArrayList<Component> componentList;
	private ToolTipRegistry toolTips;
	private ProgressMonitor progressMonitor;

	/**
	 * Creates a new PatternExplorerGUI. Entry point for the entire
	 * PatternExplorer operator initialization.
	 * 
	 * @param exampleSet
	 *            The ExampleSet to initialize the view ot
	 */
	public PatternExplorerGUI(ExampleSet exampleSet) {
		renderer = new MatrixRenderer();
		initComponents();
		toolTips = new ToolTipRegistry(toolTipLabel);
		registerToolTips();
		manager = new ViewManager(exampleSet, renderer);
		manager.renderMatrix();
		refreshUI();
		System.gc(); // put out the trash
	}

	/**
	 * Creates and customizes graphical components and initializes the GUI.
	 */
	private void initComponents() {

		// create all components first
		backButton = new JButton();
		chartPanelScrollPane = new JScrollPane();
		detailPanel = new JPanel();
		forwardButton = new JButton();
		historyLabel = new JLabel();
		mainSplitPane = new JSplitPane();
		memStatsLabel = new JLabel();
		resetButton = new JButton();
		optionsPanel = new JPanel();
		sigmaSliderLabel = new JLabel();
		sigmaLevelLabel = new JLabel();
		sigmaSlider = new JSlider();
		targetSelect = new JComboBox();
		targetSelectLabel = new JLabel();
		zoomScaleLabel = new JLabel();
		zoomSlider = new JSlider();
		zoomSliderLabel = new JLabel();
		toolTipLabel = new JLabel();
		historyOptDefault = new JRadioButton();
		historyOptTime = new JRadioButton();
		historyOptMem = new JRadioButton();
		historyOptions = new ButtonGroup();
		sideSplitPane = new JSplitPane();
		detailScrollPane = new JScrollPane();

		// create element controllers
		targetSelectController = new TargetSelectController();
		sigmaSliderController = new SigmaSliderController();
		zoomSliderController = new ZoomSliderController();
		backButtonController = new BackButtonController();
		forwardButtonController = new ForwardButtonController();
		resetButtonController = new ResetButtonController();
		historyOptController = new HistoryOptController();
		graphsClickController = new GraphsClickController();
		graphsMoveController = new GraphsMoveController();

		// progress monitor
		progressMonitor = new ProgressMonitor(this,
				"Completing the requested operation.", "", 0,
				ProgressManager.COMPLETE);

		// renderer settings
		renderer.addMouseListener(graphsClickController);
		renderer.addMouseMotionListener(graphsMoveController);

		// history label settings
		historyOptDefault.setText(GUISettings.DFLT_MEM_BTN_TXT);
		historyOptTime.setText(GUISettings.ECON_TIME_BTN_TXT);
		historyOptMem.setText(GUISettings.ECON_MEM_BTN_TXT);
		historyOptions.add(historyOptDefault);
		historyOptions.add(historyOptTime);
		historyOptions.add(historyOptMem);

		// history radio buttons settings
		historyOptDefault.addActionListener(historyOptController);
		historyOptDefault.setActionCommand(DefaultViewLoader.NAME);
		historyOptTime.addActionListener(historyOptController);
		historyOptTime.setActionCommand(FastViewLoader.NAME);
		historyOptMem.addActionListener(historyOptController);
		historyOptMem.setActionCommand(LeanViewLoader.NAME);

		// main split pane settings
		mainSplitPane.setRightComponent(chartPanelScrollPane);
		mainSplitPane.setLeftComponent(sideSplitPane);
		mainSplitPane.setBorder(null);
		mainSplitPane.setEnabled(false);
		mainSplitPane.setDividerSize(GUISettings.MAIN_DIVIDER_WIDTH);
		mainSplitPane.setOneTouchExpandable(true);

		// side split pane settings
		sideSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		sideSplitPane.setTopComponent(optionsPanel);
		sideSplitPane.setBottomComponent(detailScrollPane);
		sideSplitPane.setEnabled(false);

		// history traversal button settings
		backButton.setText(GUISettings.HIST_BACK_BTN_TXT);
		backButton.setEnabled(false);
		backButton.addActionListener(backButtonController);
		forwardButton.setText(GUISettings.HIST_FRWD_BTN_TXT);
		forwardButton.setEnabled(false);
		forwardButton.addActionListener(forwardButtonController);
		historyLabel.setText(GUISettings.HIST_LABEL_TXT);
		resetButton.setText(GUISettings.HIST_RESET_BTN_TXT);
		resetButton.setEnabled(false);
		resetButton.addActionListener(resetButtonController);

		// zoom slider settings
		zoomScaleLabel.setText(GUISettings.ZOOM_DEFAULT_SCALE + "%");
		zoomSliderLabel.setText(GUISettings.ZOOM_LABEL_TXT);
		zoomSlider.setModel(new DefaultBoundedRangeModel(GUISettings.ZOOM_SLIDER_INIT,
				GUISettings.ZOOM_SLIDER_EXT, GUISettings.ZOOM_SLIDER_MIN, GUISettings.ZOOM_SLIDER_MAX));
		zoomSlider.setMinorTickSpacing(GUISettings.ZOOM_SLIDER_MINOR_TICK_SPACING);
		zoomSlider.setPaintTicks(true);
		zoomSlider.setPaintLabels(false);
		zoomSlider.setSnapToTicks(false);
		zoomSlider.addChangeListener(zoomSliderController);

		// statistical significance settings
		sigmaSliderLabel.setText(GUISettings.SIGMA_LABEL_TXT);
		sigmaSlider.setModel(new DefaultBoundedRangeModel(GUISettings.SIGMA_SLIDER_INIT,
				GUISettings.SIGMA_SLIDER_EXT, GUISettings.SIGMA_SLIDER_MIN, GUISettings.SIGMA_SLIDER_MAX));
		sigmaSlider.setMinorTickSpacing(GUISettings.SIGMA_SLIDER_MINOR_TICK_SPACING);
		sigmaSlider.setPaintTicks(true);
		sigmaSlider.setPaintLabels(false);
		sigmaSlider.setSnapToTicks(false);
		sigmaSlider.addChangeListener(sigmaSliderController);

		// charts scroll pane settings
		chartPanelScrollPane.setBorder(null);
		chartPanelScrollPane.setViewportView(renderer);
		chartPanelScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		chartPanelScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		chartPanelScrollPane.setColumnHeaderView(toolTipLabel);
		chartPanelScrollPane.getVerticalScrollBar().setUnitIncrement(
				GUISettings.V_SCROLL_AMOUNT);
		chartPanelScrollPane.getHorizontalScrollBar().setUnitIncrement(
				GUISettings.H_SCROLL_AMOUNT);

		// detail panel settings
		detailPanel.setBackground(new Color(GUISettings.DETAIL_PANEL_BG_COLOR));
		detailScrollPane.setViewportView(detailPanel);

		// target select settings
		targetSelectLabel.setText(GUISettings.TARGET_LABEL_TXT);
		targetSelect.addActionListener(targetSelectController);

		// LAYOUT

		// main layout
		setLayout(new GridLayout(1, 1));
		add(mainSplitPane);
		optionsPanel.setLayout(new GridBagLayout());

		// side panel gridbag constraints
		GridBagConstraints c;
		Insets topInsets = new Insets(GUISettings.SIDE_PANEL_PADDING,
				GUISettings.SIDE_PANEL_PADDING, 0,
				GUISettings.SIDE_PANEL_PADDING);
		Insets lineInsets = new Insets(GUISettings.SIDE_PANEL_LINE_SPACING,
				GUISettings.SIDE_PANEL_PADDING, 0,
				GUISettings.SIDE_PANEL_PADDING);
		Insets areaInsets = new Insets(GUISettings.SIDE_PANEL_AREA_SPACING,
				GUISettings.SIDE_PANEL_PADDING, 0,
				GUISettings.SIDE_PANEL_PADDING);
		Insets bottomInsets = new Insets(GUISettings.SIDE_PANEL_AREA_SPACING,
				GUISettings.SIDE_PANEL_PADDING,
				GUISettings.SIDE_PANEL_AREA_SPACING,
				GUISettings.SIDE_PANEL_PADDING);

		// history label
		c = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE,
				topInsets, 0, 0);
		optionsPanel.add(historyLabel, c);

		JPanel browseBtns = new JPanel();
		browseBtns.add(backButton);
		browseBtns.add(resetButton);
		browseBtns.add(forwardButton);

		c = new GridBagConstraints(2, 0, 2, 1, 0.0, 0.0,
				GridBagConstraints.FIRST_LINE_END, GridBagConstraints.NONE,
				lineInsets, 0, 0);
		optionsPanel.add(browseBtns, c);
		c = new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.LINE_START, GridBagConstraints.NONE,
				lineInsets, 0, 0);
		optionsPanel.add(historyOptDefault, c);
		c = new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, lineInsets,
				0, 0);
		optionsPanel.add(historyOptTime, c);
		c = new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.LINE_END, GridBagConstraints.NONE,
				lineInsets, 0, 0);
		optionsPanel.add(historyOptMem, c);
		c = new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0,
				GridBagConstraints.LINE_START, GridBagConstraints.NONE,
				areaInsets, 0, 0);
		optionsPanel.add(targetSelectLabel, c);
		c = new GridBagConstraints(0, 4, 4, 1, 0.0, 0.0,
				GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL,
				lineInsets, 0, 0);
		optionsPanel.add(targetSelect, c);
		c = new GridBagConstraints(0, 5, 2, 1, 0.0, 0.0,
				GridBagConstraints.LINE_START, GridBagConstraints.NONE,
				areaInsets, 0, 0);
		optionsPanel.add(sigmaSliderLabel, c);
		c = new GridBagConstraints(2, 5, 2, 1, 0.0, 0.0,
				GridBagConstraints.LINE_END, GridBagConstraints.NONE,
				areaInsets, 0, 0);
		optionsPanel.add(sigmaLevelLabel, c);
		c = new GridBagConstraints(0, 6, 4, 1, 0.0, 0.0,
				GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL,
				lineInsets, 0, 0);
		optionsPanel.add(sigmaSlider, c);
		c = new GridBagConstraints(0, 7, 2, 1, 0.0, 0.0,
				GridBagConstraints.LINE_START, GridBagConstraints.NONE,
				areaInsets, 0, 0);
		optionsPanel.add(zoomSliderLabel, c);
		c = new GridBagConstraints(2, 7, 2, 1, 0.0, 0.0,
				GridBagConstraints.LINE_END, GridBagConstraints.NONE,
				areaInsets, 0, 0);
		optionsPanel.add(zoomScaleLabel, c);
		c = new GridBagConstraints(0, 8, 4, 1, 0.0, 0.0,
				GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL,
				lineInsets, 0, 0);
		optionsPanel.add(zoomSlider, c);
		c = new GridBagConstraints(0, 9, 4, 1, 0.0, 0.0,
				GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL,
				bottomInsets, 0, 0);
		optionsPanel.add(memStatsLabel, c);

		// build component list
		componentList = getAllComponents(this);

		// init memory monitor
		memMonitor = new MemoryMonitor(memStatsLabel);
		memMonitor.startMonitorThread();
	}

	private void registerToolTips() {
		toolTips.register(resetButton, GUISettings.RESET_BTN_TIP);
		toolTips.register(backButton, GUISettings.BACK_BTN_TIP);
		toolTips.register(forwardButton, GUISettings.FORWARD_BTN_TIP);
		toolTips.register(sigmaSlider, GUISettings.SIGMA_SLIDER_TIP);
		toolTips.register(targetSelect, GUISettings.TARGET_SELECT_TIP);
		toolTips.register(zoomSlider, GUISettings.ZOOM_SLIDER_TIP);
		toolTips.register(historyOptDefault, GUISettings.HIST_DFLT_TIP);
		toolTips.register(historyOptTime, GUISettings.HIST_TIME_TIP);
		toolTips.register(historyOptMem, GUISettings.HIST_MEM_TIP);
	}

	/**
	 * Code adapted from
	 * http://www.java2s.com/Code/Java/Swing-JFC/GetAllComponentsinacontainer.
	 * htm
	 * 
	 * @param c
	 * @return
	 */
	private ArrayList<Component> getAllComponents(final Container c) {
		Component[] comps = c.getComponents();
		ArrayList<Component> compList = new ArrayList<Component>();
		for (Component comp : comps) {
			compList.add(comp);
			if (comp instanceof Container) {
				compList.addAll(getAllComponents((Container) comp));
			}
		}
		return compList;
	}

	private void refreshUI() {
		refreshHistoryOptions();
		refreshHistoryButtons();
		refreshTargets();
		refreshSigma();
		refreshZoom();
		refreshDetailPanel();
		refreshCursor();
		refreshTooltip();
	}

	private void setWaiting() {
		disableGUI();
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

	}

	private void setActive() {
		enableGUI();
	}

	private void disableGUI() {
		for (Component c : componentList) {
			c.setEnabled(false);
		}
		toolTips.setEnabled(false);
		renderer.removeMouseMotionListener(graphsMoveController);
		renderer.removeMouseListener(graphsClickController);

	}

	private void enableGUI() {
		for (Component c : componentList) {
			c.setEnabled(true);
		}
		toolTips.setEnabled(true);
		renderer.addMouseMotionListener(graphsMoveController);
		renderer.addMouseListener(graphsClickController);

	}

	private void refreshTooltip() {
		toolTipLabel.setText("  " + manager.getToolTipText());

	}

	private void refreshCursor() {
		setCursor(Cursor.getPredefinedCursor(manager.getCursor()));
	}

	private void refreshDetailPanel() {
		detailPanel.removeAll();
		JPanel detailedGraph = manager.getDetailedGraph();
		if (detailedGraph != null) {
			detailPanel.add(detailedGraph);
			detailedGraph.revalidate();
		}
	}

	private void refreshHistoryOptions() {
		historyOptDefault.setSelected(manager.isDefaultOptEnabled());
		historyOptMem.setSelected(manager.isMemOptEnabled());
		historyOptTime.setSelected(manager.isTimeOptEnabled());		
	}
	
	private void refreshHistoryButtons() {
		backButton.setEnabled(manager.canStepBack());
		forwardButton.setEnabled(manager.canStepForward());
		resetButton.setEnabled(manager.canReset());
	}

	private void refreshTargets() {
		Object[] targets = manager.getTargetList();
		Object selectedTarget = manager.getSelectedTarget();
		DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel(targets);
		targetSelect.removeActionListener(targetSelectController);
		targetSelect.setModel(comboBoxModel);
		targetSelect.setSelectedItem(selectedTarget);
		targetSelect.addActionListener(targetSelectController);
	}

	private void refreshSigma() {
		int sliderVal = (int) manager.getCurrentSigmaSliderValue();
		sigmaSlider.removeChangeListener(sigmaSliderController);
		sigmaSlider.setValue(sliderVal);
		sigmaSlider.addChangeListener(sigmaSliderController);
		refreshSigmaLabel();
	}

	private void refreshSigmaLabel() {
		double sigma = manager.convertSigmaSliderToThreshold(sigmaSlider
				.getValue());
		sigmaLevelLabel.setText(sigma + GUISettings.SIGMA_CHAR);
	}

	private void refreshZoom() {
		int sliderVal = (int) manager.getCurrentZoomSliderValue();
		zoomSlider.removeChangeListener(zoomSliderController);
		zoomSlider.setValue(sliderVal);
		zoomSlider.addChangeListener(zoomSliderController);
		refreshZoomLabel();
	}

	private void refreshZoomLabel() {
		double scale = manager.convertZoomSliderToScale(zoomSlider.getValue());
		zoomScaleLabel.setText((int) scale + "%");
	}

	// event listeners

	/**
	 * Controller for the history 'back' button
	 * 
	 * @author Caleb Sotelo
	 * 
	 */
	class BackButtonController implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			new ProgressListener().start();
			new Thread(new Runnable() {
				public void run() {
					manager.doStepBack();
				}
			}).start();
		}
	}

	/**
	 * Controller for the history 'reset' button
	 * 
	 * @author Caleb Sotelo
	 * 
	 */
	class ResetButtonController implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			new ProgressListener().start();
			new Thread(new Runnable() {
				public void run() {
					manager.doReset();
				}
			}).start();
		}
	}

	/**
	 * Controller for the history 'forward' button
	 * 
	 * @author Caleb Sotelo
	 * 
	 */
	class ForwardButtonController implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			new ProgressListener().start();
			new Thread(new Runnable() {
				public void run() {
					manager.doStepForward();
				}
			}).start();
		}
	}

	/**
	 * Controller for the zoom slider
	 * 
	 * @author Caleb Sotelo
	 * 
	 */
	class ZoomSliderController implements ChangeListener {

		public void stateChanged(ChangeEvent e) {
			if (zoomSlider.getValueIsAdjusting()) {
				manager.doChangeZoom(zoomSlider.getValue());
				refreshZoomLabel();
			} else {
				if (zoomSlider.getValue() != manager
						.getCurrentZoomSliderValue()) {
					manager.doChangeAndSetZoom(zoomSlider.getValue());
					refreshUI();
				}
			}
		}
	}

	/**
	 * Controller for statistical significance threshold slider
	 * 
	 * @author Caleb Sotelo
	 * 
	 */
	class SigmaSliderController implements ChangeListener {

		public void stateChanged(ChangeEvent e) {
			if (sigmaSlider.getValueIsAdjusting()) {
				manager.doChangeSigma(sigmaSlider.getValue());
				refreshSigmaLabel();
			} else {
				if (sigmaSlider.getValue() != manager
						.getCurrentSigmaSliderValue()) {
					manager.doChangeAndSetSigma(sigmaSlider.getValue());
					refreshUI();
				}
			}
		}
	}

	/**
	 * Controller for the target selection drop-down bar
	 * 
	 * @author Caleb Sotelo
	 * 
	 */
	class TargetSelectController implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			final String selectedTarget = targetSelect.getSelectedItem()
					.toString();
			if (!selectedTarget.equals(manager.getSelectedTarget())) {
				new ProgressListener().start();
				new Thread(new Runnable() {
					public void run() {
						manager.doChangeTarget(selectedTarget);
					}
				}).start();				
			}
		}
	}

	/**
	 * Controller for history option buttons
	 * 
	 * @author Caleb Sotelo
	 * 
	 */
	class HistoryOptController implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String action = ((JRadioButton) e.getSource()).getActionCommand();
			if (!action.equals(manager.getSelectedHistOpt())) {
				manager.doChangeHistOpt(action);
			}
		}
	}

	/**
	 * Controller for histogram panel mouse actions
	 * 
	 * @author Caleb Sotelo
	 * 
	 */
	class GraphsClickController implements MouseListener {

		public void mouseClicked(final MouseEvent e) {
			new ProgressListener().start();
			new Thread(new Runnable() {
				public void run() {
					manager.doGraphClicked(e.getPoint());
				}
			}).start();
		}

		public void mouseEntered(MouseEvent e) {
			// TODO
		}

		public void mouseExited(MouseEvent e) {
			manager.doGraphExited();
			refreshCursor();
		}

		public void mousePressed(MouseEvent e) {
			// TODO
		}

		public void mouseReleased(MouseEvent e) {
			// TODO
		}

	}

	/**
	 * Controller for histogram panel mouse movement
	 * 
	 * @author Caleb Sotelo
	 * 
	 */
	class GraphsMoveController implements MouseMotionListener {

		public void mouseMoved(MouseEvent e) {
			manager.doGraphMouseMoved(e.getPoint());
			refreshCursor();
			toolTipLabel.setText("  " + manager.getToolTipText());
		}

		public void mouseDragged(MouseEvent e) {
			// TODO
		}

	}

	/**
	 * Dedicated thread for listening to long-running tasks. Manages a progress
	 * bar.
	 * 
	 * @author Caleb Sotelo
	 * 
	 */
	class ProgressListener extends Thread {

		public static final int INITIAL_WAIT = 100;
		public static final int SLEEP_TIME = 50;

		public ProgressListener() {
			ProgressManager.resetProgress();
			setPriority(Thread.MAX_PRIORITY);
		}

		public void run() {
			int pctComplete;
			try {
				Thread.sleep(INITIAL_WAIT);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!ProgressManager.isComplete() && !progressMonitor.isCanceled()) {
				setWaiting();
				while (!ProgressManager.isComplete()
						&& !progressMonitor.isCanceled()) {
					pctComplete = ProgressManager.getProgress();
					progressMonitor.setProgress(pctComplete);
					progressMonitor.setNote(pctComplete + "% complete");
					try {
						Thread.sleep(SLEEP_TIME);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (progressMonitor.isCanceled()) {
					ProgressManager.cancelOperation();
				}
				progressMonitor.close();
				setActive();
			}
			refreshUI();

		}
	}
}