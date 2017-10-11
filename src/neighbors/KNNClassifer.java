package neighbors;

import com.csvreader.CsvReader;
import org.jblas.DoubleMatrix;
import test.FileUtils;
import test.TrainData;

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


        KNNClassifer knn = new KNNClassifer(1);
        TrainData trainData = FileUtils.readData("E:\\dataset\\ml_inaction\\Ch02\\datingTestSet.txt");
        double[][] data = trainData.train_df;
        double[] label = trainData.label;
        knn.fit(data, label);
        double a = knn.predict(new double[][]{{14488, 7.153469, 1.673904}});
        System.out.println(a);

    }

}
