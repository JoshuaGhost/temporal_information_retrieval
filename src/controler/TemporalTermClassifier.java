package controler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
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
import model.SampleDataDoc;
import model.SampleDataPost;
import model.XmlDocAdapter;
import model.XmlPostNodeAdapter;
import utils.TemporalClass;

public class TemporalTermClassifier {
	public static int build(String indexPath, String dataPath) throws Exception {
		
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
				Hashtable <String, Integer> temporalTerms = post.getTemporalTerms();
				for (String term: temporalTerms.keySet()) {//every temporal term in one document, hit illustrates possibility
					Document currentDoc = new Document();
					currentDoc.add(new TextField("term", term, Field.Store.NO));
					currentDoc.add(new StringField("class", String.valueOf(temporalTerms.get(term)), Field.Store.YES));
					indexWriter.addDocument(currentDoc);
				}
			}
		}
		int numDocs = indexWriter.numDocs();
		indexWriter.commit();
		indexWriter.close();
		return numDocs;
	}
	
	public static List<model.ResultDoc> search(String indexPath, String termString, int numTopDocs)
	        throws ParseException, IOException {
		Directory indexDir = FSDirectory.open(Paths.get(indexPath));
		System.out.println(indexPath);
		IndexReader indexReader = DirectoryReader.open(indexDir);

		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		QueryParser queryPaser = new QueryParser("term", new StandardAnalyzer());
		Query query = queryPaser.parse(termString);
		System.out.println(query.toString());
		TopDocs tds = indexSearcher.search(query, numTopDocs);
		ScoreDoc[] sds = tds.scoreDocs;
		
		// System.out.println(indexSearcher.explain(query, 1).toHtml());
		// System.out.println(sds.length);
		if (tds != null & sds.length != 0) {
			System.out.println(tds.getMaxScore() - sds[sds.length - 1].score);
		}
		List<model.ResultDoc> res = new ArrayList<model.ResultDoc>();
		int pPast = 0;
		int pRecent= 0;
		int pFuture = 0;
		int pAtemp = 0;
		for (ScoreDoc sd : sds) {
			Document d = indexSearcher.doc(sd.doc);
			int point = Integer.valueOf(d.get("class"));
			pPast = pPast + (((point & TemporalClass.PAST)>0) ? 1:0);
			pRecent = pRecent + (((point & TemporalClass.RECENT)>0)?1:0);
			pFuture = pFuture + (((point & TemporalClass.FUTURE)>0)?1:0);
			pAtemp = pAtemp + (((point & TemporalClass.ATEMP)>0)?1:0);
			if (point == 0) {
				pAtemp ++;
			}
		}
		indexReader.close();
		System.out.println("Past: " + pPast);
		System.out.println("Recent: " + pRecent);
		System.out.println("pFuture: " + pFuture);
		System.out.println("pAtemp: " + pAtemp);
		return res;
	}
}
