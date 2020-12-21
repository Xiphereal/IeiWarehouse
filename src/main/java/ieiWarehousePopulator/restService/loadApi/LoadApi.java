package ieiWarehousePopulator.restService.loadApi;

import ieiWarehousePopulator.extractors.DblpExtractor;
import ieiWarehousePopulator.extractors.GoogleSchoolarExtractor;
import ieiWarehousePopulator.extractors.IeeeExtractor;
import ieiWarehousePopulator.restService.RequestStatusResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents the endpoint for the load form, receiving the configuration
 * for the {@link ieiWarehousePopulator.extractors.DblpExtractor},
 * {@link ieiWarehousePopulator.extractors.GoogleSchoolarExtractor} and
 * {@link ieiWarehousePopulator.extractors.IeeeExtractor}.
 */
@RestController
public class LoadApi {
    private final AtomicLong requestId = new AtomicLong();

    private static final String ERROR_MESSAGE = "ERROR: The given parameters for the extraction are invalid. " +
            "The expected parameters are 'startYear:yyyy' and 'endYear:yyyy'. " +
            "The valid year range goes from 1000 to 2999.";
    private static final String OK_MESSAGE = "OK: The extraction has finished has expected.";

    @GetMapping("/extractData")
    public RequestStatusResponse extractData(@RequestParam(value = "startYear", defaultValue = "") String startYear,
                                             @RequestParam(value = "endYear", defaultValue = "") String endYear,
                                             @RequestParam(value = "extractFromDBLP", defaultValue = "true") boolean extractFromDblp,
                                             @RequestParam(value = "extractFromIEEE", defaultValue = "true") boolean extractFromIeee,
                                             @RequestParam(value = "extractFromGoogleScholar", defaultValue = "true") boolean extractFromGoogleScholar) {
        boolean isStartYearInvalid = !startYear.isEmpty() && !isYear(startYear);
        boolean isEndYearInvalid = !endYear.isEmpty() && !isYear(endYear);

        // If either any parameter is invalid, the request is considered completely invalid as well.
        if (isStartYearInvalid || isEndYearInvalid)
            return new RequestStatusResponse(requestId.incrementAndGet(), ERROR_MESSAGE);

        if (extractFromDblp)
            // TODO: Support the start and end year data filtering.
            DblpExtractor.extractDataIntoWarehouse();

        if (extractFromIeee)
            // TODO: Support the start and end year data filtering.
            IeeeExtractor.extractDataIntoWarehouse();

        if (extractFromGoogleScholar)
            // TODO: Support the start and end year data filtering.
            GoogleSchoolarExtractor.extractDataIntoWarehouse();

        return new RequestStatusResponse(requestId.incrementAndGet(), OK_MESSAGE);
    }

    private boolean isYear(String input) {
        // REGEX: Numbers from 1000 to 2999.
        return input.matches("^[12][0-9]{3}$");
    }
}
