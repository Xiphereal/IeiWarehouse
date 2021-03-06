package warehouse.extractors.utils;

import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class SimpleJsonUtils {

    /**
     * If there's a way of doing this more elegant or that is a SimpleJSON or Java standard of doing this,
     * just replace this method with that call.
     */
    public static List<String> convertJsonArrayToStringCollection(JSONArray castedEeAttribute) {
        List<String> convertedCollection = new ArrayList<>();

        for (Object element : castedEeAttribute)
            convertedCollection.add((String) element);

        return convertedCollection;
    }

    public static boolean areAllArrayElementsOfTypeString(JSONArray castedEeAttribute) {
        return castedEeAttribute.stream().allMatch(element -> element instanceof String);
    }
}
