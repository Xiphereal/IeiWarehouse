package googleScholarWrapper.bibtexToJson;

import domainModel.utils.YearRange;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class BibtexToJsonParser {
    //TODO: change return in order to fit the specification
    public static JSONObject toJson(List<String> bibtex, YearRange yearRange, int maxPublications) {
        JSONArray books = new JSONArray();
        JSONArray articles = new JSONArray();
        JSONArray inproceedings = new JSONArray();
        JSONArray incollection = new JSONArray();
        for (int i = 0; i < bibtex.size() && i < maxPublications; i++) {
            String bibObject = bibtex.get(i);
            //this methods check the kind of publication that the bibtex is and add it to the corresponding jsonArray
            books = bookParser(books, bibObject, yearRange);
            articles = articleParser(articles, bibObject, yearRange);
            inproceedings = inproceedingsParser(inproceedings, bibObject, yearRange);
            incollection = incollectionParse(incollection, bibObject, yearRange);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("books", books);
        jsonObject.put("articles", articles);
        jsonObject.put("inproceedings", inproceedings);
        jsonObject.put("incollection", incollection);
        return jsonObject;
    }

    private static JSONArray incollectionParse(JSONArray incollection, String bibObject, YearRange yearRange) {
        if (bibObject.startsWith("@incollection")) {
            JSONObject publication = new JSONObject();
            //atributes
            String title;
            String authors;
            String booktitle;
            String pages;
            String year = "";
            String publisher;

            List<String> fields = Arrays.asList(bibObject.split("\n"));

            for (String field : fields) {
                field = field.trim();
                field = field.trim();
                if (field.startsWith("@title=")) {
                    title = beautify(field.substring(7));
                    publication.put("title", title);
                }
                if (field.startsWith("@author=")) {
                    authors = beautify(field.substring(7));
                    publication.put("author", authors);
                }
                if (field.startsWith("@booktitle=")) {
                    booktitle = beautify(field.substring(7));
                    publication.put("booktitle", booktitle);
                }
                if (field.startsWith("@pages=")) {
                    pages = beautify(field.substring(7));
                    publication.put("pages", pages);
                }
                if (field.startsWith("@year=")) {
                    year = beautify(field.substring(6));
                    publication.put("year", year);
                }
                if (field.startsWith("@publisher=")) {
                    publisher = beautify(field.substring(11));
                    publication.put("publisher", publisher);
                }
            }
            if (year.compareTo("") != 0 && yearRange.isGivenYearBetweenRange(Long.parseLong(year)))
                incollection.put(publication);
        }
        return incollection;
    }

    private static JSONArray inproceedingsParser(JSONArray inproceedings, String bibObject, YearRange yearRange) {
        if (bibObject.startsWith("@inproceedings")) {
            JSONObject publication = new JSONObject();
            //atributes
            String title;
            String authors;
            String booktitle;
            String year = "";
            List<String> fields = Arrays.asList(bibObject.split("\n"));

            for (String field : fields) {
                field = field.trim();
                field = field.trim();
                if (field.startsWith("@title=")) {
                    title = beautify(field.substring(7));
                    publication.put("title", title);
                }
                if (field.startsWith("@author=")) {
                    authors = beautify(field.substring(7));
                    publication.put("author", authors);
                }
                if (field.startsWith("@booktitle=")) {
                    booktitle = beautify(field.substring(7));
                    publication.put("booktitle", booktitle);
                }
                if (field.startsWith("@year=")) {
                    year = beautify(field.substring(6));
                    publication.put("year", year);
                }
            }
            if (year.compareTo("") != 0 && yearRange.isGivenYearBetweenRange(Long.parseLong(year)))
                inproceedings.put(publication);
        }
        return inproceedings;
    }

    private static JSONArray articleParser(JSONArray articles, String bibObject, YearRange yearRange) {
        if (bibObject.startsWith("@article")) {
            JSONObject publication = new JSONObject();
            // atributes
            String title;
            String authors;
            String journal;
            String volume;
            String number;
            String pages;
            String year = "";
            String url;
            String publisher;

            List<String> fields = Arrays.asList(bibObject.split("\n"));

            for (String field : fields) {
                field = field.trim();
                field = field.trim();
                if (field.startsWith("@title=")) {
                    title = beautify(field.substring(7));
                    publication.put("title", title);
                }
                if (field.startsWith("@author=")) {
                    authors = beautify(field.substring(8));
                    publication.put("authors", authors);
                }
                if (field.startsWith("@journal=")) {
                    journal = beautify(field.substring(9));
                    publication.put("journal", journal);
                }
                if (field.startsWith("@volume=")) {
                    volume = beautify(field.substring(8));
                    publication.put("volume", volume);
                }
                if (field.startsWith("@number=")) {
                    number = beautify(field.substring(8));
                    publication.put("number", number);
                }
                if (field.startsWith("@pages=")) {
                    pages = beautify(field.substring(7));
                    publication.put("pages", pages);
                }
                if (field.startsWith("@year=")) {
                    year = beautify(field.substring(6));
                    publication.put("year", year);
                }
                //TODO: delete this, useless since we dont have url in bibtex
                if (field.startsWith("@url=")) {
                    url = beautify(field.substring(5));
                    publication.put("url", url);
                }
                if (field.startsWith("@publisher=")) {
                    publisher = beautify(field.substring(11));
                    publication.put("publisher", publisher);
                }

            }
            if (year.compareTo("") != 0 && yearRange.isGivenYearBetweenRange(Long.parseLong(year)))
                articles.put(publication);
        }
        return articles;
    }

    private static JSONArray bookParser(JSONArray books, String bibObject, YearRange yearRange) {
        if (bibObject.startsWith("@book")) {
            JSONObject publication = new JSONObject();
            // atributes
            String title;
            String authors;
            String volume;
            String year = "";
            String url;
            String publisher;
            List<String> fields = Arrays.asList(bibObject.split("\n"));

            for (String field : fields) {
                field = field.trim();
                if (field.startsWith("@title=")) {
                    title = beautify(field.substring(7));
                    publication.put("title", title);
                }
                if (field.startsWith("@year=")) {
                    year = beautify(field.substring(6));
                    publication.put("year", year);
                }
                //this is useless since we dont have url in bibtex
                //TODO: delete this
                if (field.startsWith("@url=")) {
                    url = beautify(field.substring(5));
                    publication.put("url", url);
                }
                if (field.startsWith("@publisher=")) {
                    publisher = beautify(field.substring(11));
                    publication.put("publisher", publisher);
                }
                if (field.startsWith("@author=")) {
                    authors = beautify(field.substring(9));
                    publication.put("authors", authors);
                }
                if (field.startsWith("@volume=")) {
                    volume = beautify(field.substring(field.length(), 8));
                    publication.put("volume", volume);
                }
            }
            if (year.compareTo("") != 0 && yearRange.isGivenYearBetweenRange(Long.parseLong(year)))
                books.put(publication);
        }
        return books;
    }

    private static String beautify(String text) {
        String aux = text;
        if (aux.endsWith("},")) {
            aux = aux.substring(0, aux.length() - 2);
        }
        aux = aux.replace("{\\'a}", "á");
        aux = aux.replace("{\\'e}", "é");
        aux = aux.replace("{\\'i}", "í");
        aux = aux.replace("{\\'o}", "ó");
        aux = aux.replace("{\\'u}", "ú");
        aux = aux.replace("{\\'\\a}", "á");
        aux = aux.replace("{\\'\\e}", "é");
        aux = aux.replace("{\\'\\i}", "í");
        aux = aux.replace("{\\'\\o}", "ó");
        aux = aux.replace("{\\'\\u}", "ú");
        aux = aux.replace("{", "");
        aux = aux.replace("}", "");
        aux = aux.replace("\\", "");
        return aux;
    }
}
