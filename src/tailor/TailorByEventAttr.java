package tailor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import control.LogFileHandler;
import model.Attribute;
import model.LogFile;

public class TailorByEventAttr extends Tailor {
	private int crntEventNum;
	private String crntTrace;
	private String crntEvent;
	private List<String> operand1List;
	private List<String> operand2List;
	private List<String> operatorList;
	private List<String> selList;
	private LogFile logFile;
	
	
	public TailorByEventAttr(String fileName) {
		xesFile = new File(fileName);
		crntEventNum = 0;
		outputFileName = "tailoredbyeventattr";
		setReader();
		headLineData = "";
		logFile = LogFileHandler.getInstance().getLogFile(fileName);
		operand1List = new ArrayList<>();
		operand2List = new ArrayList<>();
		operatorList = new ArrayList<>();
		selList = new ArrayList<>();
	}
	
	
	@SuppressWarnings("unchecked")
	void setCondition(Object condition) {
		Queue<String> conditionQueue;
		conditionQueue = (Queue<String>) condition;
		
		while(true) {
			String elem = conditionQueue.poll();
			if( elem.equals("/") )
				break;
			selList.add(elem);
		}
		
		int size = conditionQueue.size();
		
		for(int i = 0; i < size; i++) {
			if( i % 3 == 0 )
				operand1List.add(conditionQueue.poll());
			else if( i % 3 == 1 )
				operatorList.add(conditionQueue.poll());
			else
				operand2List.add(conditionQueue.poll());
		}
	}
	
	
	@Override
	public void doTailoring(Object condition) {
		setCondition(condition);
		getHeader();
		
		System.out.println("Start tailoring...");
		File outputFile = new File(outputFileName + ".xes");
		System.out.println("Making " + outputFile.getName() + "...\n");
		
		int isMatch = 0;
		boolean eventBit = false;
		crntEventNum = 0;
		crntTrace = "";
		crntEvent = "";
		int sizeOfQueue = operatorList.size();
		
		setWriter(outputFile);
		try {
			writer.write(headLineData);

			while(true) {

				if(txtLine == null) {
					writer.write("</log>");
					writer.flush();
					writer.close();
					break;
				}
				
				if( txtLine.contains("</trace>") ) {
					crntTrace += txtLine + "\n";
					writer.write(crntTrace);
					crntTrace = "";
				}
				
				else if( txtLine.contains("<event>") ) {
					eventBit = true;
					crntEvent += txtLine + "\n";
				}
				
				//trace 부분일 경우 
				else if( !eventBit ) {
					crntTrace += txtLine + "\n";
				}
				//하나의 event가 끝났는데 조건에 부합하지 않는 event일 경우
				else if( txtLine.contains("</event>") && isMatch < sizeOfQueue ) {
					crntEvent = "";
					isMatch = 0;
					eventBit = false;
				}
				//하나의 event가 끝났는데 조건에 부합할 경우
				else if( txtLine.contains("</event>") && isMatch == sizeOfQueue ) {
					System.out.println(crntEvent.substring(10));
					crntEvent += txtLine + "\n";
					crntTrace += crntEvent;
					crntEvent = "";
					isMatch = 0;
					eventBit = false;
					crntEventNum++;
				}
				//event 부분일 경우
				else {
					//selList에 맞는 부분만 넣기로 수정
					/*crntEvent += txtLine + "\n";*/
					for(String sel : selList) {
						if( sel.equals("*") || txtLine.contains(sel) ) {
							crntEvent += txtLine + "\n";
						}
					}
					
					
					for(int i = 0; i < sizeOfQueue; i++) {
						String operand1 = operand1List.get(i);
						String operator = operatorList.get(i);
						String operand2 = operand2List.get(i);
						
						if( txtLine.contains(operand1) ) {
							String crntAttrValue = "";
							int numInCrntAttr;
							float floatInCrntAttr;
							int numInOperand2;
							float floatInOperand2;
							
							String token = txtLine.split(" ")[2];
							crntAttrValue = token.substring(7, token.length() - 3);
							List<Attribute> eventScope = logFile.getEventScope();
							
							for(Attribute attr : eventScope) {
								if ( attr.getKey().equals(operand1) ) {
									if( attr.getType().equals("int") ) {
										numInCrntAttr = Integer.parseInt(crntAttrValue);
										numInOperand2 = Integer.parseInt(operand2);
										
										if ( operator.equals(">") ) {
											if( numInCrntAttr > numInOperand2 )
												isMatch++;
											break;
										}
										else if ( operator.equals("<") ) {
											if( numInCrntAttr < numInOperand2 )
												isMatch++;
											break;
										}
									}
									else if( attr.getType().equals("float") ) {
										floatInCrntAttr = Float.parseFloat(crntAttrValue);
										floatInOperand2 = Float.parseFloat(operand2);
										
										if ( operator.equals(">") ) {
											if( floatInCrntAttr > floatInOperand2 )
												isMatch++;
											break;
										}
										else if ( operator.equals("<") ) {
											if( floatInCrntAttr < floatInOperand2 )
												isMatch++;
											break;
										}
									}
								}
							}
							
							switch (operator) {
							
							case ">" :
								break;
								
							case "<" :
								break;
								
							case "=" :
								if( crntAttrValue.matches(operand2) )
									isMatch++;
								break;
							}
						}
					}
				}
				
				txtLine = reader.readLine();
			}
			
			System.out.println("\n" + outputFileName + " is made successfully");
			System.out.println("# of Tailored Events : " + crntEventNum);
			System.out.println("Tailoring is done");	
			close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	
	public static void main(String args[]) {
		TailorByEventAttr t = new TailorByEventAttr("2000-all-nonoise.xes");
		Queue<String> at = new LinkedList<>();
		
		//selList 부분
		at.add("*");
		
		//selList와 condition 구분용 기호
		at.add("/");
		
		//condition 부분
		at.add("concept:name");
		at.add("=");
		at.add("ST");
		
		t.doTailoring(at);
	}
}
