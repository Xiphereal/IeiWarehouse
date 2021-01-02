package dblpWrapper;

import dblpWrapper.JSONtoXML.JsonToXmlConverter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DBLPWrapper {
    public static void main(String[] args) throws IOException {
       //System.out.println(JsonToXmlConverter.convert().toString(3));

       SpringApplication.run(DBLPWrapper.class, args);
    }

    private List<JSONObject> filterByYear(int yearStart, int yearEnd) throws IOException {
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