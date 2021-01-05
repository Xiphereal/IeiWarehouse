package dblpWrapper.restService;

import dblpWrapper.utils.XmlToJsonConverter;
import dblpWrapper.restService.requestResponses.RequestResultResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class RestApi {
    @GetMapping("/extract")
    public RequestResultResponse extractData(@RequestParam(value = "startYear", defaultValue = "1000") String startYear,
                                       @RequestParam(value = "endYear", defaultValue = "2999") String endYear) {
        return getDataFromDblp(Integer.parseInt(startYear), Integer.parseInt(endYear));
    }

    private RequestResultResponse getDataFromDblp(int startYear, int endYear) {
        // We filter the JSON file by year before we start creating the Publication list.
        // It is faster to filter the JSON than to filter while extracting the Articles in the
        // Warehouse.
        List<Map<String, Object>> filteredList = XmlToJsonConverter.parseXmlToJson(startYear, endYear);

        return new RequestResultResponse(filteredList);
    }

}
