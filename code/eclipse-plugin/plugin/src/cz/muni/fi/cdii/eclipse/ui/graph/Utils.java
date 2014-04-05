package cz.muni.fi.cdii.eclipse.ui.graph;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    static <T> List<T> toCheckedList(@SuppressWarnings("rawtypes") List uncheckedList, 
            Class<T> type) {
        ArrayList<T> result = new ArrayList<>();
        for (Object item : uncheckedList) {
            Class<? extends Object> itemClass = item.getClass();
            if (type.isAssignableFrom(itemClass)) {
                @SuppressWarnings("unchecked")
                T checkedItem = (T) item;
                result.add(checkedItem);
            }
        }
        return result;
    }

}
