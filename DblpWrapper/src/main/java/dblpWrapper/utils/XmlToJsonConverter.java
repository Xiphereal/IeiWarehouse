package dblpWrapper.utils;

import domainModel.utils.YearRange;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XmlToJsonConverter {

    public static List<Map<String, Object>> parseXmlToJson(YearRange yearRange, int maxArticles) {
        JSONObject convertedFile = getJsonFromXmlFile();
        JSONObject root = convertedFile.getJSONObject("dblp");
        JSONArray articles = root.getJSONArray("article");

        List<Map<String, Object>> filteredArticles = new ArrayList<>();

        for (int i = 0; i < articles.length() && i < maxArticles; i++) {
            int year = articles.getJSONObject(i).getInt("year");

            if (yearRange.isGivenYearBetweenRange((long) year))
                filteredArticles.add(articles.getJSONObject(i).toMap());
        }

        return filteredArticles;
    }

    private static JSONObject getJsonFromXmlFile() {
        StringBuilder dataFromXml = new StringBuilder();

        try {
            // This .getResourceAsStream() over the class allows for always getting
            // the reference to the given resource, despite being in development or
            // .jar context.
            InputStreamReader dataSourceReader =
                    new InputStreamReader(XmlToJsonConverter.class
                            .getResourceAsStream("/DBLP-ENTREGA-FINAL.xml"));

            BufferedReader dataSourceReaderAsBufferedReader = new BufferedReader(dataSourceReader);

            String line = "";

            while ((line = dataSourceReaderAsBufferedReader.readLine()) != null)
                dataFromXml.append(line);

        } catch (IOException fileException) {
            fileException.printStackTrace();
        }

        return XML.toJSONObject(dataFromXml.toString());
    }
}
