import com.mysql.cj.xdevapi.JsonArray;
import domain.Article;
import domain.Copy;
import domain.Person;
import domain.utils.Tuple;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.RomanToDecimalConverter;
import utils.SimpleJsonUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ieeeExtractor {
    public static void extractDataIntoWarehouse() {
        try (FileReader fileReader = new FileReader("src/main/resources/ieee/ieeeXplore_2018-2020.json")) {

            JSONArray articles = getArticlesFromJson(fileReader);

            articles.forEach(article -> parseJsonObject((JSONObject) article));

        } catch (Exception e) {
            System.err.println("An error has occurred while extracting data in " + ieeeExtractor.class.getName());
            e.printStackTrace();
        }
    }

    private static JSONArray getArticlesFromJson(FileReader fileReader) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject entireJsonFile = (JSONObject) jsonParser.parse(fileReader);
        JSONArray jsonObjectContainer = (JSONArray) entireJsonFile.get("articles");

        return jsonObjectContainer;
    }

    private static void parseJsonObject(JSONObject jsonObject) {
        try {
            Article article = extractArticleAttributes(jsonObject);
            List<Person> person = extractAuthors(jsonObject);
            Copy copy = extractCopyAttributes(jsonObject);
        } catch (ClassCastException e) {
            System.err.println("An error has occurred while retrieving the JSONObject " + jsonObject);
            e.printStackTrace();
        }
    }

    private static Article extractArticleAttributes(JSONObject jsonObject) {
        String title = extractTitle(jsonObject);
        Long year = extractYear(jsonObject);
        String url = extractURL(jsonObject);
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

    private static String extractURL(JSONObject jsonObject) {
        // The variable in which the data is extracted to, must be of type Object so that we can use
        // 'instanceof' to determine its type.
        String pdf_url = jsonObject.get("pdf_url").toString();

        return pdf_url;
    }

    /**
     * As reference for the edge cases see: https://github.com/Xiphereal/ProyectoIEI/pull/1#issuecomment-730471867
     */
    private static List<Person> extractAuthors(JSONObject jsonObject) {

        JSONObject authors = (JSONObject) jsonObject.get("authors");
        Object authorList = authors.get("authors");

        if (authorList == null) {
            System.out.println(System.lineSeparator() +
                    "'author' attribute is missing in " + jsonObject + System.lineSeparator());

            return null;
        }

        if (authorList instanceof JSONArray) {
            JSONArray castedAuthorAttribute = (JSONArray) authorList;

            List<Person> parsedAuthors = new ArrayList<>();
            int i;
            for (Object element : castedAuthorAttribute)
                parsedAuthors.add(extractPersonAttributes(((JSONObject) element).get("full_name").toString()));

            return parsedAuthors;
        }

        System.out.println(System.lineSeparator() + "The data structure for attribute 'author' is not considered. " +
                "The author collection will be set to null" + System.lineSeparator());

        return null;
    }


    private static Copy extractCopyAttributes(JSONObject jsonObject) {
        Integer volume = extractVolume(jsonObject);
        Integer number = extractNumber(jsonObject);
        Integer month = extractMonth(jsonObject);

        return new Copy(volume, number, month, null, null);
    }
    //TODO:Resolve roman numbers error
    private static Tuple<Integer, Integer> extractPages(JSONObject jsonObject) {
        // The variable in which the data is extracted to, must be of type Object so that we can use
        // 'instanceof' to determine its type.
        int initialPage = Integer.parseInt(jsonObject.get("start_page").toString());
        int finalPage =  Integer.parseInt(jsonObject.get("end_page").toString());;
        return new Tuple<>(initialPage, finalPage);
    }

    /**
     * @param author The name is the first encountered word, the surname the second.
     */
    private static Person extractPersonAttributes(String author) {
        // Split the string using spaces as separators.
        String[] splitAuthor = author.split(" ");

        String name = splitAuthor[0];
        String surname = splitAuthor.length > 1 ? splitAuthor[1] : null;

        return new Person(name, surname, null);
    }
    //TODO:Volume showing as String (Example: "PP")
    private static Integer extractVolume(JSONObject jsonObject) {
        // The variable in which the data is extracted to, must be of type Object so that we can use
        // 'instanceof' to determine its type.
        Object volume = jsonObject.get("volume");

        if (volume instanceof Long) {
            return ((Long) volume).intValue();
        }

        return null;
    }



    /**
     * Only considers attribute 'number' being a plain Integer number.
     *
     * @return Null if doesn't fit a considered edge case, the Copy number if it does.
     */
    private static Integer extractNumber(JSONObject jsonObject) {
        // The variable in which the data is extracted to, must be of type Object so that we can use
        // 'instanceof' to determine its type.
        Integer number = Integer.parseInt(jsonObject.get("article_number").toString());

        return number;
    }

    /**
     * Only considers attribute 'mdate' being a String separated by '-' with the month in the middle "x-mm-x".
     *
     * @return Null if doesn't fit a considered edge case, the Copy month if it does.
     */
    private static Integer extractMonth(JSONObject jsonObject) {
        Object mdate = jsonObject.get("mdate");

        if (mdate instanceof String) {
            String castedMdate = (String) mdate;

            String[] splitMdate = castedMdate.split("-");

            return splitMdate.length > 1 ? Integer.valueOf(splitMdate[1]) : null;
        }

        return null;
    }

    private static boolean isInSimpleRangeFormat(String stringPages) {
        // REGEX: Two numbers separated by '-'
        return stringPages.matches("\\d+-\\d+");
    }
    private static String extractTitle(JSONObject jsonObject) {
        return (String) jsonObject.get("publication_title");
    }

    private static Long extractYear(JSONObject jsonObject) {
        return (Long) jsonObject.get("publication_year");
    }
}
