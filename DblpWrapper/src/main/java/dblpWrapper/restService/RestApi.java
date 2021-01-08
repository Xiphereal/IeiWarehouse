package dblpWrapper.restService;

import dblpWrapper.utils.XmlToJsonConverter;
import domainModel.requestResponses.JsonMapResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class RestApi {
    @GetMapping("/extract")
    public JsonMapResponse extractData(@RequestParam(value = "startYear", defaultValue = "1000") String startYear,
                                       @RequestParam(value = "endYear", defaultValue = "2999") String endYear,
                                       @RequestParam(value = "maxPublications", defaultValue = "5") String maxPublications) {
        return getDataFromDblp(Integer.parseInt(startYear), Integer.parseInt(endYear), Integer.parseInt(maxPublications));
    }

    private JsonMapResponse getDataFromDblp(int startYear, int endYear, int maxPublications) {
        // We filter the JSON file by year before we start creating the Publication list.
        // It is faster to filter the JSON than to filter while extracting the Articles in the
        // Warehouse.
        List<Map<String, Object>> filteredPublicationAsJson = XmlToJsonConverter.parseXmlToJson(startYear, endYear, maxPublications);
        return new JsonMapResponse(filteredPublicationAsJson);
    }
}
