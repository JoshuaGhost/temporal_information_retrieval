package main;

import java.util.Hashtable;
import java.util.List;

import org.jfree.ui.RefineryUtilities;

import view.LineCharts;
import controler.LuceneRetriever;

public class Main {
	public static void main(String[] args) throws Exception {
		final String root = "E:\\Users\\Assassin\\workspace\\temporal_information_retrieval\\";
		final controler.LuceneRetriever hl = new controler.LuceneRetriever();
		final String [] argvs1  = {"HalloLucene", "build", root+"data\\sample"};
		hl.excute(argvs1);
		String query = "Earthquake";
//		final String[] argvs2 = {"HalloLucene", "search", "Earthquake", "100"};
//		hl.excute(argvs2);
//		final String[] argvs3 = {"HalloLucene", "tend", "Earthquake", "100"};
		String startTime = "2011-06";
		String endTime = "2012-18";
		Hashtable<String, Integer> trend = hl.temporalTrend(query, startTime, endTime);
		final LineCharts lineCharts = new LineCharts("Historical distribution");
		//StringBuffer currentTime = new StringBuffer(startTime);
		String[] currentTime = startTime.split("-");
		String currentMonth = currentTime[0]+"-"+currentTime[1];
		while (currentMonth.compareTo(endTime)<=0) {
			lineCharts.insertData(trend.get(currentMonth)==null?0:trend.get(currentMonth),
								  query,
								  currentMonth);
			int year = Integer.valueOf(currentTime[0]);
			int month = Integer.valueOf(currentTime[1])+1;
			year  = (month == 13)?(year+1):year;
			month = (month == 13)?1:month;
			currentTime[0] = String.valueOf(year);
			currentTime[1] = month<10?"0"+String.valueOf(month):String.valueOf(month);
			currentMonth = currentTime[0]+"-"+currentTime[1];
		}
		lineCharts.drawChart();
		lineCharts.pack();
		RefineryUtilities.centerFrameOnScreen(lineCharts);
		lineCharts.setVisible(true);
	} 
}
