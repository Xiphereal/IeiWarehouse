package dblpWrapper.JSONtoXML;

import domainModel.Publication;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public static List<JSONObject> filterByYear(int yearStart, int yearEnd) throws IOException {
        JSONObject convertedFilie = JsonToXmlConverter.convert();
        JSONObject globalObject = convertedFilie.getJSONObject("dblp");
        //System.out.println(globalObject.toString(2));

        JSONArray articles = globalObject.getJSONArray("article");
        List<JSONObject> validArticles = new ArrayList<>();
        for(int i = 0; i < articles.length(); i++) {
            int year = articles.getJSONObject(i).getInt("year");
            if(year >= yearStart && year <= yearEnd) {
                validArticles.add(articles.getJSONObject(i));
            }
        }
        return validArticles;
    }
}
