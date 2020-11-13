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
            Object obj = jsonParser.parse(fileReader);

            System.out.println(obj);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
