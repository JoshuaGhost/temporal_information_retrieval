package model;

import org.w3c.dom.Node;

public interface XmlPostNodeAdapter extends Node{
	public String getDate();
	public String getTitle();
	public String getContent();
}
