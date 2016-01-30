package model;


import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public interface XmlDocAdapter extends Document {

	DocumentBuilderFactory dbf = null;
	DocumentBuilder db = null;
	Document doc = null;

	public NodeList getPostsNodeList();
	public List<String> getElementsByTagNameAndAttribute(String tagName, String AttributeName, String AttributeValue);

}