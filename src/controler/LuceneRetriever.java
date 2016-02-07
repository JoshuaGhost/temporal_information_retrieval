package controler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.w3c.dom.NodeList;

import model.NytDataDoc;
import model.NytDataPost;
import model.ResultDoc;
import model.SampleDataDoc;
import model.SampleDataPost;
import model.XmlDocAdapter;
import model.XmlPostNodeAdapter;
import controler.MyFileFilters;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class LuceneRetriever {

	public int build(String indexPath, String dataPath) throws Exception {
		
		Directory indexDir = FSDirectory.open(Paths.get(indexPath));

		Analyzer analyzer = new StandardAnalyzer();

		IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
		cfg.setOpenMode(OpenMode.CREATE_OR_APPEND);

		IndexWriter indexWriter = new IndexWriter(indexDir, cfg);

		File fd = new File(dataPath);
		for (File file : FileUtils.listFiles(fd, new MyFileFilters(), TrueFileFilter.INSTANCE)) {
			System.out.println(file.getName());
			XmlDocAdapter xd = (XmlDocAdapter) new SampleDataDoc(file);
			// XmlDocAdapter xd = (XmlDocAdapter) new NytDataDoc(file);
			NodeList posts = xd.getPostsNodeList();

			for (int postnum = 0; postnum < posts.getLength(); postnum++) {
				XmlPostNodeAdapter post = (XmlPostNodeAdapter) new SampleDataPost(posts.item(postnum));
				// XmlPostNodeAdapter post = (XmlPostNodeAdapter) new
				// NytDataPost(posts.item(postnum));
				Document currentDoc = new Document();

				try {
					currentDoc.add(new StringField("filename", file.getName(), Field.Store.YES));
					currentDoc.add(new StringField("fullpath", file.getCanonicalPath(), Field.Store.YES));
					currentDoc.add(new StringField("date", post.getDate(), Field.Store.YES));
					currentDoc.add(new StringField("title", post.getTitle(), Field.Store.YES));

					currentDoc.add(new TextField("content", post.getContent(), Field.Store.NO));
				} catch (Exception E) {

				}
				indexWriter.addDocument(currentDoc);
			}
		}
		int numDocs = indexWriter.numDocs();
		indexWriter.commit();
		indexWriter.close();
		return numDocs;
	}

	public List<model.ResultDoc> search(String indexPath, String queryString, int numTopDocs)
	        throws ParseException, IOException {
		Directory indexDir = FSDirectory.open(Paths.get(indexPath));
		System.out.println(indexPath);
		IndexReader indexReader = DirectoryReader.open(indexDir);

		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		QueryParser queryPaser = new QueryParser("content", new StandardAnalyzer());
		Query query = queryPaser.parse(queryString);
		System.out.println(query.toString());
		TopDocs tds = indexSearcher.search(query, numTopDocs);
		ScoreDoc[] sds = tds.scoreDocs;
		
		// System.out.println(indexSearcher.explain(query, 1).toHtml());
		// System.out.println(sds.length);
		if (tds != null & sds.length != 0) {
			System.out.println(tds.getMaxScore() - sds[sds.length - 1].score);
		}
		List<model.ResultDoc> res = new ArrayList<model.ResultDoc>();
		for (ScoreDoc sd : sds) {
			Document d = indexSearcher.doc(sd.doc);
			res.add(new ResultDoc(d.get("title"), d.get("date"), d.get("content")));
		}
		indexReader.close();
		return res;
	}

	public Hashtable<String, Double> temporalHistory(String query, String indexPath, Calendar startTime,
	        Calendar endTime) throws ParseException, IOException {
		Hashtable<String, Double> history = new Hashtable<String, Double>();
		List<model.ResultDoc> resultDocs = null;
		// String indexPath =
		// "E:\\Users\\Assassin\\workspace\\temporal_information_retrieval\\indexes";
		resultDocs = search(indexPath, query, 1000);
		for (model.ResultDoc resultDoc : resultDocs) {

			String resultDateString = resultDoc.getDate();
			Date resultDate = Date.valueOf(resultDateString);
			Calendar resultCalendar = new GregorianCalendar();
			resultCalendar.setTime(resultDate);

			if (resultCalendar.after(startTime) & resultCalendar.before(endTime)) {
				if (history.get(resultDateString) == null) {
					history.put(resultDateString, 1.0);
					continue;
				} else {
					Double numDoc = history.get(resultDateString);
					numDoc += 1.0;
					history.put(resultDateString, numDoc);
				}
			}
		}
		return history;
	}

	public Hashtable<String, Double> smoothingAndPredict(Hashtable<String, Double> history, int predictday,
	        Calendar startCalendar, Calendar endCalendar, boolean trend_model, boolean periodic_model) {
		Hashtable<String, Double> sm_pre = new Hashtable<String, Double>();

		double alpha = estimate_alpha(history, startCalendar, endCalendar);
		double beta_star = 0.0;
		if (trend_model) {
			beta_star = estimate_beta_star(history, startCalendar, endCalendar, alpha);
		}
		// smoothing

		Calendar curCal = (Calendar) startCalendar.clone();

		double lt = 0.0;
		double ltm1 = 0.0;
		double bt = 0.0;

		for (curCal.add(Calendar.DATE, 1); 
				curCal.before(endCalendar); 
				curCal.add(Calendar.DATE, 1)) {
			ltm1 = lt;
			double yt = (history.containsKey(cal2str(curCal)) ? history.get(cal2str(curCal)) : 0);

			sm_pre.put(cal2str(curCal), lt);
			if (trend_model) {
				lt = alpha * yt + (1 - alpha) * (ltm1 + bt);
				bt = beta_star * (lt - ltm1) + (1 - beta_star) * bt;
			} else {
				lt = lt + alpha * (yt - lt);
			}
		}
		sm_pre.put(cal2str(curCal), lt);

		// prediction
		Calendar finalCal = (Calendar) endCalendar.clone();
		finalCal.add(Calendar.DATE, predictday);
	
		double btm1 = bt;
		for (curCal.add(Calendar.DATE, 1); 
				curCal.before(finalCal); 
				curCal.add(Calendar.DATE, 1)) {
			ltm1 = lt;
			if (trend_model) {
				lt = alpha * lt + (1 - alpha) * (ltm1 + btm1);
				btm1 = bt;
				bt = beta_star * (lt - ltm1) + (1 - beta_star) * bt;
			} else {
				lt = alpha * lt + (1 - alpha) * (lt - ltm1);
			}
			sm_pre.put(cal2str(curCal), lt);
		}
		return sm_pre;
	}

	private double estimate_beta_star(Hashtable<String, Double> history, Calendar startCalendar, Calendar endCalendar,
	        double alpha) {
		double beta_star = 0.0;
		double rmse_min = Double.MAX_VALUE;

		for (double b = 0.1; b < 1.0; b += 0.1) {
			Calendar curCal = (Calendar) startCalendar.clone();
			curCal.add(Calendar.DATE, 1);

			double ltm1 = (history.containsKey(cal2str(startCalendar)) ? history.get(cal2str(startCalendar)) : 0);
			double lt = (history.containsKey(cal2str(curCal)) ? history.get(cal2str(curCal)) : 0);
			double bt = 0.0;
			double e = 0.0;

			for (curCal.add(Calendar.DATE, 1); curCal.before(endCalendar); curCal.add(Calendar.DATE, 1)) {
				ltm1 = lt;
				double yt = (history.containsKey(cal2str(curCal)) ? history.get(cal2str(curCal)) : 0);

				e += Math.pow(yt - ltm1, 2);

				lt = alpha * yt + (1 - alpha) * (ltm1 + bt);
				bt = b * (lt - ltm1) + (1 - b) * bt;
			}
			e = Math.pow(e, 0.5);
			if (e < rmse_min) {
				beta_star = b;
				rmse_min = e;
			}
		}

		System.out.println(beta_star);
		System.out.println(rmse_min);
		return beta_star;
	}

	private double estimate_alpha(Hashtable<String, Double> history, Calendar startCalendar, Calendar endCalendar) {
		double alpha = 0.0;
		double rmse_min = Double.MAX_VALUE;
		// estimate alpha
		for (double a = 0.1; a < 1.0; a += 0.1) {
			Calendar curCal = (Calendar) startCalendar.clone();
			curCal.add(Calendar.DATE, 1);

			double lt = 0.0;
			double e = 0.0;

			for (curCal.add(Calendar.DATE, 1); curCal.before(endCalendar); curCal.add(Calendar.DATE, 1)) {
				double yt = (history.containsKey(cal2str(curCal)) ? history.get(cal2str(curCal)) : 0);

				e += Math.pow(yt - lt, 2);

				lt = lt + a * (yt - lt);
			}
			e = Math.pow(e, 0.5);
			if (e < rmse_min) {
				alpha = a;
				rmse_min = e;
			}
		}

		System.out.println(alpha);
		System.out.println(rmse_min);
		return alpha;
	}

	private static String cal2str(Calendar currentCalendar) {
		int year = currentCalendar.get(Calendar.YEAR), month = currentCalendar.get(Calendar.MONTH) + 1,
		        date = currentCalendar.get(Calendar.DATE);
		String sYear = String.valueOf(year), sMonth = (month < 10 ? "0" : "") + String.valueOf(month),
		        sDate = (date < 10 ? "0" : "") + String.valueOf(date);
		String converted = sYear + '-' + sMonth + '-' + sDate;
		return converted;
	}
}