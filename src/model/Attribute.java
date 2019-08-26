package model;

public class Attribute {
	private String type;
	private String key;
	
	public Attribute(String type, String key) {
		this.type = type;
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	
	public String getType() {
		return type;
	}
}