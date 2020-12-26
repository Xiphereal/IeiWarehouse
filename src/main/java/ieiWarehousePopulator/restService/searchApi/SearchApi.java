package ieiWarehousePopulator.restService.searchApi;

import ieiWarehousePopulator.extractors.utils.YearRange;
import ieiWarehousePopulator.persistence.dataAccessObjects.ArticleDAO;
import ieiWarehousePopulator.persistence.dataAccessObjects.BookDAO;
import ieiWarehousePopulator.persistence.dataAccessObjects.CongressCommunicationDAO;
import ieiWarehousePopulator.restService.RequestStatusResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
    @GetMapping("/getData")
    public RequestStatusResponse getData(@RequestParam(value = "startYear", defaultValue = "1000") String startYear,
                                         @RequestParam(value = "endYear", defaultValue = "2999") String endYear,
                                         @RequestParam(value = "author", defaultValue = "") String author,
                                         @RequestParam(value = "searchArticles", defaultValue = "true") boolean searchArticles,
                                         @RequestParam(value = "searchCongressCommunications", defaultValue = "true") boolean searchCongressCommunications,
                                         @RequestParam(value = "searchBooks", defaultValue = "true") boolean searchBooks) {

        boolean isYearRangeValid = YearRange.isRangeValid(startYear, endYear);

        // If either any parameter is invalid, the request is considered completely invalid as well.
        if (!isYearRangeValid)
            return new RequestStatusResponse(requestId.incrementAndGet(), ERROR_MESSAGE);

        YearRange yearRange = new YearRange(Long.valueOf(startYear), Long.valueOf(endYear));

        String responseContents = getResponseContentsFromWarehouse(yearRange,
                author,
                searchArticles,
                searchBooks,
                searchCongressCommunications);

        return new RequestStatusResponse(requestId.incrementAndGet(), responseContents);
    }

    private String getResponseContentsFromWarehouse(YearRange yearRange,
                                                    String author,
                                                    boolean searchArticles,
                                                    boolean searchBooks,
                                                    boolean searchCongressCommunications) {
        StringBuilder responseContents = new StringBuilder();

        if (searchArticles) {
            List<ArticleDAO> articleDAOs = ArticleDAO.getArticleDAOs();

            for (ArticleDAO articleDAO : articleDAOs) {
                // TODO: Append each article to the response contents.
            }
        }

        if (searchBooks) {
            List<BookDAO> bookDAOs = BookDAO.getBookDAOs();

            for (BookDAO bookDAO : bookDAOs) {
                // TODO: Append each book to the response contents.
            }
        }

        if (searchCongressCommunications) {
            List<CongressCommunicationDAO> congressCommunicationDAOs =
                    CongressCommunicationDAO.getCongressCommunicationDAOs();

            for (CongressCommunicationDAO congressCommunicationDAO : congressCommunicationDAOs) {
                // TODO: Append each communication congress to the response contents.
            }
        }

        return responseContents.toString();
    }
}
