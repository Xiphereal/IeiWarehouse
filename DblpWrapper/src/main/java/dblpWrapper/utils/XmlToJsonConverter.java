package dblpWrapper.utils;

import domainModel.utils.YearRange;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XmlToJsonConverter {
    private static final String PATH_TO_XML = "src/main/resources/DBLP-SOLO_ARTICLE-1.XML";

    public static List<Map<String, Object>> parseXmlToJson(int yearStart, int yearEnd) {
        JSONObject convertedFile = getJsonFromXmlFile();
        JSONObject root = convertedFile.getJSONObject("dblp");
        JSONArray articles = root.getJSONArray("article");

        YearRange yearRange = new YearRange((long) yearStart, (long) yearEnd);

        List<Map<String, Object>> filteredArticles = new ArrayList<>();

        for (int i = 0; i < articles.length(); i++) {
            int year = articles.getJSONObject(i).getInt("year");

            if (yearRange.isGivenYearBetweenRange((long) year))
                filteredArticles.add(articles.getJSONObject(i).toMap());
        }

        return filteredArticles;
    }

    private static JSONObject getJsonFromXmlFile() {
        StringBuilder dataFromXml = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(PATH_TO_XML));

            String line = "";
            while ((line = br.readLine()) != null)
                dataFromXml.append(line);

        } catch (IOException fileException) {
            fileException.printStackTrace();
        }

        return XML.toJSONObject(dataFromXml.toString());
    }
}
