package model;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

public class SampleDataPost implements XmlPostNodeAdapter {

	Node metaInfoNode = null;
	String date = null;
	String title = null;
	String content = null;

	public SampleDataPost(Node post) {
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
					this.date = childElement.getFirstChild().getNodeValue();
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
		return preventNullString(this.date);
	}

	@Override
	public String getTitle() {
		return preventNullString(this.title);
	}

	@Override
	public String getContent() {
		return preventNullString(this.content);
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
