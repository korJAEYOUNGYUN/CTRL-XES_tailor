package tailor;
/*
수직으로 잘린 두 로그 파일을 합치는 것
수직으로 잘렸기 때문에 같은 trace의 다른 event부분을 서로 가지고 있다.
이벤트의 순서를 비교해서 한 trace안에 합치면 댐
 */

import java.io.File;
import java.io.IOException;

public class VerticalUnion extends Union {

    public VerticalUnion(String fileName1, String fileName2) {
        super(fileName1, fileName2);
        outputFileName = "verticalunion";
    }


    public void doUnion() {
        prepare();

        String txtLine2;
        String crntEventFromFile1 = "";
        String crntEventFromFile2 = "";
        boolean eventBit = false;

        try {
            writer.write(headLineData);
            writer.write(txtLine);

            while (true) {
                txtLine = reader1.readLine();
                txtLine2 = reader2.readLine();



                if (txtLine.contains("<event>")) {
                    while (true) {
                        writer.write(txtLine + "\n");

                        txtLine = reader1.readLine();
                        if (txtLine.contains("</event>")) {
                            writer.write(txtLine + "\n");
                            break;
                        }
                    }
                }

                if (txtLine.contains("</trace>")) {
                    while (true) {
                        if(txtLine2.contains("<event>")) {
                            while (true) {
                                writer.write(txtLine2 + "\n");

                                txtLine2 = reader2.readLine();
                                if (txtLine2.contains("</event>")) {
                                    writer.write(txtLine2 + "\n");
                                    break;
                                }
                            }
                        }
                        if(txtLine.contains("</trace>")) {
                            break;
                        }
                    }
                }

                writer.write(txtLine + "\n");

                if (txtLine.contains("</log>")) {
                    break;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String... args) {
        String fileName1 = "";
        String fileName2 = "";

        VerticalUnion verticalUnion = new VerticalUnion(fileName1, fileName2);
        verticalUnion.doUnion();
    }
}