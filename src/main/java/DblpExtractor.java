import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * See reference: https://howtodoinjava.com/java/library/json-simple-read-write-json-examples/
 */
public class DblpExtractor {

    public static void extractDataIntoWarehouse() {
        try (FileReader fileReader = new FileReader("src/main/resources/dblp/dblp-solo-article-1.json")) {
            //Read JSON file
            JSONParser jsonParser = new JSONParser();
            JSONObject entireJsonFile = (JSONObject) jsonParser.parse(fileReader);
            JSONObject jsonObjectContainer = (JSONObject) entireJsonFile.get("dblp");
            JSONArray articles = (JSONArray) jsonObjectContainer.get("article");

            articles.forEach(article -> parseJsonObject((JSONObject) article));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseJsonObject(JSONObject jsonObject) {
        try {
            String title = (String) jsonObject.get("title");
            Long year = (Long) jsonObject.get("year");
            // TODO: Check whether "ee" exists to avoid a NullReference on retrieving "content".
            // String url = (String) ((JSONObject) jsonObject.get("ee")).get("content");

            // TODO: Check whether the "pages" is a Long, containing the total number of pages, or a String,
            //  having the initial-final pages.
            String pages = (String) jsonObject.get("pages");

            if (pages != null) {
                String initialPages = extractInitialPages(pages);
                System.out.println(initialPages);

                String finalPages = extractFinalPages(pages);
            }

        } catch (ClassCastException e) {
            System.err.println("An error has ocurred while retrieving the JSONObject " + jsonObject.toString());
            e.printStackTrace();
        }
    }

    private static String extractInitialPages(String pages) {
        return pages.substring(0, pages.indexOf("-"));
    }

    private static String extractFinalPages(String pages) {
        // The '+ 1' is because String.substring(int startIndex) includes the char at startIndex.
        return pages.substring(pages.indexOf("-") + 1);
    }
}
