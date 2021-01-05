package googleScholarWrapper;

import domainModel.utils.YearRange;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class GoogleScholarWrapper {

    public static void main(String[] args) throws IOException {
        //TODO:Add selenium scrapling and remove getting the objects from sample.bib
        //SeleniumScraper seleniumScraper = new SeleniumScraper();

        //List<String> citationsAsBibtex =
        //        seleniumScraper.retrieveCitationsAsBibtex(fakeYearRange, null);
        //String sample =
        SpringApplication.run(GoogleScholarWrapper.class, args);
    }
}
