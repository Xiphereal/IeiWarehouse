package dblpWrapper.restService;

import dblpWrapper.JSONtoXML.JsonToXmlConverter;
import dblpWrapper.restService.requestResponses.RequestResponse;
import dblpWrapper.restService.requestResponses.RequestResultResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class RestApi {
    @GetMapping("/extract")
    public RequestResponse extractData(@RequestParam(value = "startYear", defaultValue = "1000") String startYear,
                                       @RequestParam(value = "endYear", defaultValue = "2999") String endYear) throws IOException {
        RequestResultResponse req = getDataFromDblp(Integer.parseInt(startYear), Integer.parseInt(endYear));
        return req;
    }

    private RequestResultResponse getDataFromDblp(int startYear, int endYear) {
        //we filter the json file by year before we start creating the article list
        //It is faster to filter the json than to filter while creating the articles(already tested it)
        List<Map<String, Object>> filteredList = JsonToXmlConverter.parseXmlToJson(startYear, endYear);

        return new RequestResultResponse(filteredList);
    }

}
