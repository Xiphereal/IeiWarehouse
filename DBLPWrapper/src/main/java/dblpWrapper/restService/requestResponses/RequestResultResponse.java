package dblpWrapper.restService.requestResponses;

import domainModel.Article;
import domainModel.Book;
import domainModel.CongressCommunication;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class RequestResultResponse implements RequestResponse {
    private List<Map<String, Object>> jsonString;

    public RequestResultResponse(List<Map<String, Object>> jsonString) {
        this.jsonString = jsonString;
    }

    public List<Map<String, Object>> getJsonString() {
        return jsonString;
    }

    public void setJsonString(List<Map<String, Object>> jsonString) {
        this.jsonString = jsonString;
    }
}
