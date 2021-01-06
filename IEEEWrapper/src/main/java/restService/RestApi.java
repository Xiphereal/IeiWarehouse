package restService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import restService.requestResponses.RequestResultResponse;

import java.util.List;
import java.util.Map;

@RestController
public class RestApi {
    @GetMapping("/extract")
    public RequestResultResponse extractData(@RequestParam(value = "startYear", defaultValue = "1000") String startYear,
                                             @RequestParam(value = "endYear", defaultValue = "2999") String endYear,
                                             @RequestParam(value = "maxPublications", defaultValue = "5") String maxPublications) {
        return getDataFromDblp(Integer.parseInt(startYear), Integer.parseInt(endYear), Integer.parseInt(maxPublications));
    }

    private RequestResultResponse getDataFromDblp(int startYear, int endYear, int maxArticles) {
        // We filter the JSON file by year before we start creating the Publication list.
        // It is faster to filter the JSON than to filter while extracting the Articles in the
        // Warehouse.
        //List<Map<String, Object>> filteredList = XmlToJsonConverter.parseXmlToJson(startYear, endYear, maxArticles);
        return null;
    }
}
