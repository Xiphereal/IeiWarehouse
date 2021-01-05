package googleScholarWrapper;

import domainModel.utils.YearRange;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class GoogleScholarWrapper {

    public static void main(String[] args) throws IOException {
        // TODO: Replace the explicit call to scrap() here with the REST API
        //  request and its associated parameters.
        YearRange fakeYearRange = new YearRange();

        //SeleniumScraper seleniumScraper = new SeleniumScraper();

        //List<String> citationsAsBibtex =
        //        seleniumScraper.retrieveCitationsAsBibtex(fakeYearRange, null);
        //String sample =
        SpringApplication.run(GoogleScholarWrapper.class, args);
        // Terminates the program normally.
        // It seems that Selenium Web Driver keeps the main execution thread running
        // even when finishing normally.
    }
}
