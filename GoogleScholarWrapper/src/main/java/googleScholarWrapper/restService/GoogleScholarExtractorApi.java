package googleScholarWrapper.restService;

import domainModel.utils.YearRange;
import googleScholarWrapper.bibtexToJson.BibtexToJsonParser;
import googleScholarWrapper.restService.requestResponses.RequestStatusResponse;
import googleScholarWrapper.selenium.SeleniumScraper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GoogleScholarExtractorApi {
    private final AtomicLong requestId = new AtomicLong();

    private static final String ERROR_MESSAGE = "ERROR: The given parameters for the search are invalid. " +
            "The years must use format startYear:yyyy and endYear:yyyy";

    //TODO:Remove getting the string from file and add selenium scrapling
    @GetMapping("/extract")
    public Map<String, Object> getData(@RequestParam(value = "startYear", defaultValue = "1000") String startYear,
                                       @RequestParam(value = "endYear", defaultValue = "2999") String endYear,
                                       @RequestParam(value = "maxPublications", defaultValue = "10") String maxPublications) throws IOException {

        //String string = Files.readString(Path.of("D:\\IdeaProjects\\IeiWarehouse\\GoogleScholarWrapper\\src\\main\\resources\\sample.bib"));
        boolean isYearRangeValid = YearRange.isRangeValid(startYear, endYear);

        if (!isYearRangeValid)
            return null;

        YearRange yearRange = new YearRange(Long.valueOf(startYear), Long.valueOf(endYear));

        SeleniumScraper seleniumScraper = new SeleniumScraper();
        List<String> citationsAsBibtex =
                seleniumScraper.retrieveCitationsAsBibtex(yearRange, null);

        // When using selenium we wont need to split the string since we will get a List<String>
        return BibtexToJsonParser.toJson(citationsAsBibtex,
                new YearRange(Long.parseLong(startYear), Long.parseLong(endYear)),
                Integer.parseInt(maxPublications)).toMap();
    }
}
