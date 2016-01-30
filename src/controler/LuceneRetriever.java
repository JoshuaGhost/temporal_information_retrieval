package controler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
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

import model.ResultDoc;
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
			XmlDocAdapter xd = (XmlDocAdapter) new model.SampleDataDoc(file);
			NodeList posts = xd.getPostsNodeList();

			for (int postnum = 0; postnum < posts.getLength(); postnum++) {
				XmlPostNodeAdapter post = (XmlPostNodeAdapter) new SampleDataPost(posts.item(postnum));
				Document currentDoc = new Document();

				currentDoc.add(new StringField("filename", 	file.getName(), 							Field.Store.YES));
				currentDoc.add(new StringField("fullpath", 	file.getCanonicalPath(), 	Field.Store.YES));
				currentDoc.add(new StringField("date", 			post.getDate(),   					Field.Store.YES));
				currentDoc.add(new StringField("title",			post.getTitle(),  					Field.Store.YES));
				
				currentDoc.add(new TextField("content",post.getContent(),Field.Store.NO));
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
		QueryParser queryPaser = new QueryParser("text", new StandardAnalyzer());
		Query query = queryPaser.parse(queryString);
		System.out.println(query.toString());
		TopDocs tds = indexSearcher.search(query, numTopDocs);
		ScoreDoc[] sds = tds.scoreDocs;
		System.out.println(indexSearcher.explain(query, 1).toHtml());
		System.out.println(sds.length);
		System.out.println(tds.getMaxScore() - sds[sds.length - 1].score);
		List<model.ResultDoc> res = new ArrayList<model.ResultDoc>();
		for (ScoreDoc sd : sds) {
			Document d = indexSearcher.doc(sd.doc);
			res.add(new ResultDoc(d.get("title"), d.get("date"), d.get("text")));
		}
		indexReader.close();
		return res;
	}

	public void excute(String[] argv) throws Exception {

		String command = argv[1];
		String indexPath = "E:\\Users\\Assassin\\workspace\\temporal_information_retrieval\\indexes";

		switch (command) {
		case "build":
			int numDocs = 0;
			try {
				numDocs = build(indexPath, argv[2]);
			} catch (Exception e) {
				System.out.println("Exception occured in build\n");
				break;
			}
			System.out.printf("Indexing of %d documents succesfully finished\n", numDocs);
			break;

		case "search":
			// search(indexPath, argv[2], Integer.valueOf(argv[3]));
			break;

		case "exit":
			return;

		}
	}

	public Hashtable<String, Integer> temporalTrend(String query, String indexPath, String startTime, String endTime)
	        throws ParseException, IOException {
		Hashtable<String, Integer> trend = new Hashtable<String, Integer>();
		List<model.ResultDoc> resultDocs = null;
		// String indexPath =
		// "E:\\Users\\Assassin\\workspace\\temporal_information_retrieval\\indexes";
		resultDocs = search(indexPath, query, 1000);
		for (model.ResultDoc resultDoc : resultDocs) {
			// "2011-08-13".compareTo("2011-08-14") == -1
			if ((resultDoc.getDate().compareTo(startTime) >= 0) & (resultDoc.getDate().compareTo(endTime) <= 0)) {
				String[] date = resultDoc.getDate().split("-");
				if (date == null) {
					break;
				}
				String month = date[0] + "-" + date[1];
				if (trend.get(month) == null) {
					trend.put(month, 1);
					continue;
				}
				int numDoc = trend.get(month);
				numDoc++;
				trend.put(month, numDoc);
			}
		}
		return trend;
	}
}