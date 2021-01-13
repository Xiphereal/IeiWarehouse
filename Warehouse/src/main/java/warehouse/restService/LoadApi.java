package warehouse.restService;

import domainModel.requestResponses.RequestStatusResponse;
import domainModel.utils.YearRange;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import warehouse.extractors.DblpExtractor;
import warehouse.extractors.GoogleScholarExtractor;
import warehouse.extractors.IeeeExtractor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents the endpoint for the load form, receiving the configuration
 * for the {@link DblpExtractor},
 * {@link GoogleScholarExtractor} and
 * {@link IeeeExtractor}.
 */
@RestController
public class LoadApi {
    private final AtomicLong requestId = new AtomicLong();

    private static final String OK_MESSAGE = "OK: The extraction has started as expected. " +
            "The results will be ready at the warehouse after a few minutes.";

    /**
     * A typical URL request could be:
     * "http://localhost:8080/extractData?startYear=2015&endYear=2018&extractFromDBLP=false&extractFromGoogleScholar=false"
     */
    @GetMapping("/extractData")
    public RequestStatusResponse extractData(@RequestParam(value = "startYear", defaultValue = "1000") String startYear,
                                             @RequestParam(value = "endYear", defaultValue = "2999") String endYear,
                                             @RequestParam(value = "extractFromDBLP", defaultValue = "true") boolean extractFromDblp,
                                             @RequestParam(value = "extractFromIEEE", defaultValue = "true") boolean extractFromIeee,
                                             @RequestParam(value = "extractFromGoogleScholar", defaultValue = "true") boolean extractFromGoogleScholar,
                                             @RequestParam(value = "maxPublications", defaultValue = "150") int maxPublications) {

        long startYearValue = YearRange.isYear(startYear) ? Long.parseLong(startYear) : 1000L;
        long endYearValue = YearRange.isYear(endYear) ? Long.parseLong(endYear) : 2999L;

        YearRange yearRange = new YearRange(startYearValue, endYearValue);

        runExtractorsAsync(extractFromDblp, extractFromIeee, extractFromGoogleScholar, yearRange, maxPublications);

        return new RequestStatusResponse(requestId.incrementAndGet(), OK_MESSAGE);
    }

    private void runExtractorsAsync(boolean extractFromDblp,
                                    boolean extractFromIeee,
                                    boolean extractFromGoogleScholar,
                                    YearRange yearRange,
                                    int maxPublications) {
        CompletableFuture.runAsync(() -> {
                    if (extractFromDblp)
                        DblpExtractor.extractDataIntoWarehouse(yearRange, maxPublications);

                    if (extractFromIeee)
                        IeeeExtractor.extractDataIntoWarehouse(yearRange, maxPublications);

                    if (extractFromGoogleScholar)
                        GoogleScholarExtractor.extractDataIntoWarehouse(yearRange, maxPublications);
                }
        );
    }
}
