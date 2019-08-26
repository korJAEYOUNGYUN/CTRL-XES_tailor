package tailor;

import java.io.*;

public abstract class Union {
    File file1;
    File file2;
    BufferedReader reader1;
    BufferedReader reader2;
    BufferedWriter writer;
    String outputFileName;
    String headLineData;
    String txtLine;


    public Union(String fileName1, String fileName2) {
        file1 = new File(fileName1);
        file2 = new File(fileName2);
        setReader();

        headLineData = "";
    }


    public void getHeader() {
        while(true) {
            try {
                txtLine = reader1.readLine();

                if(txtLine.contains("<trace>")) {
                    break;
                }
                headLineData += txtLine + "\n";
                reader2.readLine();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    //BufferedReader 인스턴스 생성용
    void setReader() {
        try {
            reader1 = new BufferedReader(new InputStreamReader(new FileInputStream(file1)));
            reader2 = new BufferedReader(new InputStreamReader(new FileInputStream(file2)));
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


    void close() {
        try {
            reader1.close();
            reader2.close();
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public abstract void doUnion();

    void prepare() {
        getHeader();

        System.out.println("Start union...");
        File outputFile = new File(outputFileName + ".xes");
        System.out.println("Making " + outputFile.getName() + "...\n");

        setWriter(outputFile);
    }
}
