package warehouse.extractors;

import domainModel.Article;
import domainModel.Copy;
import domainModel.Magazine;
import domainModel.Person;
import domainModel.utils.Tuple;
import domainModel.utils.YearRange;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import warehouse.extractors.utils.RomanToDecimalConverter;
import warehouse.extractors.utils.SimpleJsonUtils;
import warehouse.persistence.dataAccessObjects.ArticleDAO;
import warehouse.restService.HttpRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * See reference: https://howtodoinjava.com/java/library/json-simple-read-write-json-examples/
 */
public class DblpExtractor {
    private static final String BASE_URL_REQUEST_TO_WRAPPER = "http://localhost:8082/extract";

    public static void extractDataIntoWarehouse(YearRange yearRange, int maxPublications) {
        String requestToWrapper = BASE_URL_REQUEST_TO_WRAPPER +
                "?startYear=" + yearRange.getStartYear() +
                "&endYear=" + yearRange.getEndYear() +
                "&maxPublications=" + maxPublications;

        try {
            String retrievedJsonFromDatasource = HttpRequest.GET(requestToWrapper);

            JSONArray articles = getArticlesFromJson(retrievedJsonFromDatasource);

            articles.forEach(article -> parseJsonObject((JSONObject) article, yearRange));

        } catch (IOException | InterruptedException | ParseException e) {
            System.err.println("An error has occurred while extracting data in " + DblpExtractor.class.getName());
            e.printStackTrace();
        }
    }

    private static JSONArray getArticlesFromJson(String json) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject entireJsonFile = (JSONObject) jsonParser.parse(json);
        JSONArray jsonRootElement = (JSONArray) entireJsonFile.get("jsonString");

