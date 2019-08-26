package tailor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/*
 * Tailor클래스들이 상속하는 클래스
 * 유도클래스들의 공통 기능을 가지고 있고 
 * doTailoring 메소드는 유도클래스에서 직접 구현
 */
public abstract class Tailor {
	protected BufferedReader reader;
	protected BufferedWriter writer;
	protected File xesFile;
	protected String outputFileName;
	protected String headLineData;
	protected String txtLine;

	
	//BufferedReader 인스턴스 생성용
	void setReader() {
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(xesFile)));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	
	//BufferedWriter 인스턴스 생성용
	void setWriter(File outputFile) {
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	
	void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
		//System.out.println("Current outputFileName : " + outputFileName);
	}
	
	
	void close() {
		try {
			reader.close();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/*
	 * xes파일에서 트레이스 태그가 처음 나오기전에 있는 정보들을 헤더라고 표현
	 * 변수 headLineData에 저장됨
	 */
	public void getHeader() {
		while(true) {
			try {
				txtLine = reader.readLine();
				if(txtLine.contains("<trace>")) {
					break;
				}
				headLineData += txtLine + "\n";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	abstract void setCondition(Object condition);
	
	public abstract void doTailoring(Object condition);
}
