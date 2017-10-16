package utils;

import org.jblas.DoubleMatrix;

/**
 * @author Qian Shaofeng
 *         created on 2017/10/16.
 */
public class Preprocess {
    public static double[][] minMaxScale(double[][] data){
        DoubleMatrix matrix = new DoubleMatrix(data);
        DoubleMatrix columnMatrix;
        for (int i=0; i < matrix.columns; ++i){
            columnMatrix = matrix.getColumn(i);
            double minVals = columnMatrix.min();
            double maxVals = columnMatrix.max();
            double diff = maxVals - minVals;
            if (diff == 0){
                matrix.putColumn(i, DoubleMatrix.zeros(matrix.rows, 1));
            }
            else {
                matrix.putColumn(i, columnMatrix.sub(minVals).div(diff));
            }

        }
        return matrix.toArray2();
    }

    public static void main(String[] args) {

    }

}
