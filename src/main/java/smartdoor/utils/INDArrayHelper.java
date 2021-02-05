package smartdoor.utils;

import org.nd4j.linalg.api.ndarray.INDArray;

import java.util.List;

public class INDArrayHelper {
    public static INDArray[] listToArray(List<INDArray> list) {
        int listSize = list.size();

        INDArray[] array = new INDArray[listSize];
        for (int i = 0; i < listSize; i ++) {
            array[i] = list.get(i);
        }

        return array;
    }
}
