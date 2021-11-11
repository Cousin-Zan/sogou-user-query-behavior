package utils;

import org.lionsoul.jcseg.tokenizer.ASegment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.lionsoul.jcseg.tokenizer.core.ADictionary;
import org.lionsoul.jcseg.tokenizer.core.DictionaryFactory;
import org.lionsoul.jcseg.tokenizer.core.IWord;
import org.lionsoul.jcseg.tokenizer.core.JcsegException;
import org.lionsoul.jcseg.tokenizer.core.JcsegTaskConfig;
import org.lionsoul.jcseg.tokenizer.core.SegmentFactory;

/**
 * Created by Administrator on 2016/4/6.
 */
public class AnaylyzerTools {

    public static ArrayList<String> anaylyzerWords(String str) {
        JcsegTaskConfig config = new JcsegTaskConfig(AnaylyzerTools.class.getResource("").getPath() + "jcseg.properties");
        ADictionary dic = DictionaryFactory.createDefaultDictionary(config);
        ArrayList<String> list = new ArrayList<String>();
        ASegment seg = null;
        try {
            seg = (ASegment) SegmentFactory.createJcseg(JcsegTaskConfig.COMPLEX_MODE, new Object[]{config, dic});
        } catch (JcsegException e1) {
            e1.printStackTrace();
        }
        try {
            seg.reset(new StringReader(str));
            IWord word = null;
            while ((word = seg.next()) != null) {
                list.add(word.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }



    public static String readString() {
        String str = "";
        File file = new File("D:/searchname.txt");
        try {
            FileInputStream in = new FileInputStream(file);
            // size 为字串的长度 ，这里一次性读完
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            str = new String(buffer, "utf-8");
        } catch (IOException e) {
            return null;
        }
        return str;
    }



    public static void main(String[] args) {
        String text = readString();
        List<String> list = AnaylyzerTools.anaylyzerWords(text);
        for (String word : list) {
            System.out.println(word);
        }
        System.out.println(list.size());
    }
}