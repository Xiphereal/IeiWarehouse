package dblpWrapper.JSONtoXML;

import domainModel.utils.YearRange;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonToXmlConverter {
    private static String PATH_TO_XML = "src/main/java/dblpWrapper/JSONtoXML/DBLP-SOLO_ARTICLE-1.XML";

    public static JSONObject xmlFileToJsonObject() {
        StringBuilder dataFromXML = new StringBuilder();
        String line = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(PATH_TO_XML));
            while ((line = br.readLine()) != null) {
                dataFromXML.append(line);
            }
        } catch (FileNotFoundException fileException) {
            System.out.println("ERROR: File not found");
        } catch (IOException ioException) {
            System.out.println("ERROR: IOException");
        }
        JSONObject jsonObject = XML.toJSONObject(dataFromXML.toString());
        return jsonObject;
    }

    public static List<Map<String, Object>> parseXmlToJson(int yearStart, int yearEnd) {
        JSONObject convertedFilie = JsonToXmlConverter.xmlFileToJsonObject();
        JSONObject globalObject = convertedFilie.getJSONObject("dblp");
        JSONArray articles = globalObject.getJSONArray("article");

        YearRange yearRange = new YearRange((long) yearStart, (long) yearEnd);

        List<Map<String, Object>> validArticles = new ArrayList<>();
        for (int i = 0; i < articles.length(); i++) {
            int year = articles.getJSONObject(i).getInt("year");
            if (yearRange.isGivenYearBetweenRange((long) year)) {
                validArticles.add(articles.getJSONObject(i).toMap());
            }
        }
        return validArticles;
    }
}
