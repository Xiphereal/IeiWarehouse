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
}