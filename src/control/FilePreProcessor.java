package control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.StringTokenizer;

import model.Attribute;
import model.LogFile;

public class FilePreProcessor {
	private RandomAccessFile reader;
	private LogFile logFile;
	private File xesFile;
	private String headLine;
	private int contentStartPoint;
	
	public FilePreProcessor(String fileName) {
		logFile = new LogFile(fileName);
		xesFile = new File(fileName);
		setReader();
		headLine = "";
		contentStartPoint = 0;
	}
	
	
	private void setReader() {
		try {
			reader = new RandomAccessFile(xesFile, "r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	public LogFile doPreProcessing() {
		System.out.println("<<Start - PreProcess>>");
		getHeadLine();
		System.out.println("<<End getHeader>>");
		getAttribute();
		System.out.println("<<End getAttribute>>");
		System.out.println("***************************");
		
		return getResult();
	}
	
	
	private void getHeadLine() {
		while(true) {
			try {
				String txtLine = reader.readLine();
				
				if(txtLine.contains("<trace>")) {
					logFile.setHeadLine(headLine);
					System.out.println("finish get HeadLine");
					contentStartPoint = (int)reader.getFilePointer() - 8;
					break;
				}
				else {		//로그 파일 본내용이 아닌 메타데이터 부분이라면 헤드라인에 추가
					headLine += txtLine + "\n";
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private void getEventAttribute() {
		String eventScopeType = "";
		String eventScopeKey = "";
		System.out.println("\nEVENT TYPE\t\t\tEVENT KEY");
		try {
			while(true) {
				String txtEvent = reader.readLine();
				
				if( txtEvent.contains("</event>") ) {
					break;
				}
				
				if( txtEvent.contains("key=\"")) {
					eventScopeType = txtEvent.split(" ")[0].trim().substring(1);
					eventScopeKey = txtEvent.split("\"")[1];
					
					System.out.println(eventScopeType + "\t\t\t" + eventScopeKey);
					logFile.addEventScope(new Attribute(eventScopeType, eventScopeKey));
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private void getAttribute() {
		try {
			reader.seek(contentStartPoint);
			String txt;
			String traceScopeType;
			String traceScopeKey;
			System.out.println("\nTRACE TYPE\t\t\tTRACE KEY");
			
			while(true) {
				txt = reader.readLine();
				
				if( txt.contains("key=\"") ) {
					traceScopeKey = txt.split("\"")[1];
					traceScopeType = txt.split(" ")[0].trim().substring(1);
					
					System.out.println(traceScopeType + "\t\t\t" + traceScopeKey);
					logFile.addTraceScope(new Attribute(traceScopeType, traceScopeKey));
				}
				else if( txt.contains("<event>") ) {
					getEventAttribute();
					break;
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private LogFile getResult() {
		return logFile;
	}
}
