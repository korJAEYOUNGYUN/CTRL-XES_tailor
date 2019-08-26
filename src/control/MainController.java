package control;

import interpreter.QueryInterpreter;

public class MainController {
	
	private InputHandler inputHandler;
	private QueryInterpreter queryInterpreter;
	
	MainController() {
		inputHandler = InputHandler.getInstance();
		queryInterpreter = new QueryInterpreter();
	}
	
	
	void start() {
		System.out.println("*************************************");
		System.out.println("ctrl.kyonggi.ac.kr");
		System.out.println("*************************************");
		String sentence;
		while(true) {
			System.out.flush();
			System.err.flush();
			sentence = inputHandler.getInput();
			
			if( sentence.equals("quit") ) {
				System.out.println("Quit Tailoring program");
				break;
			}
			
			queryInterpreter.interpret(sentence);
		}
	}
	
	
	public static void main(String[] args) {
		MainController mainController = new MainController();
		mainController.start();
	}
}
