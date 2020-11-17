import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

/**
 * See reference: https://howtodoinjava.com/java/library/json-simple-read-write-json-examples/
 */
public class DblpExtractor {

    public static void extractDataIntoWarehouse() {
        try (FileReader fileReader = new FileReader("src/main/resources/dblp/dblp-solo-article-1.json")) {

            JSONArray articles = getArticlesFromJson(fileReader);

            articles.forEach(article -> parseJsonObject((JSONObject) article));

        } catch (Exception e) {
            System.err.println("An error has occurred while extracting data in " + DblpExtractor.class.getName());
            e.printStackTrace();
        }
    }

    // TODO: Get the articles from an interface passed by argument, so that we can later
    //  change the data source (from a file to the API REST request) by creating a new
    //  class that implements that interface.
    private static JSONArray getArticlesFromJson(FileReader fileReader) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject entireJsonFile = (JSONObject) jsonParser.parse(fileReader);
        JSONObject jsonObjectContainer = (JSONObject) entireJsonFile.get("dblp");
        JSONArray articles = (JSONArray) jsonObjectContainer.get("article");

        return articles;
    }

    private static void parseJsonObject(JSONObject jsonObject) {
        try {
            String title = extractTitle(jsonObject);
            Long year = extractYear(jsonObject);
            String url = extractURL(jsonObject);
            String pages = extractPages(jsonObject);

        } catch (ClassCastException e) {
            System.err.println("An error has occurred while retrieving the JSONObject " + jsonObject);
            e.printStackTrace();
        }
    }

    private static String extractTitle(JSONObject jsonObject) {
        return (String) jsonObject.get("title");
    }

    private static Long extractYear(JSONObject jsonObject) {
        return (Long) jsonObject.get("year");
    }

    private static String extractURL(JSONObject jsonObject) {
        JSONObject ee = (JSONObject) jsonObject.get("ee");

        // Check whether "ee" exists to avoid a NullReference on retrieving "content".
        if (ee.isEmpty()) {
            System.out.println("'ee' attribute is missing in " + jsonObject);
            return "";
        }

        return (String) ee.get("content");
    }

    private static String extractPages(JSONObject jsonObject) {
        // The variable in which the data is extracted to, must be of type Object so that we can use
        // 'instanceof' to determine if its an exact page number ("264", of type Long) or a range
        // ("164-168", of type String).
        Object pages = jsonObject.get("pages");

        if (pages == null) {
            System.out.println("'pages' attibute is missing in " + jsonObject);
            return "";
        }

        String initialPages = "null";
        String finalPages = "null";

        // Variable "pages" might be a String if it is more than one, but a Long if it is only one page.
        if (pages instanceof String) {
            String stringPages = pages.toString();

            initialPages = extractInitialPages(stringPages);
            finalPages = extractFinalPages(stringPages);
        } else if (pages instanceof Long) {
            initialPages = pages.toString();
            finalPages = pages.toString();
        }

        return "TODO: Return a tuple of init-final pages";
    }

    private static String extractInitialPages(String pages) {
        return pages.substring(0, pages.indexOf("-"));
    }

    private static String extractFinalPages(String pages) {
        // The '+ 1' is because String.substring(int startIndex) includes the char at startIndex.
        return pages.substring(pages.indexOf("-") + 1);
    }
}
