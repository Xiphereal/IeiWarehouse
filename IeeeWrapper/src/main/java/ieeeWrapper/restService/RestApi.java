package ieeeWrapper.restService;

import ieeeWrapper.utils.HttpService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApi {

    private final String ieeeURl = "http://ieeexploreapi.ieee.org/api/v1/search/articles?apikey=5sgmwkb7768ztxdw2vcze5x3&format=json";

    @GetMapping("/extract")
    public String extractData(@RequestParam(value = "startYear", defaultValue = "1000") String startYear,
                              @RequestParam(value = "endYear", defaultValue = "2999") String endYear,
                              @RequestParam(value = "maxPublications", defaultValue = "5") String maxPublications) {
        return getDataFromIEEE(Integer.parseInt(startYear), Integer.parseInt(endYear), Integer.parseInt(maxPublications));
    }

    private String getDataFromIEEE(int startYear, int endYear, int maxArticles) {
        String ieeeQuery = ieeeURl + "&max_records=" + maxArticles + "&start_record=1&sort_order=asc&sort_field=article_number&start_year=" + startYear +
                "&end_year=" + endYear;

        String jsonRetrieved = HttpService.executeGet(ieeeQuery);

        return jsonRetrieved;
    }
}
