package control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

public class InputHandler {
	private Scanner scanner;
	private Map<String, String> aliasList;
	private static InputHandler instance = new InputHandler();
	private LogFileHandler logFileHandler = LogFileHandler.getInstance();
	
	private InputHandler() {
		scanner = new Scanner(System.in);
		aliasList = new HashMap<>();
	}
	
	
	public static InputHandler getInstance() {
		return instance;
	}

	
	public String getInput() {
		String sentence = "";
		String stringLine = "";
		
		while(true) {
			if( logFileHandler.hasCrntLogFile() ) {
				System.out.print(logFileHandler.getCrntLogFile().getName());
			}
			System.out.print("> ");
			stringLine = scanner.nextLine();
			
			if( !aliasList.isEmpty() ) {
				StringTokenizer tokenizer = new StringTokenizer(stringLine);
				while( tokenizer.hasMoreTokens() ) {
					String token = tokenizer.nextToken();
					for(String alias : aliasList.keySet()) {
						if( token.equals(alias) ) {
							stringLine.replace(token, aliasList.get(alias));
						}
					}
				}
			}
		
			if( stringLine.contains(";") ) {
				sentence += " " + stringLine.substring(0, stringLine.indexOf(";")).trim();
				break;
			}
			sentence += " " + stringLine.trim();
		}
		
		return sentence.trim();
	}
}