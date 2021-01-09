package domainModel.requestResponses;

import java.util.List;
import java.util.Map;

public class JsonMapResponse {
    private List<Map<String, Object>> jsonString;

    public JsonMapResponse(List<Map<String, Object>> jsonString) {
        this.jsonString = jsonString;
    }

    public List<Map<String, Object>> getJsonString() {
        return jsonString;
    }

    public void setJsonString(List<Map<String, Object>> jsonString) {
        this.jsonString = jsonString;
    }
}
