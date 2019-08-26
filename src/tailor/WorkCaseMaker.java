package tailor;

import java.io.*;
import java.util.List;

public class WorkCaseMaker {
    String fileName;
    File file;
    String outputFileName;

    public WorkCaseMaker(String fileName) {
        this.fileName = fileName;
        file = new File(fileName);
        outputFileName = "workcase_" + fileName;
    }


    public void make() {
        String txtLine;
        String outputLine = "";
        String eventID;
        try {
            FileReader reader = new FileReader(file);
            BufferedReader bufReader = new BufferedReader(reader);
            BufferedWriter bufWriter = new BufferedWriter(new FileWriter(outputFileName));

            while(true) {
                txtLine = bufReader.readLine();
                if( txtLine.contains("/log") )
                    break;
                if( txtLine.contains("<event>") ) {
                    while(true) {
                        txtLine = bufReader.readLine();
                        if(txtLine.contains("concept:name")) {
                            eventID = txtLine.split("\"")[3];
                            outputLine += eventID + "->";
                            break;
                        }
                    }
                }
                if( txtLine.contains("/trace") ) {
                    bufWriter.write(outputLine.substring(0, outputLine.length()-2) + "\n");
                    bufWriter.flush();
                    outputLine = "";
                }

            }
        } catch (Exception e) {

        }

    }

    public static void main(String[] args) {
        WorkCaseMaker w = new WorkCaseMaker("tailoredbyeventattr.xes");
        w.make();
    }
}
