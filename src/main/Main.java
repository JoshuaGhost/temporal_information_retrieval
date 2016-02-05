package main;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.lucene.queryparser.classic.ParseException;
import org.jfree.ui.RefineryUtilities;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import controler.LuceneRetriever;
import view.LineCharts;

public class Main {
	
	static LuceneRetriever luceneRetriever = null;
	final static String root = ".";
	///root/workspace/temporal_information_retrieval/indexes
	final static String indexPath = root+"/indexes";
	//final String queryPath = root+"/data/queries/queries.xml";
	final static String queryPath = root+"/data/queries/NTCIR-11TIRTopicsFormalRun.xml";
	//final String dataPath = root+"/data/nyt/data";
	final static String dataPath = root+"/data/sample_total";

	//real start and end date:
	final static String startDateString = "2011-04-01";
	final static String endDateString   = "2013-12-31";
	
	//start and end dates for experiments:
	//final static String startDateString = "2011-04-01";
	//final static String endDateString = "2011-10-02";
	final static Date startDate = Date.valueOf(startDateString);
	final static Date endDate = Date.valueOf(endDateString);
	final static Calendar startCalendar = new GregorianCalendar();
	final static Calendar endCalendar = new GregorianCalendar();
	private static final int quiryNum = 3;

	final static LineCharts lineCharts = new LineCharts("Historical distribution");
	private static final int predictDay = 10;

	private static void multipleTrend(NodeList queries) throws ParseException, IOException {
		for (int queryNum = 0; queryNum<queries.getLength(); queryNum++) {
			singleTend(queries, queryNum);
		}
	}
	
	private static Hashtable <String, Double> singleTend(NodeList queries, int queryNum) throws ParseException, IOException {
		
		Element queryNode = (Element) queries.item(queryNum);
		String query = queryNode.getFirstChild().getNodeValue();

		Hashtable<String, Double> trend = luceneRetriever.temporalHistory(query, indexPath, startCalendar, endCalendar);
		Calendar currentCalendar = (Calendar) startCalendar.clone();
		
		while (currentCalendar.before(endCalendar)) {
			String currentDateString = cal2str(currentCalendar);
			lineCharts.insertData(trend.get(currentDateString)==null?0:trend.get(currentDateString),
																 query,	currentDateString);
			currentCalendar.add(Calendar.DATE, 1);
		}
		return trend;
	} 
	
	private static String cal2str(Calendar currentCalendar) {
		int year = currentCalendar.get(Calendar.YEAR),
				 month = currentCalendar.get(Calendar.MONTH)+1,
				 date = currentCalendar.get(Calendar.DATE);
		String 	sYear = String.valueOf(year),
						 	sMonth = (month<10?"0":"")+String.valueOf(month),
							sDate = (date<10?"0":"")+String.valueOf(date);
		String converted = sYear+'-'+sMonth+'-'+sDate;
		return converted;
	}

	private static void singleTendAndPredict(NodeList queries, int queiryNum) throws ParseException, IOException {
		Hashtable<String, Double> trend = singleTend(queries, queiryNum);
		Hashtable<String, Double> predict = luceneRetriever.smoothingAndPredict(trend, predictDay, startCalendar, endCalendar, true, false);
		//Calendar currentCalendar = (Calendar) endCalendar.clone();
		Calendar currentCalendar = (Calendar) startCalendar.clone();
		Calendar finalCal = (Calendar) endCalendar.clone();
		finalCal.add(Calendar.DATE, predictDay);
		while (currentCalendar.before(finalCal)) {
			String currentDateString = cal2str(currentCalendar);
			Double value = (predict.get(currentDateString)==null?0:predict.get(currentDateString))+
												(trend.get(currentDateString)==null?0:trend.get(currentDateString));
								
			lineCharts.insertData(value,
																 "Smoothed and Prediction of "+queries.item(queiryNum).getFirstChild().getNodeValue(),	
																 currentDateString);
			currentCalendar.add(Calendar.DATE, 1);
		}
	}
	
	public static void main(String[] args) throws Exception {

		startCalendar.setTime(startDate);
		endCalendar.setTime(endDate);
		luceneRetriever = new controler.LuceneRetriever();
		
		//luceneRetriever.build(indexPath, dataPath);

		File queryFile = new File(queryPath);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		org.w3c.dom.Document queryXmlDoc = db.parse(queryFile);
		NodeList queries = queryXmlDoc.getElementsByTagName("title");
		
		//multipleTrend(queries);
		singleTendAndPredict(queries, quiryNum);

		lineCharts.drawChart();
		lineCharts.pack();
		RefineryUtilities.centerFrameOnScreen(lineCharts);
		lineCharts.setVisible(true);
	}
	
}
