package neighbors;

import com.csvreader.CsvReader;
import org.jblas.DoubleMatrix;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Qian Shaofeng
 *         created on 2017/10/11.
 */
public class KNNClassifer {
    int k = 3;
    DoubleMatrix x_train;
    DoubleMatrix y_train;
    DoubleMatrix x_test;

    public KNNClassifer(int k){
        this.k = k;
    }

    public void fit(double[][] x, double[] y){
        x_train = new DoubleMatrix(x);
        y_train = new DoubleMatrix(y);
        System.out.println(y_train.rows + " "+y_train.columns);
        System.out.println(x_train.rows + " "+x_train.columns);
    }

    public double predict(double[][] x){
        x_test = new DoubleMatrix(x);
        System.out.println(x_test.rows +" "+x_test.columns);
        DoubleMatrix diffMatrix = x_train.subiRowVector(x_test);
        DoubleMatrix sqDiffMatrix = diffMatrix.mul(diffMatrix);
        DoubleMatrix distance = sqDiffMatrix.rowSums();

        for (int i=0; i < distance.columns; ++i){
            distance.put(0, i, Math.pow(distance.get(0, i), 0.5));
        }
        int[] indexSort = distance.sortingPermutation();
        
        HashMap<Double, Integer> countMap = new HashMap<>();
        for (int i=0; i < k; ++i){
            System.out.println(indexSort[i]);
            int num = countMap.getOrDefault(y_train.get(indexSort[i], 0), 0);
            countMap.put(y_train.get(indexSort[i], 0), num+1);
        }

        int max=0;
        double maxLabel = -1;
        for (Map.Entry ele: countMap.entrySet()){
            int value = (int) ele.getValue();
            if (value > max){
                maxLabel = (double) ele.getKey();
            }
        }
        return maxLabel;
    }

    public static void main(String[] args) throws Exception {
        CsvReader csvReader = new CsvReader("E:\\dataset\\ml_inaction\\Ch02\\datingTestSet.txt");
        String line;
        String[] eles;
        double[] doubleEle;
        int labelIndex = 0;
        HashMap<String, Integer> hashMap = new HashMap<>();
        List<double[]> list = new ArrayList<>();
        List<Integer> ylist = new ArrayList<>();
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
            list.add(doubleEle);
        }
        double[][] data = new double[list.size()][];
        double[] label = new double[ylist.size()];
        for (int i=0; i < list.size(); ++i){
            data[i] = list.get(i);
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

        KNNClassifer knn = new KNNClassifer(1);
        knn.fit(data, label);
        double a = knn.predict(new double[][]{{14488, 7.153469, 1.673904}});
        System.out.println(a);

    }

}
