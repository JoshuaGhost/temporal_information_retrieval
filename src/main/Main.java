package main;

import java.io.File;
import java.util.Hashtable;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.jfree.ui.RefineryUtilities;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import view.LineCharts;
import controler.LuceneRetriever;

public class Main {
	public static void main(String[] args) throws Exception {
		final String root = ".";
		final String indexPath = root+"/indexes";
		final String queryPath = root+"/data/queries.xml";
		//final String dataPath = root+"/data/nyt/data";
		final String dataPath = root+"/data/sample";
		//String query = "China air pollution";
		final String startTime = "2011-06";
		final String endTime = "2012-18";

		final controler.LuceneRetriever luceneRetriever = new controler.LuceneRetriever();
		//final String [] argvs1  = {"HalloLucene", "build", root+"data\\sample"};
		luceneRetriever.build(indexPath, dataPath);
		//		final String[] argvs2 = {"HalloLucene", "search", "Earthquake", "100"};
		//		luceneRetriever.excute(argvs2);
		//		final String[] argvs3 = {"HalloLucene", "tend", "Earthquake", "100"};
		
	/*
		File queryFile = new File(queryPath);
		model.XmlDoc queryXmlDoc = new model.XmlDoc(queryFile);
		NodeList queries = queryXmlDoc.getElementsByTagName("title");
		
		final LineCharts lineCharts = new LineCharts("Historical distribution");

		for (int queryNum = 0; queryNum<queries.getLength(); queryNum++) {
			Element queryNode = (Element) queries.item(queryNum);
			String query = queryNode.getFirstChild().getNodeValue();

			Hashtable<String, Integer> trend = luceneRetriever.temporalTrend(query, indexPath, startTime, endTime);
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
		}
		
		lineCharts.drawChart();
		lineCharts.pack();
		RefineryUtilities.centerFrameOnScreen(lineCharts);
		lineCharts.setVisible(true);
		*/
	} 
}
