import domain.Article;
import domain.utils.Tuple;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.RomanToDecimalConverter;

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

        return (JSONArray) jsonObjectContainer.get("article");
    }

    private static void parseJsonObject(JSONObject jsonObject) {
        try {
            Article article = extractArticleAttributes(jsonObject);

            System.out.println(article);

        } catch (ClassCastException e) {
            System.err.println("An error has occurred while retrieving the JSONObject " + jsonObject);
            e.printStackTrace();
        }
    }

    private static Article extractArticleAttributes(JSONObject jsonObject) {
        String title = extractTitle(jsonObject);
        Long year = extractYear(jsonObject);
        //String url = extractURL(jsonObject);
        String url = "haha yes";
        Tuple<Integer, Integer> pages = extractPages(jsonObject);

        // TODO: How to resolve the dependencies with other entities, like Article.authors & Article.copyPublishedBy.
        return new Article(title,
                year,
                url,
                null,
                pages.getFirstElement(),
                pages.getSecondElement(),
                null);
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

    private static Tuple<Integer, Integer> extractPages(JSONObject jsonObject) {
        // The variable in which the data is extracted to, must be of type Object so that we can use
        // 'instanceof' to determine its type.
        Object pages = jsonObject.get("pages");

        if (pages == null) {
            System.out.println(System.lineSeparator() +
                    "'pages' attribute is missing in " + jsonObject + System.lineSeparator());
            return new Tuple<>();
        }

        int initialPage = 0;
        int finalPage = 0;

        // Attribute "pages" might be of type String if it is a range ("164-168") or
        // if its a range containing letters ("e3-e13") or
        // if its on roman notation ("iii").
        // Otherwise, it can be of Long type if its is only one page ("264").
        if (pages instanceof String) {
            String stringPages = pages.toString();

            if (isInSimpleRangeFormat(stringPages)) {
                initialPage = extractInitialPage(stringPages);
                finalPage = extractFinalPage(stringPages);
            } else if (isInRomanNotation(stringPages)) {
                initialPage = RomanToDecimalConverter.romanToDecimal(stringPages);
                finalPage = initialPage;
            } else {
                return new Tuple<>();
            }
        } else if (pages instanceof Long) {
            initialPage = ((Long) pages).intValue();
            finalPage = initialPage;
        }

        return new Tuple<>(initialPage, finalPage);
    }

    private static boolean isInSimpleRangeFormat(String stringPages) {
        // REGEX: Two numbers separated by '-'
        return stringPages.matches("\\d+-\\d+");
    }

    private static int extractInitialPage(String pages) {
        return Integer.parseInt(pages.substring(0, pages.indexOf("-")));
    }

    private static int extractFinalPage(String pages) {
        // The '+ 1' is because String.substring(int startIndex) includes the char at startIndex.
        return Integer.parseInt(pages.substring(pages.indexOf("-") + 1));
    }

    private static boolean isInRomanNotation(String stringPages) {
        // REGEX: Contains any number.
        return !stringPages.matches(".*[0-9].*");
    }
}
