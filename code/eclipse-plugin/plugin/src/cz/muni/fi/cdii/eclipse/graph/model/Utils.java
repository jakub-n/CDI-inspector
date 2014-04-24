package cz.muni.fi.cdii.eclipse.graph.model;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    
    private Utils() {}

    public static <T> List<T> iterableToList(Iterable<T> input) {
        ArrayList<T> result = new ArrayList<>();
        for (T item : input) {
            result.add(item);
        }
        return result;
    }

}
