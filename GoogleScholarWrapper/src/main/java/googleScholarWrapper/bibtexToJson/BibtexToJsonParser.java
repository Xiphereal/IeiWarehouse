package googleScholarWrapper.bibtexToJson;

import domainModel.Book;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BibtexToJsonParser {
    public static List<String> toJson(List<String> bibtex) throws IOException {
        String books = "\"books\": [";
        List<String> json = new ArrayList<>();
        for (int i = 0; i < bibtex.size(); i++) {
            String bibObject = bibtex.get(i);

            if (bibObject.startsWith("book")) {
                String publication = "{";
                // conver to book
                String title = "";
                String authors = "";
                String volume = "";
                Long year = 0L;
                String url = "";
                String publisher = "";
                List<String> fields = Arrays.asList(bibObject.split("\n"));
                //title
                for (String field : fields) {
                    field = field.trim();
                    if (field.startsWith("title=")) {
                        title = beautify(field.substring(7, field.length()));
                        publication = publication + "\"title\"" + ":" + "\"" + title + "\"" + ",";
                    }
                    if (field.startsWith("year=")) {
                        year = Long.parseLong(beautify(field.substring(6, field.length())));
                        publication = publication + "\"year\":" + "\"" + year + "\"" + ",";
                    }
                    if (field.startsWith("url=")) {
                        url = beautify(field.substring(5, field.length()));
                        publication = publication + "\"url\":" + "\"" + year + "\"" + ",";
                    }
                    if (field.startsWith("publisher=")) {
                        publisher = beautify(field.substring(11, field.length()));
                        publication = publication + "\"publisher\":" + "\"" + publisher + "\"" + ",";
                    }
                    if (field.startsWith("author=")) {
                        authors = beautify(field.substring(9, field.length()));
                        publication = publication + "\"author\":" + "\"" + authors + "\"" + ",";
                    }
                    if (field.startsWith("volume=")) {
                        volume = beautify(field.substring(8, field.length()));
                        publication = publication + "\"volume\":" + "\"" + volume + "\"" + ",";
                    }
                }
                publication = publication.substring(0, publication.length()-1) + "},";
                books = books + publication;
            }
            if (bibObject.startsWith("article")) {
                String publication = "{";
                // conver to book
                String title = "";
                String authors = "";
                String volume = "";
                Long year = 0L;
                String url = "";
                String publisher = "";
                List<String> fields = Arrays.asList(bibObject.split("\n"));
                //title
                for (String field : fields) {
                    field = field.trim();
                    if (field.startsWith("title=")) {
                        title = beautify(field.substring(7, field.length()));
                        publication = publication + "\"title\"" + ":" + "\"" + title + "\"" + ",";
                    }
                    if (field.startsWith("year=")) {
                        year = Long.parseLong(beautify(field.substring(6, field.length())));
                        publication = publication + "\"year\":" + "\"" + year + "\"" + ",";
                    }
                    if (field.startsWith("url=")) {
                        url = beautify(field.substring(5, field.length()));
                        publication = publication + "\"url\":" + "\"" + year + "\"" + ",";
                    }
                    if (field.startsWith("publisher=")) {
                        publisher = beautify(field.substring(11, field.length()));
                        publication = publication + "\"publisher\":" + "\"" + publisher + "\"" + ",";
                    }
                    if (field.startsWith("author=")) {
                        authors = beautify(field.substring(9, field.length()));
                        publication = publication + "\"author\":" + "\"" + authors + "\"" + ",";
                    }
                    if (field.startsWith("volume=")) {
                        volume = beautify(field.substring(8, field.length()));
                        publication = publication + "\"volume\":" + "\"" + volume + "\"" + ",";
                    }
                }
                publication = publication.substring(0, publication.length()-1) + "},";
                books = books + publication;
            }
        }
        books = books.substring(0, books.length() -1) + "]";
        json.add(books);
        return json;
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
