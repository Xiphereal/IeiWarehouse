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
    public DblpExtractor() {

    }

    public static void extractDataIntoWarehouse() {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader fileReader = new FileReader("src/main/resources/dblp/dblp-solo-article-1.json")) {
            //Read JSON file
            JSONObject entireJsonFile = (JSONObject) jsonParser.parse(fileReader);
            JSONObject jsonObjectContainer = (JSONObject) entireJsonFile.get("dblp");
            JSONArray articles = (JSONArray) jsonObjectContainer.get("article");

            articles.forEach(article -> parseJsonObject((JSONObject) article));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void parseJsonObject(JSONObject jsonObject) {
        Long year = (Long) jsonObject.get("year");
        System.out.println(year);
    }
}
