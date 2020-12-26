package ieiWarehousePopulator.restService.searchApi;

import ieiWarehousePopulator.domain.Article;
import ieiWarehousePopulator.domain.Book;
import ieiWarehousePopulator.domain.CongressCommunication;
import ieiWarehousePopulator.extractors.utils.YearRange;
import ieiWarehousePopulator.persistence.dataAccessObjects.ArticleDAO;
import ieiWarehousePopulator.persistence.dataAccessObjects.BookDAO;
import ieiWarehousePopulator.persistence.dataAccessObjects.CongressCommunicationDAO;
import ieiWarehousePopulator.restService.requestResponses.RequestResponse;
import ieiWarehousePopulator.restService.requestResponses.RequestResultResponse;
import ieiWarehousePopulator.restService.requestResponses.RequestStatusResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
    public RequestResponse getData(@RequestParam(value = "startYear", defaultValue = "1000") String startYear,
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

        return getResponseContentsFromWarehouse(yearRange,
                author,
                searchArticles,
                searchBooks,
                searchCongressCommunications);
    }

    private RequestResultResponse getResponseContentsFromWarehouse(YearRange yearRange,
                                                                   String author,
                                                                   boolean searchArticles,
                                                                   boolean searchBooks,
                                                                   boolean searchCongressCommunications) {
        List<Article> retrievedArticles = new ArrayList<>();
        List<Book> retrievedBooks = new ArrayList<>();
        List<CongressCommunication> retrievedCongressCommunications = new ArrayList<>();

        if (searchArticles)
            retrievedArticles = ArticleDAO.retrieveArticles(yearRange, author);

        if (searchBooks)
            retrievedBooks = BookDAO.retrieveBooks(yearRange, author);

        if (searchCongressCommunications)
            retrievedCongressCommunications =
                    CongressCommunicationDAO.retrieveCongressCommunications(yearRange, author);

        return new RequestResultResponse(retrievedArticles, retrievedBooks, retrievedCongressCommunications);
    }
}
