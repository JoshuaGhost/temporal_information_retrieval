package model;

public class ResultDoc {
	private String title = null;
	private String text = null;
	private String date = null;
	
	public ResultDoc(String title, String date, String text) {
		this.title = title;
		this.date = date;
		this.text = text;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getDate() {
		return this.date;
	}
	
	public String getText() {
		return this.text;
	}
}
