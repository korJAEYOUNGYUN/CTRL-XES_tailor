package tailor;
/*
 * 트레이스의 개수 N을 기준으로  1~N번째 트레이스와 N+1부터 나머지 트레이스를 각각 다른 파일로 분할
 */

import java.io.File;
import java.io.IOException;


public class TailorByTraceNum extends Tailor {
	private int traceNum;
	private int crntTraceNum;
	
	
	/*
	 * fileName은 인풋으로 주는 xes파일 이름 , traceNum은 분할할 트레이스의 개수
	 */
	public TailorByTraceNum(String fileName) {
		xesFile = new File(fileName);
		crntTraceNum = 0;
		outputFileName = "tailoredbynum";
		setReader();
		headLineData = "";
	}
	
	
	void setCondition(Object condition) {
		traceNum = (int) condition;
	}
	
	
	@Override
	public void doTailoring(Object condition) {
		setCondition(condition);
		getHeader();
		
		System.out.println("Start tailoring...");
		int fileNum = 0;
		crntTraceNum = 0;
		boolean isfinal = false;
		
		while (!isfinal) {
			File outputFile = new File(outputFileName + ++fileNum + ".xes");
			System.out.println("Making " + outputFile.getName() + "...");
			
			setWriter(outputFile);
			try {
				writer.write(headLineData);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
			while(true) {
				try {
					String txtLine = reader.readLine();
					if(txtLine == null) {
						writer.flush();
						writer.close();
						isfinal = true;
						if(crntTraceNum == 0) {
							outputFile.delete();
							System.out.println(outputFile.getName() + " has no body");
							System.out.println(outputFile.getName() + " has been deleted");
							fileNum--;
						}
						else {
							System.out.println(outputFile.getName() + " is made successfully");
						}
						break;
					}
					writer.write(txtLine + "\n");
					
					if(txtLine.contains("</trace>")) {
						crntTraceNum++;
						if(crntTraceNum == traceNum) {
							crntTraceNum = 0;
							writer.write("</log>");
							writer.flush();
							writer.close();
							System.out.println(outputFile.getName() + " is made successfully");
							break;
						}
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("\nTailoring is done");
		System.out.println("Total # of outputFile: " + fileNum);
		close();
	}
	
	
	public static void main(String[] args) {
		TailorByTraceNum t = new TailorByTraceNum("BPI Challenge 2018.xes");
	/*	t.setCondition(500);
		//t.thisIsForTest();
		t.getHeader();
		t.doTailoring();*/
	}
}
