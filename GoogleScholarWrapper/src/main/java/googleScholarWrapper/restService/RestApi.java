package googleScholarWrapper.restService;

import domainModel.utils.YearRange;
import googleScholarWrapper.bibtexToJson.BibtexToJsonParser;
import googleScholarWrapper.selenium.SeleniumScraper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class RestApi {

    //TODO:Remove getting the string from file and add selenium scrapling
    @GetMapping("/extract")
    public Map<String, Object> extract(@RequestParam(value = "startYear", defaultValue = "1000") String startYear,
                                       @RequestParam(value = "endYear", defaultValue = "2999") String endYear,
                                       @RequestParam(value = "maxPublications", defaultValue = "10") String maxPublications) {

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
