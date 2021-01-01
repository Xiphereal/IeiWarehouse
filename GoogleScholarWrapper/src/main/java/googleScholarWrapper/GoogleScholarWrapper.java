package googleScholarWrapper;

import domainModel.utils.YearRange;
import googleScholarWrapper.selenium.SeleniumScraper;

import java.util.List;

public class GoogleScholarWrapper {

    public static void main(String[] args) {
        // TODO: Replace the explicit call to scrap() here with the REST API
        //  request and its associated parameters.
        YearRange fakeYearRange = new YearRange();

        SeleniumScraper seleniumScraper = new SeleniumScraper();

        try {
            List<String> citationsAsBibtex =
                    seleniumScraper.retrieveCitationsAsBibtex(fakeYearRange, null);

            for (String citation : citationsAsBibtex) {
                System.out.println(citation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Terminates the program normally.
            System.exit(0);
        }
        // Terminates the program normally.
        // It seems that Selenium Web Driver keeps the main execution thread running
        // even when finishing normally.
        System.exit(0);
    }
}