        return jsonRootElement;
    }

    private static void parseJsonObject(JSONObject jsonObject, YearRange yearRange) {
        try {
            Article article = extractArticleAttributes(jsonObject);

            List<Person> authors = extractAuthors(jsonObject);
            Copy copy = extractCopyAttributes(jsonObject);
            Magazine magazine = extractMagazineAttributes(jsonObject);

            resolveEntitiesRelationships(article, authors, copy, magazine);

            ArticleDAO.persist(article);

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

        return new Article(title,
                year,
                url,
                pages.getFirstElement(),
                pages.getSecondElement());
    }

    private static String extractTitle(JSONObject jsonObject) {
        String title = (String) jsonObject.get("title");

        return title.replaceAll("\"", "'");
    }

    private static Long extractYear(JSONObject jsonObject) {
        // The variable in which the data is extracted to, must be of type Object so that we can use
        // 'instanceof' to determine its type.
        Object year = jsonObject.get("year");

        if (year instanceof Long)
            return (Long) jsonObject.get("year");

        if (year instanceof String) {
            String castedYear = (String) year;

            if (isANumber(castedYear))
                return Long.valueOf(castedYear);
        }

        return null;
    }

    private static String extractURL(JSONObject jsonObject) {
        // The variable in which the data is extracted to, must be of type Object so that we can use
        // 'instanceof' to determine its type.
        Object ee = jsonObject.get("ee");

        if (ee == null) {
            System.out.println(System.lineSeparator() +
                    "'ee' attribute is missing in " + jsonObject + System.lineSeparator());

            return null;
        }

        if (ee instanceof String)
            return (String) ee;

        if (ee instanceof JSONArray) {
            JSONArray castedEeAttribute = (JSONArray) ee;

            if (SimpleJsonUtils.areAllArrayElementsOfTypeString(castedEeAttribute)) {
                List<String> eeElements = SimpleJsonUtils
                        .convertJsonArrayToStringCollection(castedEeAttribute);

                for (String element : eeElements)
                    if (element.contains("www.wikidata.org"))
                        return element;
            } else {
                for (Object element : castedEeAttribute)
                    if (element instanceof String)
                        return (String) element;
            }
        }

        if (ee instanceof JSONObject) {
            JSONObject castedEeAttribute = (JSONObject) ee;

            return (String) castedEeAttribute.get("content");
        }

        // TODO: In the current DBLP JSON file there's one case that, being 'ee' of type JSONArray, doesn't enter
        //  the 'if' case.
        System.out.println(System.lineSeparator() + "The data structure for attribute 'ee' is not considered. " +
                Article.class + " 'url' field will be set to null" + System.lineSeparator());

        return null;
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

    /**
     * As reference for the edge cases see: https://github.com/Xiphereal/ProyectoIEI/pull/1#issuecomment-730471867
     */
    private static List<Person> extractAuthors(JSONObject jsonObject) {
        // The variable in which the data is extracted to, must be of type Object so that we can use
        // 'instanceof' to determine its type.
        Object authors = jsonObject.get("author");

        if (authors == null) {
            System.out.println(System.lineSeparator() +
                    "'author' attribute is missing in " + jsonObject + System.lineSeparator());

            return null;
        }

        if (authors instanceof JSONArray) {
            JSONArray castedAuthorAttribute = (JSONArray) authors;

            if (SimpleJsonUtils.areAllArrayElementsOfTypeString(castedAuthorAttribute)) {
                List<String> authorElements = SimpleJsonUtils
                        .convertJsonArrayToStringCollection(castedAuthorAttribute);

                List<Person> parsedAuthors = new ArrayList<>();

                for (String element : authorElements)
                    parsedAuthors.add(Person.extractPersonAttributes(element));

                return parsedAuthors;
            }
        }

        System.out.println(System.lineSeparator() + "The data structure for attribute 'author' is not considered. " +
                "The author collection will be set to null" + System.lineSeparator());

        return null;
    }

    private static Copy extractCopyAttributes(JSONObject jsonObject) {
        Integer volume = extractVolume(jsonObject);
        Integer number = extractNumber(jsonObject);
        Integer month = extractMonth(jsonObject);

        return new Copy(volume, number, month);
    }

    /**
     * Only considers attribute 'volume' being a plain Integer number.
     *
     * @return Null if doesn't fit a considered edge case, the Copy volume number if it does.
     */
    private static Integer extractVolume(JSONObject jsonObject) {
        // The variable in which the data is extracted to, must be of type Object so that we can use
        // 'instanceof' to determine its type.
        Object volume = jsonObject.get("volume");

        if (volume instanceof Long) {
            return ((Long) volume).intValue();
        }

        if (volume instanceof String) {
            String castedVolume = (String) volume;

            if (isANumber(castedVolume))
                return Integer.valueOf(castedVolume);
        }

        return null;
    }

    private static boolean isANumber(String castedVolume) {
        // REGEX: Not empty number.
        return castedVolume.matches("\\d+\\d*");
    }

    /**
     * Only considers attribute 'number' being a plain Integer number.
     *
     * @return Null if doesn't fit a considered edge case, the Copy number if it does.
     */
    private static Integer extractNumber(JSONObject jsonObject) {
        // The variable in which the data is extracted to, must be of type Object so that we can use
        // 'instanceof' to determine its type.
        Object number = jsonObject.get("number");

        if (number instanceof Long) {
            return ((Long) number).intValue();
        }

        if (number instanceof String) {
            String castedNumber = (String) number;

            if (isANumber(castedNumber))
                return Integer.valueOf(castedNumber);
        }

        return null;
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

    private static Magazine extractMagazineAttributes(JSONObject jsonObject) {
        Object name = jsonObject.get("journal");

        if (name == null) {
            System.out.println(System.lineSeparator() +
                    "'journal' attribute is missing in " + jsonObject + System.lineSeparator());

            return null;
        }

        String castedName = name.toString();
        return new Magazine(castedName);
    }

    private static void resolveEntitiesRelationships(Article article, List<Person> authors, Copy copy, Magazine magazine) {
        copy.setMagazinePublishBy(magazine);
        article.setCopyPublishedBy(copy);
        article.setAuthors(authors);

        if (authors != null)
            authors.forEach(author -> author.setAuthoredPublication(article.getTitle()));
    }
}
