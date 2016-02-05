package model;

import org.w3c.dom.NodeList;

import com.nytlabs.corpus.NYTCorpusDocument;

public class NytPostsList implements NodeList{

	private NytPostNode nytPost = null;
	
	public NytPostsList(NYTCorpusDocument ncDoc) {
		this.nytPost = new NytPostNode(ncDoc);
	}

	@Override
	public int getLength() {
		return 1;
	}

	@Override
	public NytPostNode item(int arg0) {
		return this.nytPost;
	}

}
