package utils;

import java.io.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/6.
 */
public class netAnalyzer {

    public static ArrayList<String> anaylyzerWords() {
        ArrayList<String> list = new ArrayList<String>();
        try {
            String strFile = "D:\\cliurl.txt";
            File file = new File(strFile);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String strLine = null;
            int lineCount = 1;
            while (null != (strLine = bufferedReader.readLine())) {

                java.net.URL url = new java.net.URL("http://" + strLine);

                String host = url.getHost();
                list.add(host);
//                System.out.println("第[" + lineCount + "]行数据:[" + host + "]");
                lineCount++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public static void main(String[] args) throws MalformedURLException {
        System.out.println(netAnalyzer.anaylyzerWords());
    }
}