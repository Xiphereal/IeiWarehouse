package ieiWarehousePopulator.extractors;

import ieiWarehousePopulator.domain.*;
import ieiWarehousePopulator.domain.utils.Tuple;
import ieiWarehousePopulator.extractors.utils.YearRange;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GoogleScholarExtractor {
    public static void extractDataIntoWarehouse(YearRange yearRange) {
        try (FileReader fileReader = new FileReader("src/main/resources/googleSchoolar/sample_array.json")) {

            JSONParser jsonParser = new JSONParser();
            JSONObject entireJsonFile = (JSONObject) jsonParser.parse(fileReader);

            JSONArray articles = getArticlesFromJson(entireJsonFile);
            articles.forEach(article -> parseJsonArticle((JSONObject) article, yearRange));

            JSONArray books = getBooksFromJson(entireJsonFile);
            books.forEach(book -> parseJsonBook((JSONObject) book, yearRange));

            JSONArray communicationCongress = getCommunicationCongress(entireJsonFile);
            communicationCongress.forEach(congress ->
                    parseJsonCommunicationCongress((JSONObject) congress, yearRange));

        } catch (Exception e) {
            System.err.println("An error has occurred while extracting data in " + DblpExtractor.class.getName());
            e.printStackTrace();
        }
    }

    private static void parseJsonCommunicationCongress(JSONObject jsonObject, YearRange yearRange) {
        try {
            List<Person> authors = extractAuthors(jsonObject);
            CongressCommunication congressCommunication = extractCongressCommunicationAttributes(jsonObject);

            // If the congress communication is from outside the requested range,
            // there's no need in continuing parsing.
            if (!yearRange.isGivenYearBetweenRange(congressCommunication.getYear()))
                return;

            resolveEntitiesRelationshipsCommunication(congressCommunication, authors);

            congressCommunication.persist();

            //TODO: Check if this magazine already exists, if it does add this publication to the magazine.
            // Also check if that copy already exists, if it does Add the article to the copy
        } catch (ClassCastException e) {
            System.err.println("An error has occurred while retrieving the JSONObject " + jsonObject);
            e.printStackTrace();
        }
    }

    private static JSONArray getCommunicationCongress(JSONObject entireJsonFile) throws IOException, ParseException {
        JSONObject communicationCongress = (JSONObject) entireJsonFile.get("inproceedings");
        JSONArray jsonObjectContainer = new JSONArray();
        jsonObjectContainer.add(communicationCongress);

        return jsonObjectContainer;
    }

    private static void parseJsonBook(JSONObject jsonObject, YearRange yearRange) {
        try {
            List<Person> authors = extractAuthors(jsonObject);
            Book book = extractBookAttributes(jsonObject);

            // If the book is from outside the requested range, there's no need in continuing parsing.
            if (!yearRange.isGivenYearBetweenRange(book.getYear()))
                return;

            resolveEntitiesRelationshipsBook(book, authors);

            book.persist();

            //TODO: Check if this magazine already exists, if it does add this publication to the magazine.
            // Also check if that copy already exists, if it does Add the article to the copy
        } catch (ClassCastException e) {
            System.err.println("An error has occurred while retrieving the JSONObject " + jsonObject);
            e.printStackTrace();
        }
    }

    private static JSONArray getBooksFromJson(JSONObject entireJsonFile) throws IOException, ParseException {
        JSONArray jsonObjectContainer = (JSONArray) entireJsonFile.get("books");
        jsonObjectContainer.add(entireJsonFile.get("incollection"));

        return jsonObjectContainer;
    }

    private static JSONArray getArticlesFromJson(JSONObject entireJsonFile) throws IOException, ParseException {
        JSONArray jsonObjectContainer = (JSONArray) entireJsonFile.get("articles");

        return jsonObjectContainer;
    }

    private static void parseJsonArticle(JSONObject jsonObject, YearRange yearRange) {
        try {
            Article article = extractArticleAttributes(jsonObject);

            // If the article is from outside the requested range, there's no need in continuing parsing.
            if (!yearRange.isGivenYearBetweenRange(article.getYear()))
                return;

            List<Person> person = extractAuthors(jsonObject);
            Copy copy = extractCopyAttributes(jsonObject);
            Magazine magazine = extractMagazineAttributes(jsonObject);

            resolveEntitiesRelationships(article, person, copy, magazine);

            article.persist();

            //TODO: Check if this magazine already exists, if it does add this publication to the magazine.
            // Also check if that copy already exists, if it does Add the article to the copy
        } catch (ClassCastException e) {
            System.err.println("An error has occurred while retrieving the JSONObject " + jsonObject);
            e.printStackTrace();
        }
    }

    private static Book extractBookAttributes(JSONObject jsonObject) {
        String title = extractTitle(jsonObject);
        Long year = extractYear(jsonObject);
        String url = extractURL(jsonObject);
        String editorial = extractEditorial(jsonObject);
        return new Book(title,
                year,
                url,
                editorial);
    }

    private static JSONObject getInfoFromJson(FileReader fileReader) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject entireJsonFile = (JSONObject) jsonParser.parse(fileReader);
        JSONObject jsonObjectContainer = (JSONObject) entireJsonFile.get("bibtex");

        return jsonObjectContainer;
    }

    private static CongressCommunication extractCongressCommunicationAttributes(JSONObject jsonObject) {
        String title = extractTitle(jsonObject);
        Long year = extractYear(jsonObject);
        String url = extractURL(jsonObject);
        String congress = extractCongress(jsonObject);
        String edition = extractYear(jsonObject).toString();
        String place = extractCongressCommunicationPlace(jsonObject);
        Tuple<Integer, Integer> pages = extractPages(jsonObject);

        return new CongressCommunication(title,
                year,
                url,
                congress,
                edition,
                place,
                pages.getFirstElement(),
                pages.getSecondElement());
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
                pages.getSecondElement()
        );
    }

    private static String extractURL(JSONObject jsonObject) {
        // The variable in which the data is extracted to, must be of type Object so that we can use
        // 'instanceof' to determine its type.
        //TODO: URL doesn't appear in the json file, it might be extracted directly from the website
        return null;
    }

    /**
     * As reference for the edge cases see: https://github.com/Xiphereal/ProyectoIEI/pull/1#issuecomment-730471867
     */
    private static List<Person> extractAuthors(JSONObject jsonObject) {
        Object authorList = jsonObject.get("author");
        List<Person> parsedAuthors;

        if (authorList == null) {
            System.out.println(System.lineSeparator() +
                    "'author' attribute is missing in " + jsonObject + System.lineSeparator());

            return null;
        }

        if (authorList instanceof String) {
            parsedAuthors = new ArrayList<>();
            String parserString = (String) ((String) authorList).trim();
            parserString = parserString.replaceAll(",", "-");

            //extract each name from the whole string of names
            while (!parserString.isEmpty()) {
                int separator = ((String) parserString).indexOf("-");
                String fullName = "";
                Person person;
                if (separator != -1) {
                    fullName = (parserString.trim()).substring(0, separator - 1);
                    person = extractPersonAttributes(fullName);
                    parserString = parserString.substring(separator + 1);
                } else {
                    person = extractPersonAttributes(parserString.trim());
                    parserString = "";
                }

                parsedAuthors.add(person);
            }

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

        return new Copy(volume, number, month);
    }

    private static Tuple<Integer, Integer> extractPages(JSONObject jsonObject) {
        // The variable in which the data is extracted to, must be of type Object so that we can use
        // 'instanceof' to determine its type.
        Object pages = jsonObject.get("pages");

        if (pages == null) {
            System.out.println(System.lineSeparator() +
                    "'pages' attribute is missing in " + jsonObject + System.lineSeparator());

            return new Tuple<>(-1, -1);
        }

        int initialPage = 0;
        int finalPage = 0;

        // Attribute "pages" might be of type String if it is a range ("164--168") or

        if (pages instanceof String) {
            String stringPages = pages.toString();

            initialPage = extractInitialPage(stringPages);
            finalPage = extractFinalPage(stringPages);
        } else {
            return new Tuple<>();
        }
        return new Tuple<>(initialPage, finalPage);
    }

    private static int extractInitialPage(String pages) {
        return Integer.parseInt(pages.substring(0, pages.indexOf("-")));
    }

    private static int extractFinalPage(String pages) {
        // The '+ 12' is because String.substring(int startIndex) includes the char at startIndex and we need 1 more index since the format is "pag--pag".
        return Integer.parseInt(pages.substring(pages.indexOf("-") + 2));
    }

    /**
     * @param author The name is the first encountered word, the surname the second.
     */
    private static Person extractPersonAttributes(String author) {
        // Split the string using spaces as separators.
        String[] splitAuthor = author.split(" ");

        String name = splitAuthor[0];
        String surname = splitAuthor.length > 1 ? splitAuthor[1] : null;

        return new Person(name, surname);
    }

    private static Integer extractVolume(JSONObject jsonObject) {
        Object volume = jsonObject.get("volume");

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
        Object number = jsonObject.get("number");

        if (number == null || number.toString().compareTo("Preprint") == 0)
            return null;

        return Integer.parseInt(number.toString());
    }

    /**
     * Only considers attribute 'mdate' being a String separated by '-' with the month in the middle "x-mm-x".
     *
     * @return Null if doesn't fit a considered edge case, the Copy month if it does.
     */
    private static Integer extractMonth(JSONObject jsonObject) {
        //we don't have a month attribute in google schoolar articles
        return null;
    }

    private static boolean isInSimpleRangeFormat(String stringPages) {
        // REGEX: Two numbers separated by '-'
        return stringPages.matches("\\d+-\\d+");
    }

    private static String extractTitle(JSONObject jsonObject) {
        return (String) jsonObject.get("title");
    }

    private static Long extractYear(JSONObject jsonObject) {
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

    private static String extractDate(JSONObject jsonObject) {
        return null;
    }

    //we can't get the congress name in google schoolar inproceedings
    private static String extractCongress(JSONObject jsonObject) {
        return null;
    }

    //we can't get the place in google schoolar inproceedings
    private static String extractCongressCommunicationPlace(JSONObject jsonObject) {
        return null;
    }

    private static String extractEditorial(JSONObject jsonObject) {
        return null;
    }

    private static boolean isInRomanNotation(String stringPages) {
        // REGEX: Contains any number.
        return !stringPages.matches(".*[0-9].*");
    }

    private static void resolveEntitiesRelationshipsArticle(Article article, List<Person> authors, Copy copy, Magazine magazine) {
        return;
    }

    private static void resolveEntitiesRelationshipsBook(Book book, List<Person> authors) {
        return;
    }

    private static void resolveEntitiesRelationshipsCommunication(CongressCommunication congressCommunication, List<Person> authors) {
        return;
    }

    private static Magazine extractMagazineAttributes(JSONObject jsonObject) {
        Object name = jsonObject.get("journal");

        if (name == null) {
            System.out.println(System.lineSeparator() +
                    "'journal' attribute is missing in " + jsonObject + System.lineSeparator());

            return null;
        }

        return new Magazine(name.toString());
    }

    private static void resolveEntitiesRelationships(Article article, List<Person> authors, Copy copy, Magazine magazine) {
        copy.setMagazinePublishBy(magazine);
        article.setCopyPublishedBy(copy);
        article.setAuthors(authors);

        if (authors != null)
            authors.forEach(author -> author.setAuthoredPublication(article.getTitle()));
    }
}
