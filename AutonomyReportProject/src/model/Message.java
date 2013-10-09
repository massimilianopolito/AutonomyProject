package model;

public class Message {
	public static int INFO = 	0;
	public static int ERROR = 	1;
	public static int WARNING = 2;
	
	private String text;
	private int type = -1;
	private String[] values;

	public String getText() {return text;}
	public void setText(String text) {this.text = text;}

	public String[] getValues() {return values;}
	public void setValues(String[] values) {this.values = values;}
	
	public int getType() {return type;}
	public void setType(int type) {this.type = type;}
}
