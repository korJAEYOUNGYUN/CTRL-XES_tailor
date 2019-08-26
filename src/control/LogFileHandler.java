package control;

import java.util.ArrayList;
import java.util.Scanner;

import model.Attribute;
import model.LogFile;

public class LogFileHandler {
	
	private ArrayList<LogFile> logFileList;
	private int indexOfCrntLogFile;
	private static LogFileHandler instance = new LogFileHandler();
	
	
	private LogFileHandler() {
		logFileList = new ArrayList<>();
		indexOfCrntLogFile = -1;
	}
	
	
	public static LogFileHandler getInstance() {
		return instance;
	}
	
	
	//open filename 했을시 동작하는 함수
	public void setCrntLogFile(String fileName) {
		indexOfCrntLogFile = getFileIndex(fileName);
		if( indexOfCrntLogFile == -1 ) {
			System.out.println("USE err: wrong filename");
		}
	}
	
	
	public void setLogFile(String fileName) {
		LogFile logFile;
		
		FilePreProcessor filePreProcessor = new FilePreProcessor(fileName);
		logFile = filePreProcessor.doPreProcessing();
		
		logFileList.add(logFile);
	}
	
	
	public void showLogList() {
		int s = logFileList.size();
		System.out.println("***************************");
		System.out.println("<< List of prepared log files>>");
		for(int i = 0; i < s; i++) {
			System.out.println(i + " : " + logFileList.get(i).getName());
		}
		System.out.println("***************************");
	}
	
	
	public boolean hasCrntLogFile() {
		return indexOfCrntLogFile != -1 ? true : false;
	}
	
	
	public LogFile getCrntLogFile() {
		if( indexOfCrntLogFile == -1 ) {
			System.err.println("Use log file first");
			return null;
		}
		
		return logFileList.get(indexOfCrntLogFile);
	}
	
	
	public LogFile getLogFile(String name) {
		return logFileList.get(getFileIndex(name));
	}
	
	
	public int getFileIndex(String name) {
		int s = logFileList.size();
		int indexNum = 0;
		
		for(indexNum = 0; indexNum < s; indexNum++) {
			if( logFileList.get(indexNum).getName().equals(name) ) {
				return indexNum;
			}
		}
		return -1;
	}
	
	
	public void describeIndex(LogFile lf) {
		System.out.println(lf.getIndexTable());
	}
}
