package dblpWrapper.JSONtoXML;

import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JsonToXmlConverter {
    private static String PATH_TO_XML = "src/main/java/dblpWrapper/JSONtoXML/DBLP-SOLO_ARTICLE-1.XML";

    public static JSONObject convert() throws IOException {
        StringBuilder dataFromXML = new StringBuilder();
        String line = "";
        BufferedReader br = new BufferedReader(new FileReader(PATH_TO_XML));
        while ((line = br.readLine()) != null) {
            dataFromXML.append(line);
        }
        JSONObject jsonObject = XML.toJSONObject(dataFromXML.toString());
        return jsonObject;
    }

}
