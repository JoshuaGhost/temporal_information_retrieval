package test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.nio.file.FileSystems;

import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public class HelloLucene {

	protected static Document getDocument(File f) throws Exception {
		Document doc = new Document();
		doc.add(new TextField("contents", new FileReader(f)));
		doc.add(new StringField("filename",
								f.getName(),
								Field.Store.YES));
		doc.add(new StringField("fullpath",
								f.getCanonicalPath(),
								Field.Store.YES));
		return doc;		
	}
	
	public static IndexWriter indexer(String dir) throws IOException {
		Directory indexDir = FSDirectory.open(FileSystems.getDefault().getPath(dir));
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
		cfg.setOpenMode(OpenMode.CREATE_OR_APPEND);
		return new IndexWriter(indexDir, cfg);
	}
	
	private static void indexFile(IndexWriter w,
								  File f) throws Exception {
		Document doc = getDocument(f);
		w.addDocument(doc);
	}
	
	private static int buildIndex(IndexWriter w,
							 String dataDir,
							 KeywordFilter filter) throws Exception{
		KeywordFilter kf = new KeywordFilter();
		Iterator<File> itr = FileUtils.iterateFiles(new File(dataDir), 
													kf, 
													TrueFileFilter.INSTANCE);
		while (itr.hasNext()) {
			File file = itr.next();
			if (filter == null || filter.accept(file)){
				indexFile(w, file);
			}
		}
		
		return w.numDocs();
	}
	
	public static void search(String indexDirName,
					   String q, int topNum)
		throws IOException, ParseException {
		IndexReader rdr = DirectoryReader.open(FSDirectory.open(
				FileSystems.getDefault().getPath(indexDirName)));

		IndexSearcher is = new IndexSearcher(rdr);

		QueryParser parser = 
				new QueryParser("contents",
						new StandardAnalyzer());
		Query query = parser.parse(q);

		TopDocs hits = is.search(query, topNum);

		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = is.doc(scoreDoc.doc);
			System.out.println(doc.get("fullpath"));
		}
	}

	public static void main(String[] argv) throws Exception {
		
		String indexDirName = "indexes";
		String dataDir = "E:\\users\\Assassin\\workspace\\temporal_information_retrieval\\data\\nyt\\data";
		String q = "test";
		int topNum = 10;
		
		Directory indexDir = FSDirectory.open(FileSystems.getDefault().getPath(indexDirName));
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
		cfg.setOpenMode(OpenMode.CREATE_OR_APPEND);
		IndexWriter w = new IndexWriter(indexDir, cfg);
		
		buildIndex(w, dataDir, new KeywordFilter());
		System.out.println(w.numDocs());
		w.close();
		
		search(indexDirName, q, topNum);
	}
}