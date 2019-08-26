package model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class LogFile {
	private String name;
	private int numOfTrace;
	private String headLine;
	private Hashtable<String, Integer> indexTable;
	private ArrayList<Attribute> traceScope;
	private ArrayList<Attribute> eventScope;
	
	
	public LogFile(String name) {
		this.name = name;
		init();
	}
	
	
	private void init() {
		numOfTrace = 0;
		indexTable = new Hashtable<>();
		traceScope = new ArrayList<>();
		eventScope = new ArrayList<>();
	}
	
	
	public String getName() {
		return name;
	}
	
	
	public void setNumOfTrace(int numOfTrace) {
		this.numOfTrace = numOfTrace;
	}
	
	
	public int getNumOfTrace() {
		return numOfTrace;
	}
	
	
	public void setHeadLine(String str) {
		headLine = str;
	}
	
	
	public String getHeadLine() {
		return headLine;
	}
	
	
	public void addIndex(String key, Integer value) {
		indexTable.put(key, value);
	}
	
	
	public int getIndex(String key) {
		return indexTable.get(key);
	}
	
	
	public void addEventScope(Attribute attr) {
		eventScope.add(attr);
	}
	
	
	public void addTraceScope(Attribute attr) {
		traceScope.add(attr);
	}
	
	
	public List<Attribute> getEventScope() {
		return eventScope;
	}
	
	
	public List<Attribute> getTraceScope() {
		return traceScope;
	}
	
	
	public Hashtable<String, Integer> getIndexTable() {
		return indexTable;
	}
}
