package model;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

import utils.TemporalClass;

public class SampleDataPost implements XmlPostNodeAdapter {

	private double tempBase = 0.03;
	Node currentPostNode = null;
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	DocumentBuilder builder = null;
	Document currentPostDoc = null;
	Node importedNode = null;
	Node metaInfoNode = null;
	//Note: date and publishDate infer the same date with different format
	//date is '-'-split, i.e. "2015-12-11"
	//publishDate is the same date without '-'-split and also converted into Integer, i.e. 20151211
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
	int pubYear = 0;
	int pubMonth = 0;
	int pubDate = 0;
	int publishDate = 0;
	String title = null;
	String content = null;

	public SampleDataPost(Node post) throws ParserConfigurationException {
		currentPostNode = post;
		factory.setNamespaceAware(true);
		builder = factory.newDocumentBuilder();
		currentPostDoc = builder.newDocument();
		importedNode = currentPostDoc.importNode(currentPostNode, true);
		currentPostDoc.appendChild(importedNode);

		metaInfoNode = post.getFirstChild();
		while (metaInfoNode.getNodeType() != Node.ELEMENT_NODE) {
			metaInfoNode = metaInfoNode.getNextSibling();
		}

		Node childOfMetaInfoNode = metaInfoNode.getFirstChild();
		while (childOfMetaInfoNode.getNextSibling() != null) {
			childOfMetaInfoNode = childOfMetaInfoNode.getNextSibling();
			if (childOfMetaInfoNode.getNodeType() == Node.ELEMENT_NODE) {
				Element childElement = (Element) childOfMetaInfoNode;
				if (childElement.getAttribute("name").equals("date")) {
					String[] pubTimeParts = childElement.getFirstChild().getNodeValue().split("-");
					pubYear = Integer.valueOf(pubTimeParts[0]);
					pubMonth = Integer.valueOf(pubTimeParts[1]);
					pubDate = Integer.valueOf(pubTimeParts[2]);
				} else if (childElement.getAttribute("name").equals("title")) {
					this.title = childElement.getFirstChild().getNodeValue();
				}
			}
		}

		Node textNode = metaInfoNode.getNextSibling();
		try {
			while (textNode.getNodeType() != Node.ELEMENT_NODE) {
				textNode = textNode.getNextSibling();
			}
			this.content = textNode.getTextContent();
		} catch(NullPointerException nullPointer) {
			System.out.println("[Error]: "+metaInfoNode.getFirstChild().getNextSibling().getFirstChild().getNodeValue());
		}
	}

	private String preventNullString(String str) {
		if (str.length()==0 | str == null) {
			return "";
		}
		return str;
	}

	@Override
	public String getDate() {
		return preventNullString(String.valueOf(pubYear)+
																"-"+
																String.valueOf(pubMonth)+
																"-"+
																String.valueOf(pubDate));
	}

	@Override
	public String getTitle() {
		return preventNullString(this.title);
	}

	@Override
	public String getContent() {
		return preventNullString(this.content);
	}

	private int getTermInfo(String tTermTimeS) {
		int tTermYear = 0;
		int tTermMonth = 0;
		int tTermDate = 0;
		int termInfo = 0;
		if (tTermTimeS.length() == 0) {
			termInfo |= TemporalClass.ATEMP;
			return termInfo;
		}
		if (tTermTimeS.length() >= 4) {
			tTermYear = Integer.valueOf(tTermTimeS.substring(0, 4));
			if (tTermYear - pubYear < -1) {
				termInfo |= TemporalClass.PAST;
				return termInfo;
			} else if (tTermYear - pubYear > 1) {
				termInfo |= TemporalClass.FUTURE;
				return termInfo;
			} else if (tTermTimeS.length() < 6){
				termInfo |= TemporalClass.RECENT;
			}
		}
		if (tTermTimeS.length() >= 6) {
			tTermMonth = Integer.valueOf(tTermTimeS.substring(4,6));
			int pub = pubYear * 12 + pubMonth;
			int ter = tTermYear * 12 + tTermMonth;
			if (pub == ter) {
				termInfo |= TemporalClass.RECENT;
			} else if (ter < pub) {
				termInfo |= TemporalClass.PAST;
				if (tTermTimeS.length() < 8) {
					return termInfo;
				}
			} else {
				termInfo |= TemporalClass.FUTURE;
				if (tTermTimeS.length() < 8) {
					return termInfo;
				}
			}
		}
		if (tTermTimeS.length() >= 8) {
			tTermDate = Integer.valueOf(tTermTimeS.substring(6, 8));
			if (tTermDate > pubDate) {
				termInfo |= TemporalClass.FUTURE;
			} else if (tTermDate < pubDate) {
				termInfo |= TemporalClass.PAST;
			} 
		}
		return termInfo;
	}
	
	public Hashtable <String, Integer> getTemporalTerms() {
		Hashtable <String, Integer> ret = new Hashtable <String, Integer> ();
		NodeList temporalTerms = currentPostDoc.getElementsByTagName("t");
		for (int i = 0; i < temporalTerms.getLength(); i++) {
			Node tTermNode = temporalTerms.item(i);
			String tTerm = tTermNode.getFirstChild().getNodeValue();
			String tTermTimeS = tTermNode.getAttributes().getNamedItem("val").getNodeValue();
			int termInfo = getTermInfo(tTermTimeS);
			ret.put(tTerm, termInfo);
		}
		return ret;
	}

	@Override
	public Node appendChild(Node arg0) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node cloneNode(boolean arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short compareDocumentPosition(Node arg0) throws DOMException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public NamedNodeMap getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBaseURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeList getChildNodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getFeature(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getFirstChild() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getLastChild() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocalName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNamespaceURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getNextSibling() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNodeName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short getNodeType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getNodeValue() throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document getOwnerDocument() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getParentNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrefix() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getPreviousSibling() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTextContent() throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getUserData(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasAttributes() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasChildNodes() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Node insertBefore(Node arg0, Node arg1) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDefaultNamespace(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEqualNode(Node arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSameNode(Node arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSupported(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String lookupNamespaceURI(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String lookupPrefix(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void normalize() {
		// TODO Auto-generated method stub

	}

	@Override
	public Node removeChild(Node arg0) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node replaceChild(Node arg0, Node arg1) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNodeValue(String arg0) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPrefix(String arg0) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTextContent(String arg0) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public Object setUserData(String arg0, Object arg1, UserDataHandler arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
