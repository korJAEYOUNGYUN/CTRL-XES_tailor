package interpreter;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import control.InputHandler;
import control.LogFileHandler;
import model.Attribute;
import model.LogFile;
import tailor.*;

/*
 * QueryInterpreter에서 분석한 쿼리를 내용에 맞게 실행함
 * 
 * 20180903
 * SELECT 쿼리 잘 되는지 확인할것
 * selList 적용 방안 생각하기
 */

public class QueryExecutor {
	
	private LogFileHandler logFileHandler;
	private InputHandler inputHandler;

	QueryExecutor() {
		logFileHandler = LogFileHandler.getInstance();
		inputHandler = InputHandler.getInstance();
	}
	
	void doOPEN(String fileName) {
		logFileHandler.setLogFile(fileName);
		logFileHandler.setCrntLogFile(fileName);
	}

	
	void doUSE(String fileName) {
		logFileHandler.setCrntLogFile(fileName);
	}
	
	
	void doSHOW() {
		logFileHandler.showLogList();
	}
	
	
	void doSLICE(int numToSlice) {
		if( logFileHandler.hasCrntLogFile() ) {
			String fileName = logFileHandler.getCrntLogFile().getName();
			TailorByTraceNum slicer = new TailorByTraceNum(fileName);

			slicer.doTailoring(numToSlice);
		}
		else {
			System.out.println("Use logfile first");
		}
	}
	
	
	void doSELECT(Queue<String> selQueue, Queue<String> fromQueue, Queue<String> conditionQueue) {
		if( logFileHandler.hasCrntLogFile() ) {
			Tailor tailor = null;
			LogFile logFile = logFileHandler.getCrntLogFile();
			List<Attribute> scopeList;
			Queue<String> checkedConditionQueue = new LinkedList<>();
			
			if( selQueue.isEmpty() ) {
				System.err.println("selList is empty");
				return;
			}
			
			if( fromQueue.isEmpty() ) {
				System.err.println("fromList is empty");
				return;
			}
			
			String from = fromQueue.poll().toLowerCase();
			if( from.equals("trace") ) {
				tailor = new TailorByTraceAttr(logFile.getName());
				scopeList = logFile.getTraceScope();
			}
			else if( from.equals("event") ) {
				tailor = new TailorByEventAttr(logFile.getName());
				scopeList = logFile.getEventScope();
			}
			else {
				System.err.println("wrong fromList : trace or event");
				return;
			}
			
			for(String sel : selQueue) {
				if( sel.equals("*") ) {
					checkedConditionQueue.add(sel);
				}
				else {
					boolean checked = false;
					
					for(Attribute attr : scopeList) {
						if( attr.getKey().equals(sel) ) {
							checkedConditionQueue.add(sel);
							checked = true;
							break;
						}
					}
					
					if( !checked ) {
						System.err.println("There is no matched scope: " + sel);
						return;
					}
					
				}
			}
			checkedConditionQueue.add("/");
			
			for(String condition : conditionQueue) {
				String operand1 = "";
				String operator = "";
				String operand2 = "";
				for(int i = 0; i < condition.length(); i++) {
					char temp = condition.charAt(i);
					if( temp == '>' || temp == '<' || temp == '=' ) {
						operator += temp;
						operand2 = condition.substring(i + 1);
						break;
					}
					operand1 += temp;
				}
				
				boolean checked = false;
				for(Attribute attr : scopeList) {
					if( attr.getKey().equals(operand1) ) {
						checked = true;
						break;
					}
				}
				if( !checked ) {
					System.err.println("condition err: " + operand1 + " is not in scope");
					return;
				}
				if( operator.equals("") ) {
					System.err.println("condition err: no operator");
				}
				if( operand2.equals("") ) {
					System.err.println("condition err: no operand");
					return;
				}
				checkedConditionQueue.add(operand1);
				checkedConditionQueue.add(operator);
				checkedConditionQueue.add(operand2);
			}
			//System.out.println(checkedConditionQueue);
			
			tailor.doTailoring(checkedConditionQueue);
		}
		else {
			System.out.println("Use logfile first");
		}
	}


	void doH_UNION(String fileName) {
		String fileName1 = logFileHandler.getCrntLogFile().getName();
		String fileName2 = fileName;

		HorizontalUnion horizontalUnion = new HorizontalUnion(fileName1, fileName2);
		horizontalUnion.doUnion();
	}
}
