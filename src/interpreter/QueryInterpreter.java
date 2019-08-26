package interpreter;

import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

/*
 * 쿼리를 입력받아 적절한 쿼리인지 검사하고 적절한 쿼리이면 쿼리에 해당하는 명령을 실행
 */
/*
 * 명령어별로 구문검사 하는 거 구현해야함
 * 구문검사 후 실제 명령으로 바꾸는거 해야함
 */


public class QueryInterpreter {
	
	QueryExecutor queryExecutor;
	String query;
	
	public QueryInterpreter() {
		queryExecutor = new QueryExecutor();
	}
	
	
	public void interpret(String query) {
		this.query = query;
		syntacticCheck();
	}
	
	
	private boolean syntacticCheck() {
		boolean result = true;
		StringTokenizer tokenizer = new StringTokenizer(query);
		String instructionInQuery = null;
		
		if(tokenizer.hasMoreTokens()) {
			instructionInQuery = tokenizer.nextToken();
			instructionInQuery = instructionInQuery.toLowerCase();
			
			switch (instructionInQuery) {
			case "open" :
				parseForOPEN(tokenizer);
				break;
			case "use" :
				parseForUSE(tokenizer);
				break;
			case "show" :
				parseForSHOW(tokenizer);
				break;
			case "select" :
				parseForSELECT(tokenizer);
				break;
			case "slice" :
				parseForSLICE(tokenizer);
				break;
			case "hunion" :
				parserForH_UNION(tokenizer);
				break;
			default :
				System.err.println("syntax error: instruction mismatched - " + instructionInQuery);
			}
		}
		
		return result;
	}
	
	
	private void parseForOPEN(StringTokenizer tokenizer) {
		if(!tokenizer.hasMoreTokens()) {
			System.err.println("syntax error: filename is required");
			return;
		}
		
		String fileName = tokenizer.nextToken(";").trim();
		
		queryExecutor.doOPEN(fileName);
	}

	
	private void parseForUSE(StringTokenizer tokenizer) {
		if(!tokenizer.hasMoreTokens()) {
			System.err.println("syntax error: filename is required");
			return;
		}
		String fileName = tokenizer.nextToken(";").trim();
		
		queryExecutor.doUSE(fileName);
	}
	
	
	private void parseForSHOW(StringTokenizer tokenizer) {
		if(tokenizer.hasMoreTokens()) {
			System.err.println("syntax error: too many operators");
			return;
		}
		
		queryExecutor.doSHOW();
	}
	
	
	private void parseForSELECT(StringTokenizer tokenizer) {
		Queue<String> selQueue = new LinkedList<>();
		Queue<String> fromQueue = new LinkedList<>();
		Queue<String> conditionQueue = new LinkedList<>();
		String token = null;
		StringTokenizer tempTokenizer = null;

		while(tokenizer.hasMoreTokens()) {
			token = tokenizer.nextToken();
			if( token.toLowerCase().equals("from") )
				break;
			tempTokenizer = new StringTokenizer(token, ",");
			String operand1 = null;
			while(tempTokenizer.hasMoreTokens()) {
				operand1 = tempTokenizer.nextToken();
				selQueue.add(operand1);
			}
		}
		
		while(tokenizer.hasMoreTokens()) {
			token = tokenizer.nextToken();
			if( token.toLowerCase().equals("where") )
				break;
			tempTokenizer = new StringTokenizer(token, ",");
			String operand2 = null;
			while(tempTokenizer.hasMoreTokens()) {
				operand2 = tempTokenizer.nextToken();
				fromQueue.add(operand2);
			}
		}
		
		while(tokenizer.hasMoreTokens()) {
			token = tokenizer.nextToken().trim();
			String[] conditions = token.split("AND|and");
			for(String condition : conditions) {
				conditionQueue.add(condition.trim());
			}
		}
		
		queryExecutor.doSELECT(selQueue, fromQueue, conditionQueue);
	}
	
	
	private void parseForSLICE(StringTokenizer tokenizer) {
		if(!tokenizer.hasMoreTokens()) {
			System.err.println("syntax error: number is required");
			return;
		}
		String numString = tokenizer.nextToken();
		int numToSlice = Integer.valueOf(numString);
		
		queryExecutor.doSLICE(numToSlice);
	}


	private void parserForH_UNION(StringTokenizer tokenizer) {
		if(!tokenizer.hasMoreTokens()) {
			System.err.println("syntax error: filename is required");
			return;
		}
		String fileName = tokenizer.nextToken(";").trim();

		queryExecutor.doH_UNION(fileName);
	}
	
	
/*	public static void main(String[] args) {
		QueryInterpreter q = new QueryInterpreter();
		q.interpret("Select 1,2 from 2000,1000 wHere traceid = \"trace01\" AND tracef = \"11\"");
	}*/
}
