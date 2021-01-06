package restService.requestResponses;

import java.util.List;
import java.util.Map;

public class RequestResultResponse {
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
