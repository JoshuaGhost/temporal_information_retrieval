package tests;

import org.jfree.ui.RefineryUtilities;
import org.junit.Test;

public class TestJFreeChart {
	
	@Test
	public void testLineChart() {
		  view.LineCharts fjc = new view.LineCharts("����ͼ");
		  fjc.pack();
		  RefineryUtilities.centerFrameOnScreen(fjc);
		  fjc.setVisible(true);
		} 
}
