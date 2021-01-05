package dblpWrapper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DblpWrapper {
    public static void main(String[] args){
       //System.out.println(JsonToXmlConverter.convert().toString(3));

       SpringApplication.run(DblpWrapper.class, args);
    }
}