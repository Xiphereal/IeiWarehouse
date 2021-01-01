package googleScholarWrapper;

import domainModel.utils.YearRange;
import googleScholarWrapper.selenium.SeleniumScraper;

public class GoogleScholarWrapper {

    public static void main(String[] args) {
        // TODO: Replace the explicit call to scrap() here with the REST API
        //  request and its associated parameters.
        YearRange fakeYearRange = new YearRange();

        SeleniumScraper seleniumScraper = new SeleniumScraper();

        try {
            seleniumScraper.scrap(fakeYearRange, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Terminates the program normally.
            System.exit(0);
        }
    }
}
