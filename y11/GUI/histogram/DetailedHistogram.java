package y11.GUI.histogram;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

import y11.GUI.GUISettings;
import y11.models.histogram.AttributeHistogramModel;
import y11.models.histogram.DefaultHistogramModel;
import y11.models.histogram.HistogramModel;
import y11.models.histogram.TargetHistogramModel;

/**
 * A histogram for the detailed graph that is displayed in the sidebar. Uses the
 * JFreeChart library.
 * 
 * @author Caleb Sotelo
 * 
 */
public class DetailedHistogram extends Histogram {

	private ChartPanel chartPanel;
	private JFreeChart chart;
	protected CategoryPlot plot;

	/**
	 * Creates a new DetailedHistogram from the specified model.
	 * 
	 * @param m
	 */
	public DetailedHistogram(HistogramModel m) {
		super(m);
		initChart();
	}

	/**
	 * Gets the ChartPanel (JFreeChart construct) maintained by this histogram.
	 * 
	 * @return
	 */
	public ChartPanel getChartPanel() {
		return chartPanel;
	}

	private void initChart() {
		chartPanel = new ChartPanel(ChartFactory.createBarChart("", "", "",
				getChartDataSet(), PlotOrientation.VERTICAL, false, false,
				false));
		chart = chartPanel.getChart();
		// chartPanel.setSize(200, chartPanel.getWidth());
		plot = chart.getCategoryPlot();

		// customization
		chart.setBackgroundPaint(new Color(
				GUISettings.HISTOGRAM_BACKGROUND_COLOR));

		CategoryItemRenderer renderer = plot.getRenderer();
		((BarRenderer) renderer).setBarPainter(new StandardBarPainter());
		((BarRenderer) renderer).setShadowVisible(false);
		((BarRenderer) renderer).setSeriesPaint(0, new Color(
				GUISettings.HISTOGRAM_DEFAULT_BAR_COLOR));
		plot.setRenderer(renderer);

		plot.setOutlinePaint(new Color(
				GUISettings.HISTOGRAM_DETAIL_BORDER_COLOR));
		plot.setOutlineStroke(new BasicStroke(
				GUISettings.HISTOGRAM_BORDER_WIDTH));
		plot.setBackgroundPaint(new Color(
				GUISettings.HISTOGRAM_BACKGROUND_COLOR));
		plot.setRangeGridlinesVisible(false);
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setAutoRangeIncludesZero(true);
		rangeAxis.setVisible(true);
		rangeAxis.setAxisLineVisible(false);

		CategoryAxis categoryAxis = (CategoryAxis) plot.getDomainAxis();
		categoryAxis.setVisible(true);
		categoryAxis.setAxisLineVisible(false);
		categoryAxis
				.setCategoryMargin(GUISettings.HISTOGRAM_DETAIL_CATEGORY_MARGIN);

		Dimension d = new Dimension(GUISettings.HISTOGRAM_DETAIL_WIDTH,
				GUISettings.HISTOGRAM_DETAIL_HEIGHT);
		// setMaximumSize(d);
		chartPanel.setPreferredSize(d);
		// setMinimumSize(d);

		if (model instanceof TargetHistogramModel) {
			// row header histogram
			// TargetHistogramModel thm = (TargetHistogramModel) model;
			chart.setTitle(model.getAttributeName());
		} else if (model instanceof AttributeHistogramModel) {
			// col header histogram
			chart.setTitle(model.getAttributeName());
			categoryAxis
					.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
		} else {
			// default histogram
			DefaultHistogramModel dhm = (DefaultHistogramModel) model;
			chart.setTitle(dhm.getTarget() + " : " + dhm.getValue());
			categoryAxis.setLabel(dhm.getPredictor());
			// TODO color significant bars
			categoryAxis
					.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
		}
		rangeAxis.setLabel("Examples");
	}

	private DefaultCategoryDataset getChartDataSet() {
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		int[] counts = model.getCounts();
		if (model instanceof TargetHistogramModel) {
			TargetHistogramModel thm = (TargetHistogramModel) model;

			for (int i = 0; i < counts.length; i++) {
				data.addValue(counts[i], "ROW_0", thm.getValueName());
			}
		} else {
			// atribute or default histogram model
			for (int i = 0; i < counts.length; i++) {
				data.addValue(counts[i], "ROW_0", model.getValueName(i));
			}
		}

		return data;
	}

}
