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

/*
 * Trace의 속성을 선택해서 맞는 속성에 해당하는 트레이스들만 따로 추출해 파일을 생성한다.
 */
public class TailorByTraceAttr extends Tailor{
	private int crntTraceNum;
	private String crntTrace;
	private List<String> operand1List;
	private List<String> operand2List;
	private List<String> operatorList;
	private List<String> selList;
	private LogFile logFile;

	
	public TailorByTraceAttr(String fileName) {
		xesFile = new File(fileName);
		crntTraceNum = 0;
		outputFileName = "tailoredbytraceattr";
		setReader();
		headLineData = "";
		
		operand1List = new ArrayList<>();
		operand2List = new ArrayList<>();
		operatorList = new ArrayList<>();
		selList = new ArrayList<>();
		logFile = LogFileHandler.getInstance().getLogFile(fileName);
	}
	
	
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
		int sizeOfQueue = operatorList.size();
		
		crntTraceNum = 0;
		crntTrace = "";
		 
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
				
				if( txtLine.contains("<trace>") ) {
					crntTrace += txtLine + "\n";
				}
				//event태그가 나오면 이제 더이상 비교할 trace의 속성값이 없기때문에 빠른처리를 위해서
				//조건에 부합하는 trace이면 write하고 부합하지 않으면 다음 trace로 넘어간다
				else if(txtLine.contains("<event>") && isMatch < sizeOfQueue) {
					crntTrace = "";
					while(true) {
						txtLine = reader.readLine();
						if(txtLine.contains("</trace>")) {
							isMatch = 0;
							break;
						}
					}
				}
				else if(txtLine.contains("<event>") && isMatch == sizeOfQueue) {		
					while(true) {
						txtLine = reader.readLine();
						if(txtLine.contains("</trace>")) {
							System.out.println(crntTrace.substring(10).trim());
							crntTrace += txtLine + "\n";
							crntTraceNum++;
							writer.write(crntTrace);
							isMatch = 0;
							crntTrace = "";
							break;
						}
						//event 미포함 
						//crntTrace += txtLine + "\n";
					}
				}
				else {
					//조건에 맞는 부분만 포함하도록 변경
					//crntTrace += txtLine + "\n";
					for(String sel : selList) {
						if( sel.equals("*") || txtLine.contains(sel) ) {
							crntTrace += txtLine + "\n";
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
							
							List<Attribute> traceScope = logFile.getTraceScope();
							
							for(Attribute attr : traceScope) {
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
			System.out.println("# of Tailored Traces : " + crntTraceNum);
			System.out.println("Tailoring is done");	
			close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	
	public static void main(String args[]) {
		Tailor t = new TailorByTraceAttr("2000-all-nonoise.xes");
		Queue<String> at = new LinkedList<>();
		
		//selList부분
		at.add("concept:name");
		
		//selList와 condition을 구분하기 위한 기호
		at.add("/");
		
		//condition부분
		at.add("concept:name");
		at.add(">");
		at.add("trace_1");
		at.add("concept:name");
		at.add("<");
		at.add("trace_5");
		
		
		t.doTailoring(at);
	}
}
