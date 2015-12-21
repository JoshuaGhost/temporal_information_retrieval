package view;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.junit.Test;

public class LineCharts extends ApplicationFrame {

	DefaultCategoryDataset lineDataset = null;
	ChartPanel chartPanel = null;
	
	public void insertData(double num, String query, String Date) {
		this.lineDataset.addValue(num, query, Date);
	}

	private static JFreeChart createChart(DefaultCategoryDataset lineDataset) {
		//定义图表对象
		JFreeChart chart = ChartFactory.createLineChart("Temporary Trend", // chart title
				"time", // domain axis label
				"hit", // range axis label
				lineDataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // urls
				);
		CategoryPlot plot = chart.getCategoryPlot();
		// customise the range axis...
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setAutoRangeIncludesZero(true);
		rangeAxis.setUpperMargin(0.20);
		rangeAxis.setLabelAngle(Math.PI / 2.0);
		return chart;
	}

	public void drawChart() {
		JFreeChart jFreeChart = createChart(lineDataset);
		this.chartPanel = new ChartPanel(jFreeChart);
		setContentPane(this.chartPanel);
	}
	
	public LineCharts(String title) {
		super(title);
		this.lineDataset = new DefaultCategoryDataset();
	}
}
