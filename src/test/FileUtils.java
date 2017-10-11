package test;

import com.csvreader.CsvReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Qian Shaofeng
 *         created on 2017/10/11.
 */
public class FileUtils {
    public static TrainData readData(String fileName) throws Exception{
        String line;
        String[] eles;
        double[] doubleEle;
        int labelIndex = 0;

        HashMap<String, Integer> hashMap = new HashMap<>();
        List<double[]> datalist = new ArrayList<>();
        List<Integer> ylist = new ArrayList<>();

        CsvReader csvReader = new CsvReader(fileName);


        while(csvReader.readRecord()){
            line = csvReader.getRawRecord();
            eles = line.split("\\t");
            doubleEle = new double[eles.length-1];
            int i=0;
            for (; i < eles.length-1; ++i){
                doubleEle[i] = Double.valueOf(eles[i]);
            }
            if (hashMap.getOrDefault(eles[i], -1) == -1){
                hashMap.put(eles[i], labelIndex++);
            }
            System.out.println(hashMap.get(eles[i]));
            ylist.add(hashMap.get(eles[i]));
            datalist.add(doubleEle);
        }
        double[][] data = new double[datalist.size()][];
        double[] label = new double[ylist.size()];
        for (int i=0; i < datalist.size(); ++i){
            data[i] = datalist.get(i);
            label[i] = (double) ylist.get(i);
        }

        for (int i=0; i < data.length; ++i){
            for (int j=0; j < data[0].length; ++j){
                System.out.print(data[i][j]+" ");
            }
            System.out.println();
        }
        for (int i=0; i < data.length; ++i){
            System.out.println(label[i]);
        }

        TrainData trainData = new TrainData();
        trainData.train_df = data;
        trainData.label = label;
        trainData.labelMap = hashMap;
        return trainData;
    }
}
