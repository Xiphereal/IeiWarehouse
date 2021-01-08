package dblpWrapper.restService;

import dblpWrapper.utils.XmlToJsonConverter;
import domainModel.requestResponses.JsonMapResponse;
import domainModel.utils.YearRange;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class RestApi {
    @GetMapping("/extract")
    public JsonMapResponse extractData(@RequestParam(value = "startYear", defaultValue = "1000") int startYear,
                                       @RequestParam(value = "endYear", defaultValue = "2999") int endYear,
                                       @RequestParam(value = "maxPublications", defaultValue = "5") String maxPublications) {
        YearRange yearRange = new YearRange((long) startYear, (long) endYear);

        return getDataFromDblp(yearRange, Integer.parseInt(maxPublications));
    }

    private JsonMapResponse getDataFromDblp(YearRange yearRange, int maxPublications) {
        // We filter the JSON file by year before we start creating the Publication list.
        // It is faster to filter the JSON than to filter while extracting the Articles in the
        // Warehouse.
        List<Map<String, Object>> filteredPublicationAsJson = XmlToJsonConverter.parseXmlToJson(yearRange, maxPublications);
        return new JsonMapResponse(filteredPublicationAsJson);
    }
}
