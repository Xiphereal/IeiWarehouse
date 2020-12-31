package warehouse.extractors;

import domainModel.*;
import domainModel.utils.Tuple;
import domainModel.utils.YearRange;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import warehouse.extractors.utils.RomanToDecimalConverter;
import warehouse.persistence.dataAccessObjects.ArticleDAO;
import warehouse.persistence.dataAccessObjects.BookDAO;
import warehouse.persistence.dataAccessObjects.CongressCommunicationDAO;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IeeeExtractor {

    // TODO: Revert the changes made for the year-filtered extractions and support the
    //  performing of a REST API request to the wrapper for obtaining the already
    //  filtered JSON file.
    public static void extractDataIntoWarehouse(YearRange yearRange) {
        try (FileReader fileReader = new FileReader("src/main/resources/ieee/ieeeXplore_2018-2020-short.json")) {

            JSONArray articles = getArticlesFromJson(fileReader);

            articles.forEach(article -> parseJsonObject((JSONObject) article, yearRange));

        } catch (Exception e) {
            System.err.println("An error has occurred while extracting data in " + IeeeExtractor.class.getName());
            e.printStackTrace();
        }
    }

    private static JSONArray getArticlesFromJson(FileReader fileReader) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject entireJsonFile = (JSONObject) jsonParser.parse(fileReader);

        return (JSONArray) entireJsonFile.get("articles");
    }

    private static void parseJsonObject(JSONObject jsonObject, YearRange yearRange) {
        try {
            List<Person> person = extractAuthors(jsonObject);
            String type = (String) jsonObject.get("content_type");

            // TODO: Check if this magazine already exists, if it does add this publication to the magazine.
            //  Also check if that copy already exists, if it does Add the article to the copy
            if (type.compareTo("Early Access Articles") == 0 || type.compareTo("Journals") == 0) {
                Article article = extractArticleAttributes(jsonObject);

                // If the article is from outside the requested range, there's no need in continuing parsing.
                if (!yearRange.isGivenYearBetweenRange(article.getYear()))
                    return;

                Copy copy = extractCopyAttributes(jsonObject);
                Magazine magazine = new Magazine((String) jsonObject.get("publication_title"));

                resolveEntitiesRelationshipsArticle(article, person, copy, magazine);

                ArticleDAO.persist(article);

            } else if (type.compareTo("Conferences") == 0) {
                CongressCommunication congressCommunication = extractCongressCommunicationAttributes(jsonObject);

                // If the congress communication is from outside the requested range,
                // there's no need in continuing parsing.
                if (!yearRange.isGivenYearBetweenRange(congressCommunication.getYear()))
                    return;

                resolveEntitiesRelationshipsCommunication(congressCommunication, person);

                CongressCommunicationDAO.persist(congressCommunication);

            } else if (type.compareTo("Books") == 0) {
                Book book = extractBookAttributes(jsonObject);

                // If the book is from outside the requested range, there's no need in continuing parsing.
                if (!yearRange.isGivenYearBetweenRange(book.getYear()))
                    return;

                resolveEntitiesRelationshipsBook(book, person);

                BookDAO.persist(book);

            }
        } catch (ClassCastException e) {
            System.err.println("An error has occurred while retrieving the JSONObject " + jsonObject);
            e.printStackTrace();
        }
    }

    private static Book extractBookAttributes(JSONObject jsonObject) {
        String title = extractCongressCommunicationTitle(jsonObject);
        Long year = extractYear(jsonObject);
        String url = extractURL(jsonObject);
        String editorial = extractEditorial(jsonObject);

        return new Book(title,
                year,
                url,
                editorial);
    }

    private static CongressCommunication extractCongressCommunicationAttributes(JSONObject jsonObject) {
        String title = extractCongressCommunicationTitle(jsonObject);
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

        return jsonObject.get("pdf_url").toString();
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

            // If no authors are found, the author list should be null.
            if (castedAuthorAttribute.size() == 0)
                return null;

            List<Person> parsedAuthors = new ArrayList<>();
            int i;
            for (Object element : castedAuthorAttribute)
                parsedAuthors.add(Person.extractPersonAttributes(((JSONObject) element).get("full_name").toString()));

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
        Object initial_page = jsonObject.get("start_page");
        Object final_page = jsonObject.get("end_page");

        int initialPage = 0;

        if (initial_page instanceof String) {
            if (isInRomanNotation((String) initial_page))
                RomanToDecimalConverter.romanToDecimal((String) initial_page);
            else
                initialPage = Integer.parseInt((String) initial_page);
        }

        int finalPage = 0;

        if (final_page instanceof String) {
            if (isInRomanNotation((String) final_page))
                RomanToDecimalConverter.romanToDecimal((String) final_page);
            else
                finalPage = Integer.parseInt((String) initial_page);
        }

        return new Tuple<>(initialPage, finalPage);
    }

    private static Integer extractVolume(JSONObject jsonObject) {
        // The variable in which the data is extracted to, must be of type Object so that we can use
        // 'instanceof' to determine its type.
        Object volume = jsonObject.get("volume");

        try {
            return Integer.parseInt((String) volume);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Only considers attribute 'number' being a plain Integer number.
     *
     * @return Null if doesn't fit a considered edge case, the Copy number if it does.
     */
    private static Integer extractNumber(JSONObject jsonObject) {
        // The variable in which the data is extracted to, must be of type Object so that we can use
        // 'instanceof' to determine its type.

        return Integer.parseInt(jsonObject.get("article_number").toString());
    }

    /**
     * Only considers attribute 'mdate' being a String separated by '-' with the month in the middle "x-mm-x".
     *
     * @return Null if doesn't fit a considered edge case, the Copy month if it does.
     */
    private static Integer extractMonth(JSONObject jsonObject) {
        String date = extractDate(jsonObject);

        if (date == null)
            return null;

        String monthWritten = date.replaceAll("\\d", "");
        int firstMonthEnd = monthWritten.indexOf("-");

        if (firstMonthEnd != -1)
            monthWritten = monthWritten.substring(0, firstMonthEnd);

        monthWritten = monthWritten.replaceAll("\\s+", "");

        switch (monthWritten) {
            case "Jan.":
                return 1;
            case "Feb.":
                return 2;
            case "March":
                return 3;
            case "April":
                return 4;
            case "May":
                return 5;
            case "June":
                return 6;
            case "July":
                return 7;
            case "Aug.":
                return 8;
            case "Sept.":
                return 9;
            case "Oct.":
                return 10;
            case "Nov.":
                return 11;
            case "Dec.":
                return 12;
        }
        return null;
    }

    private static String extractTitle(JSONObject jsonObject) {
        return (String) jsonObject.get("publication_title");
    }

    private static Long extractYear(JSONObject jsonObject) {
        return (Long) jsonObject.get("publication_year");
    }

    private static String extractDate(JSONObject jsonObject) {
        return (String) jsonObject.get("publication_date");
    }

    private static String extractCongress(JSONObject jsonObject) {
        return (String) jsonObject.get("publication_title");
    }

    private static String extractCongressCommunicationTitle(JSONObject jsonObject) {
        return (String) jsonObject.get("title");
    }

    private static String extractCongressCommunicationPlace(JSONObject jsonObject) {
        return (String) jsonObject.get("conference_location");
    }

    private static String extractEditorial(JSONObject jsonObject) {
        return (String) jsonObject.get("publisher");
    }

    private static boolean isInRomanNotation(String stringPages) {
        // REGEX: Contains any number.
        return !stringPages.matches(".*[0-9].*");
    }

    private static void resolveEntitiesRelationshipsArticle(Article article, List<Person> authors, Copy copy, Magazine magazine) {
        copy.setMagazinePublishBy(magazine);
        article.setCopyPublishedBy(copy);
        article.setAuthors(authors);

        if (authors != null)
            authors.forEach(author -> author.setAuthoredPublication(article.getTitle()));
    }

    private static void resolveEntitiesRelationshipsBook(Book book, List<Person> authors) {
        book.setAuthors(authors);

        if (authors != null)
            authors.forEach(author -> author.setAuthoredPublication(book.getTitle()));
    }

    private static void resolveEntitiesRelationshipsCommunication(CongressCommunication congressCommunication, List<Person> authors) {
        congressCommunication.setAuthors(authors);

        if (authors != null)
            authors.forEach(author -> author.setAuthoredPublication(congressCommunication.getTitle()));
    }
}
