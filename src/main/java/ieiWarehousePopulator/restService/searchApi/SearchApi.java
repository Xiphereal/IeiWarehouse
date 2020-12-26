package ieiWarehousePopulator.restService.searchApi;

import ieiWarehousePopulator.restService.RequestStatusResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents the endpoint for the search form, allowing for filtered data
 * request to the Warehouse.
 */
@RestController
public class SearchApi {
    private final AtomicLong requestId = new AtomicLong();

    private static final String ERROR_MESSAGE = "ERROR: The given parameters for the search are invalid. " +
            "TODO: Continue defining the error message";

    /**
     * A typical URL request could be:
     * "http://localhost:8080/extractData?startYear=2015&endYear=2018&extractFromDBLP=false&extractFromGoogleScholar=false"
     */
    @GetMapping("/extractData")
    public RequestStatusResponse getData(@RequestParam(value = "startYear", defaultValue = "1000") String startYear,
                                         @RequestParam(value = "endYear", defaultValue = "2999") String endYear,
                                         @RequestParam(value = "author", defaultValue = "") String author,
                                         @RequestParam(value = "searchArticles", defaultValue = "true") boolean searchArticles,
                                         @RequestParam(value = "searchCongressCommunications", defaultValue = "true") boolean searchCongressCommunications,
                                         @RequestParam(value = "searchBooks", defaultValue = "true") boolean searchBooks) {
        return null;
    }
}
