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
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import model.ResultDoc;

public class LuceneRetriever {

	private int build(String indexPath, String dataDir) throws Exception {
		Directory indexDir = FSDirectory.open(Paths.get(indexPath));

		Analyzer analyzer = new StandardAnalyzer();

		IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
		cfg.setOpenMode(OpenMode.CREATE_OR_APPEND);

		IndexWriter indexWriter = new IndexWriter(indexDir, cfg);

		File fd = new File(dataDir);
		for (File file:fd.listFiles()) {
			model.XmlDoc xd = new model.XmlDoc(file);
			NodeList docs = xd.getElementsByTagName("doc");

			for (int docnum = 0; docnum<docs.getLength(); docnum++) {
				Node doc = docs.item(docnum);
				Document indexedDoc = new Document();

				indexedDoc.add(new StringField("filename", file.getName(), Field.Store.YES));
				indexedDoc.add(new StringField("fullpath", file.getCanonicalPath(),	Field.Store.YES));

				Node metaInfoNode = doc.getFirstChild();
				//because of format of the input data, the first child of 
				//each doc is \n or null, instead of meta-info
				while (metaInfoNode.getNodeType() != Node.ELEMENT_NODE) {
					metaInfoNode = metaInfoNode.getNextSibling();
				}
				Node childOfMetaInfoNode = metaInfoNode.getFirstChild();

				while (childOfMetaInfoNode.getNextSibling() != null) {
					childOfMetaInfoNode = childOfMetaInfoNode.getNextSibling();
					if (childOfMetaInfoNode.getNodeType() == Node.ELEMENT_NODE) {
						Element childElement = (Element) childOfMetaInfoNode;
						if (childElement.getAttribute("name").equals("date")) {
							indexedDoc.add(new StringField("date",
									childElement.getFirstChild().getNodeValue(),
									Field.Store.YES));
						} else if (childElement.getAttribute("name").equals("title")) {
							indexedDoc.add(new StringField("title",
									childElement.getFirstChild().getNodeValue(),
									Field.Store.YES));
						}
					}
				}

				Node textNode = metaInfoNode.getNextSibling();
				while (textNode.getNodeType() != Node.ELEMENT_NODE) {
					textNode = textNode.getNextSibling();
				}
				indexedDoc.add(new TextField("text",
						textNode.getTextContent(),
						Field.Store.YES));
				indexWriter.addDocument(indexedDoc);
			}
		}
		int numDocs = indexWriter.numDocs();
		indexWriter.close();
		return numDocs;		
	}


	public List<model.ResultDoc> search(String indexPath, String queryString, int numTopDocs) throws ParseException, IOException {
		Directory indexDir =  FSDirectory.open(Paths.get(indexPath));
		IndexReader indexReader = DirectoryReader.open(indexDir);

		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		QueryParser queryPaser = new QueryParser("text", new StandardAnalyzer());
		Query query = queryPaser.parse(queryString);
		System.out.println(query.toString());
		TopDocs tds = indexSearcher.search(query, numTopDocs);
		ScoreDoc[] sds = tds.scoreDocs;
		System.out.println(sds.length);
		List<model.ResultDoc> res = new ArrayList<model.ResultDoc>();
		for (ScoreDoc sd:sds) {
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
			//search(indexPath, argv[2], Integer.valueOf(argv[3]));
			break;

		case "exit":
			return;

		}
	}

	public Hashtable<String, Integer> temporalTrend(String query, String startTime, String endTime) throws ParseException, IOException {
		Hashtable<String, Integer> trend = new Hashtable<String, Integer>();
		List<model.ResultDoc> resultDocs = null;
		String indexPath = "E:\\Users\\Assassin\\workspace\\temporal_information_retrieval\\indexes";
		resultDocs = search(indexPath, query, 100);
		for (model.ResultDoc resultDoc : resultDocs) {
			//"2011-08-13".compareTo("2011-08-14") == -1
			if ((resultDoc.getDate().compareTo(startTime)  >= 0) &
					(resultDoc.getDate().compareTo(endTime)<= 0)) {
				String[] date = resultDoc.getDate().split("-");
				if (date == null) {
					break;
				}
				String month = date[0]+"-"+date[1];
				if (trend.get(month) == null){
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