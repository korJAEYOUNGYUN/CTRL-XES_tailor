package tailor;
/*
수평으로 잘린 두 로그 파일을 합치는 것
수평으로 잘렸기 때문에 서로 다른 trace를 가지고 있고 그냥 합치면 됨
 */

import java.io.*;

public class HorizontalUnion extends Union {

    public HorizontalUnion(String fileName1, String fileName2) {
        super(fileName1, fileName2);
        outputFileName = "horizontalunion";
    }


    public void doUnion() {
        prepare();

        try {
            writer.write(headLineData);
            writer.write(txtLine);
            while (true) {
                txtLine = reader1.readLine();

                if ( txtLine.contains("</log>") ) {
                    break;
                }

                writer.write(txtLine + "\n");
            }

            while (true) {
                txtLine = reader2.readLine();


                if ( txtLine.contains("</log>") ) {
                    writer.write(txtLine);
                    break;
                }

                writer.write(txtLine + "\n");
            }

            System.out.println("\n" + outputFileName + " is made successfully");
            System.out.println("Union is done");
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static  void main(String... args) {
        String fileName1 = "tailoredbynum1.xes";
        String fileName2 = "tailoredbynum2.xes";

        HorizontalUnion h = new HorizontalUnion(fileName1, fileName2);
        h.doUnion();
    }
}