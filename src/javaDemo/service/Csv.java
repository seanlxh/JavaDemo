package javaDemo.service;

import com.csvreader.*;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
//读取CSV文件的工具类
public class Csv {

    public void readeCsv(ArrayList<String> funtions, ArrayList<String> judges, HashMap<String, Integer> mp, String fileAddress) {
        try {

            ArrayList<String[]> csvList = new ArrayList<String[]>(); //用来保存数据
            String csvFilePath = fileAddress;
            CsvReader reader = new CsvReader(csvFilePath, ',', Charset.forName("utf-8"));    //一般用这编码读就可以了

            //reader.readHeaders(); // 跳过表头   如果需要表头的话，不要写这句。

            while (reader.readRecord()) { //逐行读入除表头的数据
                csvList.add(reader.getValues());
            }
            reader.close();

            for (int row = 0; row < csvList.size(); row++) {

                funtions.add(csvList.get(row)[1]); //取得第row行第0列的数据
                int tmp = 0;
                if (mp.get(csvList.get(row)[1]) != null) {
                    tmp = mp.get(csvList.get(row)[1]);
                }
                mp.put(csvList.get(row)[1], tmp + 1);
                judges.add(csvList.get(row)[2]);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    public ArrayList<String> readLog(String fileAddress) {
        ArrayList<String> result = new ArrayList<>();
        try {
            ArrayList<String[]> csvList = new ArrayList<String[]>(); //用来保存数据
            String csvFilePath = fileAddress;
            CsvReader reader = new CsvReader(csvFilePath, ',', Charset.forName("utf-8"));    //一般用这编码读就可以了

           // reader.readHeaders(); // 跳过表头   如果需要表头的话，不要写这句。

            while (reader.readRecord()) { //逐行读入除表头的数据
                csvList.add(reader.getValues());
            }
            reader.close();

            for (int row = 0; row < csvList.size(); row++) {
                String[] sentence = csvList.get(row);
                String tmp = "";
                for(int i = 0; i < sentence.length ; i ++){
                    tmp += sentence[i] + ",";
                }
                result.add(tmp);
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }
        return result;

    }

}