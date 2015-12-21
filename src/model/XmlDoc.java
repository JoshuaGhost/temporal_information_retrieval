package model;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;
import org.xml.sax.SAXException;

public class XmlDoc implements Document {

	private DocumentBuilderFactory dbf = null;
	private DocumentBuilder db = null;
	Document doc = null;
	
	public XmlDoc(File file) throws SAXException, IOException, ParserConfigurationException {
		 this.dbf = DocumentBuilderFactory.newInstance();
		 this.db = dbf.newDocumentBuilder();
		 this.doc = db.parse(file);
		  //this.nl = doc.getElementsByTagName("title");
		// System.out.println(nl.item(1).getFirstChild().getNodeValue());
		 //System.out.println(doc.getElementsByTagName("title").getLength());
	}

	
	public List<String> getElementsByTagNameAndAttribute(String tagName, String AttributeName, String AttributeValue) {
		List <String> ret = new ArrayList<String>();
		NodeList nl = doc.getElementsByTagName(tagName);
		System.out.println(nl.getLength());
		for (int i = 0; i < nl.getLength(); i++) {
			Node no = nl.item(i).getAttributes().getNamedItem(AttributeName);
			if (no.getNodeValue().equals(AttributeValue)){
				ret.add(nl.item(i).getFirstChild().getNodeValue().toString());
			}
		}
		return ret;	
	}

	@Override
	public String getNodeName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNodeValue() throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNodeValue(String nodeValue) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public short getNodeType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Node getParentNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeList getChildNodes() {
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
	public Node getPreviousSibling() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getNextSibling() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NamedNodeMap getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document getOwnerDocument() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node insertBefore(Node newChild, Node refChild) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node removeChild(Node oldChild) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node appendChild(Node newChild) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildNodes() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Node cloneNode(boolean deep) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void normalize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSupported(String feature, String version) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNamespaceURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrefix() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPrefix(String prefix) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getLocalName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasAttributes() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getBaseURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short compareDocumentPosition(Node other) throws DOMException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getTextContent() throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTextContent(String textContent) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSameNode(Node other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String lookupPrefix(String namespaceURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDefaultNamespace(String namespaceURI) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String lookupNamespaceURI(String prefix) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEqualNode(Node arg) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getFeature(String feature, String version) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object setUserData(String key, Object data, UserDataHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getUserData(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DocumentType getDoctype() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DOMImplementation getImplementation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element getDocumentElement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element createElement(String tagName) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DocumentFragment createDocumentFragment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Text createTextNode(String data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comment createComment(String data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CDATASection createCDATASection(String data) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attr createAttribute(String name) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityReference createEntityReference(String name) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeList getElementsByTagName(String tagname) {
		return doc.getElementsByTagName(tagname);
	}

	@Override
	public Node importNode(Node importedNode, boolean deep) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element getElementById(String elementId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInputEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getXmlEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getXmlStandalone() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getXmlVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setXmlVersion(String xmlVersion) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getStrictErrorChecking() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setStrictErrorChecking(boolean strictErrorChecking) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDocumentURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDocumentURI(String documentURI) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Node adoptNode(Node source) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DOMConfiguration getDomConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void normalizeDocument() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Node renameNode(Node n, String namespaceURI, String qualifiedName) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}
}