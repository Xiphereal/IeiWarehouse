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

    @GetMapping("/extract")
    public Map<String, Object> extract(@RequestParam(value = "startYear", defaultValue = "1000") int startYear,
                                       @RequestParam(value = "endYear", defaultValue = "2999") int endYear,
                                       @RequestParam(value = "maxPublications", defaultValue = "10") int maxPublications) {

        YearRange yearRange = new YearRange((long) startYear, (long) endYear);

        SeleniumScraper seleniumScraper = new SeleniumScraper();
        List<String> citationsAsBibtex =
                seleniumScraper.retrieveCitationsAsBibtex(yearRange, null);

        return BibtexToJsonParser.toJson(citationsAsBibtex, yearRange, maxPublications).toMap();
    }
}
