package googleScholarWrapper.bibtexToJson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BibtexToJsonParser {
    //TODO: change return in order to fit the specification
    public static JSONObject toJson(List<String> bibtex) throws IOException {
        JSONArray books = new JSONArray();
        JSONArray articles = new JSONArray();
        JSONArray inproceedings = new JSONArray();
        JSONArray incollection = new JSONArray();
        for (int i = 0; i < bibtex.size(); i++) {
            String bibObject = bibtex.get(i);
            //this methods check the kind of publication that the bibtex is and add it to the corresponding jsonArray
            books = bookParser(books, bibObject);
            articles = articleParser(articles, bibObject);
            inproceedings = inproceedingsParser(inproceedings, bibObject);
            incollection = incollectionParse(incollection, bibObject);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("books", books);
        jsonObject.put("articles", articles);
        jsonObject.put("inproceedings", inproceedings);
        jsonObject.put("incollection", incollection);
        return jsonObject;
    }

    private static JSONArray incollectionParse(JSONArray incollection, String bibObject) {
        if (bibObject.startsWith("incollection")) {
            JSONObject publication = new JSONObject();
            //atributes
            String title = "";
            String authors = "";
            String booktitle = "";
            String pages = "";
            String year = "";
            String publisher = "";

            List<String> fields = Arrays.asList(bibObject.split("\n"));

            for (String field : fields) {
                field = field.trim();
                field = field.trim();
                if (field.startsWith("title=")) {
                    title = beautify(field.substring(7));
                    publication.put("title", title);
                }
                if (field.startsWith("author=")) {
                    authors = beautify(field.substring(7));
                    publication.put("author", authors);
                }
                if (field.startsWith("booktitle=")) {
                    booktitle = beautify(field.substring(7));
                    publication.put("booktitle", booktitle);
                }
                if (field.startsWith("pages=")) {
                    pages = beautify(field.substring(7));
                    publication.put("pages", pages);
                }
                if (field.startsWith("year=")) {
                    year = beautify(field.substring(6));
                    publication.put("year", year);
                }
                if (field.startsWith("publisher=")) {
                    publisher = beautify(field.substring(11));
                    publication.put("publisher", publisher);
                }
            }
            incollection.put(publication);
        }
        return incollection;
    }

    private static JSONArray inproceedingsParser(JSONArray inproceedings, String bibObject) {
        if (bibObject.startsWith("inproceedings")) {
            JSONObject publication = new JSONObject();
            //atributes
            String title = "";
            String authors = "";
            String booktitle = "";

            List<String> fields = Arrays.asList(bibObject.split("\n"));

            for (String field : fields) {
                field = field.trim();
                field = field.trim();
                if (field.startsWith("title=")) {
                    title = beautify(field.substring(7));
                    publication.put("title", title);
                }
                if (field.startsWith("author=")) {
                    authors = beautify(field.substring(7));
                    publication.put("author", authors);
                }
                if (field.startsWith("booktitle=")) {
                    booktitle = beautify(field.substring(7));
                    publication.put("booktitle", booktitle);
                }
            }
            inproceedings.put(publication);
        }
        return inproceedings;
    }

    private static JSONArray articleParser(JSONArray articles, String bibObject) {
        if (bibObject.startsWith("article")) {
            JSONObject publication = new JSONObject();
            // atributes
            String title = "";
            String authors = "";
            String journal = "";
            String volume = "";
            String number = "";
            String pages = "";
            String year = "";
            String url = "";
            String publisher = "";

            List<String> fields = Arrays.asList(bibObject.split("\n"));

            for (String field : fields) {
                field = field.trim();
                field = field.trim();
                if (field.startsWith("title=")) {
                    title = beautify(field.substring(7));
                    publication.put("title", title);
                }
                if (field.startsWith("author=")) {
                    authors = beautify(field.substring(8));
                    publication.put("authors", authors);
                }
                if (field.startsWith("journal=")) {
                    journal = beautify(field.substring(9));
                    publication.put("journal", journal);
                }
                if (field.startsWith("volume=")) {
                    volume = beautify(field.substring(8));
                    publication.put("volume", volume);
                }
                if (field.startsWith("number=")) {
                    number = beautify(field.substring(8));
                    publication.put("number", number);
                }
                if (field.startsWith("pages=")) {
                    pages = beautify(field.substring(7));
                    publication.put("pages", pages);
                }
                if (field.startsWith("year=")) {
                    year = beautify(field.substring(6));
                    publication.put("year", year);
                }
                //TODO: delete this, useless since we dont have url in bibtex
                if (field.startsWith("url=")) {
                    url = beautify(field.substring(5));
                    publication.put("url", url);
                }
                if (field.startsWith("publisher=")) {
                    publisher = beautify(field.substring(11));
                    publication.put("publisher", publisher);
                }

            }
            articles.put(publication);
        }
        return articles;
    }

    private static JSONArray bookParser(JSONArray books, String bibObject) {
        if (bibObject.startsWith("book")) {
            JSONObject publication = new JSONObject();
            // atributes
            String title = "";
            String authors = "";
            String volume = "";
            String year = "";
            String url = "";
            String publisher = "";
            List<String> fields = Arrays.asList(bibObject.split("\n"));

            for (String field : fields) {
                field = field.trim();
                if (field.startsWith("title=")) {
                    title = beautify(field.substring(7));
                    publication.put("title", title);
                }
                if (field.startsWith("year=")) {
                    year = beautify(field.substring(6));
                    publication.put("year", year);
                }
                //this is useless since we dont have url in bibtex
                //TODO: delete this
                if (field.startsWith("url=")) {
                    url = beautify(field.substring(5));
                    publication.put("url", url);
                }
                if (field.startsWith("publisher=")) {
                    publisher = beautify(field.substring(11));
                    publication.put("publisher", publisher);
                }
                if (field.startsWith("author=")) {
                    authors = beautify(field.substring(9));
                    publication.put("authors", authors);
                }
                if (field.startsWith("volume=")) {
                    volume = beautify(field.substring(field.length(), 8));
                    publication.put("volume", volume);
                }
            }
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

    /*
    public static List<String> toJson(List<String> bibtex) throws IOException {
        List<String> jsonList = new ArrayList<>();
        for (int i = 0; i < bibtex.size(); i++) {
            Reader reader = new StringReader(bibtex.get(i));

            try {
                LaTeXParser parser = new LaTeXParser();

                List<LaTeXObject> laTeXObjects = parser.parse(reader);
                LaTeXPrinter laTeXPrinter = new LaTeXPrinter();
               // jsonList.add(new JSONPObject(laTeXObjects.get(i)));
            } catch (ParseException exception) {
                System.out.println("ERROR");
            } finally {
                reader.close();
            }
        }
        return jsonList;
    }

    public static List<String> toJson(String bibtex) throws IOException {
        return toJson(List.of(bibtex));
    }
*/
}
