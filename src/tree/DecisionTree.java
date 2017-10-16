package tree;


import java.util.*;

/**
 * @author Qian Shaofeng
 *         created on 2017/10/16.
 */
public class DecisionTree {
    class DataAndLabel{
        double[][] data;
        Object[] labels;

        public DataAndLabel(double[][] data, Object[] labels){
            this.data = data;
            this.labels = labels;
        }
    }

    private double calEntropy(Object[] labels){
        HashMap<Object, Integer> hashMap = new HashMap();
        for (Object ele: labels){
            hashMap.put(ele, hashMap.getOrDefault(ele, 0) + 1);
        }

        double entropy = 0;
        for (Map.Entry<Object, Integer> ele: hashMap.entrySet()){
            double p = ele.getValue() * 1.0 / labels.length;

            entropy += p * Math.log(p) / Math.log(2);
        }

        return -entropy;
    }

    private DataAndLabel splitDataset(double[][] data, Object[] labels, int colIndex, double value){
        ArrayList<double[]> list = new ArrayList<>();
        ArrayList<Object> label_list = new ArrayList<>();
        int colNum = data[0].length;
        for (int i=0; i < data.length; ++i){
            if (data[i][colIndex] == value){
                double[] tmp = new double[colNum - 1];
                int j = 0;
                for (; j < colIndex; ++j){
                    tmp[j] = data[i][j];
                }
                for (; j+1 < colNum; ++j){
                    tmp[j] = data[i][j+1];
                }
                list.add(tmp);
                label_list.add(labels[i]);
            }
        }
        double[][] x_data = new double[list.size()][];
        for (int i=0; i < x_data.length; ++i){
            x_data[i] = list.get(i);
        }
        Object[] y_labels = label_list.toArray();
        return new DataAndLabel(x_data, y_labels);
    }

    private int choosBestFeature(double[][] data, Object[] labels){
        int numFeature = data[0].length;
        int numRows = data.length;
        double baseEntroy = calEntropy(labels);
        double bestInfoGain = 0.0;
        int bestFeature = -1;
        for(int i=0; i < numFeature; ++i){
            Set<Double> featureSet = new HashSet();
            for (int j=0; j < numRows; ++j){
                featureSet.add(data[j][i]);
            }

            Iterator<Double> it = featureSet.iterator();
            double newEntroy = 0;
            while (it.hasNext()){
                DataAndLabel dataAndLabel = splitDataset(data, labels, i, it.next());
                double[][] subData = dataAndLabel.data;
                Object[] subLabels = dataAndLabel.labels;
                double pro = subLabels.length * 1.0 / labels.length;
                newEntroy += pro * calEntropy(subLabels);

            }
            double infoGain = baseEntroy - newEntroy;
            System.out.println(infoGain);
            if (infoGain > bestInfoGain){
                bestInfoGain = infoGain;
                bestFeature = i;
            }
        }
        return bestFeature;
    }

    private Map majority(double[][] data, Object[] labels){
        HashMap<Object, Integer> hashMap = new HashMap<>();
        for (int i=0; i < data.length; ++i){
            hashMap.put(labels[i], hashMap.getOrDefault(labels[i], 0) + 1);
        }

        int max = 0;
        Object featureLabel = null;
        for (Map.Entry<Object, Integer> ele: hashMap.entrySet()){
            int value = ele.getValue();
            if (value > max){
                max = value;
                featureLabel = ele.getKey();
            }
        }
        HashMap result = new HashMap();
        result.put(featureLabel, null);
        return result;
    }

    public Map createTree(double[][] data, Object[] labels, ArrayList<String> featureName){
        HashSet<Object> hashSet = new HashSet<>();
        for (int i=0; i < labels.length; ++i){
            hashSet.add(labels[i]);
        }
        if (hashSet.size() == 1) {
            HashMap singleLabel = new HashMap();
            singleLabel.put(hashSet.iterator().next(), null);
            return singleLabel;
        }
        if (data[0].length == 0){
            return majority(data, labels);
        }

        int bestFeature = choosBestFeature(data, labels);
        Map<String, Map> hashMap = new HashMap<>();
        Map<String, Map> subMap = new HashMap<>();
        hashMap.put(featureName.get(bestFeature), subMap);

        featureName.remove(bestFeature);
        HashSet<Double> colDataSet = new HashSet<>();
        for (int i=0; i < data.length; ++i){
            colDataSet.add(data[i][bestFeature]);
        }

        Iterator<Double> it = colDataSet.iterator();
        while (it.hasNext()){
            double value = it.next();
            DataAndLabel dataAndLabel = splitDataset(data, labels, bestFeature, value);
            double[][] subData = dataAndLabel.data;
            Object[] subLabels = dataAndLabel.labels;

            subMap.put(String.valueOf(value), createTree(subData, subLabels, featureName));
        }

        return hashMap;

    }
    public static void main(String[] args) throws Exception {
//        TrainData trainData = FileUtils.readData("E:\\dataset\\ml_inaction\\Ch02\\datingTestSet.txt");
        double[][] data = {{1, 1}, {1 ,1}, {1, 0}, {0, 1}, {0, 1}};
        Object[] label = {1, 1, 0, 0, 0};

        String[] y_train = new String[label.length];
        for (int i=0; i < label.length; ++i){
            y_train[i] = String.valueOf(label[i]);
        }
        String[] featureName = new String[]{"a", "b"};
        ArrayList<String> list = new ArrayList<>();
        for (String ele: featureName)
            list.add(ele);
        System.out.println(new DecisionTree().createTree(data, label, list));
    }
}
