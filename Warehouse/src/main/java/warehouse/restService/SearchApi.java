package warehouse.restService;

import domainModel.Article;
import domainModel.Book;
import domainModel.CongressCommunication;
import domainModel.Person;
import domainModel.utils.YearRange;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import warehouse.persistence.dataAccessObjects.ArticleDAO;
import warehouse.persistence.dataAccessObjects.BookDAO;
import warehouse.persistence.dataAccessObjects.CongressCommunicationDAO;
import domainModel.requestResponses.RequestResponse;
import domainModel.requestResponses.RequestResultResponse;

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

    /**
     * A typical URL request could be:
     * "http://localhost:8080/getData?startYear=2015&endYear=2018"
     */
    @GetMapping("/getData")
    public RequestResponse getData(@RequestParam(value = "startYear", defaultValue = "1000") String startYear,
                                   @RequestParam(value = "endYear", defaultValue = "2999") String endYear,
                                   @RequestParam(value = "author", defaultValue = "") String author,
                                   @RequestParam(value = "searchArticles", defaultValue = "true") boolean searchArticles,
                                   @RequestParam(value = "searchCongressCommunications", defaultValue = "true") boolean searchCongressCommunications,
                                   @RequestParam(value = "searchBooks", defaultValue = "true") boolean searchBooks) {

        long startYearValue = YearRange.isYear(startYear) ? Long.parseLong(startYear) : 1000L;
        long endYearValue = YearRange.isYear(endYear) ? Long.parseLong(endYear) : 2999L;

        YearRange yearRange = new YearRange(startYearValue, endYearValue);

        Person requestedAuthor = null;

        if (!author.isEmpty() && !author.isBlank())
            requestedAuthor = Person.extractPersonAttributes(author);

        return getDataFromWarehouse(yearRange,
                requestedAuthor,
                searchArticles,
                searchBooks,
                searchCongressCommunications);
    }

    private RequestResultResponse getDataFromWarehouse(YearRange yearRange,
                                                       Person requestedAuthor,
                                                       boolean searchArticles,
                                                       boolean searchBooks,
                                                       boolean searchCongressCommunications) {
        List<Article> retrievedArticles = new ArrayList<>();
        List<Book> retrievedBooks = new ArrayList<>();
        List<CongressCommunication> retrievedCongressCommunications = new ArrayList<>();

        if (searchArticles)
            retrievedArticles = ArticleDAO.retrieveArticles(yearRange, requestedAuthor);

        if (searchBooks)
            retrievedBooks = BookDAO.retrieveBooks(yearRange, requestedAuthor);

        if (searchCongressCommunications)
            retrievedCongressCommunications =
                    CongressCommunicationDAO.retrieveCongressCommunications(yearRange, requestedAuthor);

        return new RequestResultResponse(retrievedArticles, retrievedBooks, retrievedCongressCommunications);
    }
}
