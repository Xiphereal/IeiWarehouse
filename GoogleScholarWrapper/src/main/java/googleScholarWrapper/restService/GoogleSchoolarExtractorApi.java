package googleScholarWrapper.restService;

import domainModel.utils.YearRange;
import googleScholarWrapper.restService.requestResponses.RequestResponse;
import googleScholarWrapper.restService.requestResponses.RequestStatusResponse;
import googleScholarWrapper.selenium.SeleniumScraper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class GoogleSchoolarExtractorApi {
    private final AtomicLong requestId = new AtomicLong();

    private static final String ERROR_MESSAGE = "ERROR: The given parameters for the search are invalid. " +
            "The years must use format startYear:yyyy and endYear:yyyy";

    @GetMapping("/extract")
    public RequestResponse getData(@RequestParam(value = "startYear", defaultValue = "1000") String startYear,
                                   @RequestParam(value = "endYear", defaultValue = "2999") String endYear) {
        boolean isYearRangeValid = YearRange.isRangeValid(startYear, endYear);
        if (!isYearRangeValid)
            return new RequestStatusResponse(requestId.incrementAndGet(), ERROR_MESSAGE);

        YearRange yearRange = new YearRange(Long.valueOf(startYear), Long.valueOf(endYear));

        SeleniumScraper seleniumScraper = new SeleniumScraper();

        List<String> citationsAsBibtex =
                seleniumScraper.retrieveCitationsAsBibtex(yearRange, null);
        return null;
    }
}
