package ieiWarehousePopulator.restService.loadApi;

import ieiWarehousePopulator.extractors.DblpExtractor;
import ieiWarehousePopulator.extractors.GoogleScholarExtractor;
import ieiWarehousePopulator.extractors.IeeeExtractor;
import ieiWarehousePopulator.extractors.utils.YearRange;
import ieiWarehousePopulator.restService.RequestStatusResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    private static final String ERROR_MESSAGE = "ERROR: The given parameters for the extraction are invalid. " +
            "The expected parameters are 'startYear:yyyy' and 'endYear:yyyy'" +
            ", with startYear being less than endYear. " +
            "The valid year range goes from 1000 to 2999.";
    private static final String OK_MESSAGE = "OK: The extraction has finished has expected.";

    /**
     * A typical URL request could be:
     * "http://localhost:8080/extractData?startYear=2015&endYear=2018&extractFromDBLP=false&extractFromGoogleScholar=false"
     */
    @GetMapping("/extractData")
    public RequestStatusResponse extractData(@RequestParam(value = "startYear", defaultValue = "1000") String startYear,
                                             @RequestParam(value = "endYear", defaultValue = "2999") String endYear,
                                             @RequestParam(value = "extractFromDBLP", defaultValue = "true") boolean extractFromDblp,
                                             @RequestParam(value = "extractFromIEEE", defaultValue = "true") boolean extractFromIeee,
                                             @RequestParam(value = "extractFromGoogleScholar", defaultValue = "true") boolean extractFromGoogleScholar) {

        boolean isYearRangeValid = YearRange.isRangeValid(startYear, endYear);

        // If either any parameter is invalid, the request is considered completely invalid as well.
        if (!isYearRangeValid)
            return new RequestStatusResponse(requestId.incrementAndGet(), ERROR_MESSAGE);

        YearRange yearRange =
                new YearRange(Long.valueOf(startYear), Long.valueOf(endYear));

        // TODO: Convert all extractor from sync to async, so that this REST request answers back
        //  immediately to the requester with the corresponding response message.

        if (extractFromDblp)
            DblpExtractor.extractDataIntoWarehouse(yearRange);

        if (extractFromIeee)
            IeeeExtractor.extractDataIntoWarehouse(yearRange);

        if (extractFromGoogleScholar)
            GoogleScholarExtractor.extractDataIntoWarehouse(yearRange);

        return new RequestStatusResponse(requestId.incrementAndGet(), OK_MESSAGE);
    }
}
