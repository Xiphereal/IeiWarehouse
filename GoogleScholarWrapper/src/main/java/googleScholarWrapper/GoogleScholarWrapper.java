package googleScholarWrapper;

import domainModel.Person;
import domainModel.utils.YearRange;
import googleScholarWrapper.selenium.SeleniumScraper;

public class GoogleScholarWrapper {

    public static void main(String[] args) {
        // TODO: Replace the explicit call to scrap() here with the REST API
        //  request and its associated parameters.
        YearRange fakeYearRange = new YearRange();
        Person fakeRequestedAuthor = new Person("FakeName", "FakeSurnames");

        SeleniumScraper seleniumScraper = new SeleniumScraper();
        seleniumScraper.scrap(fakeYearRange, fakeRequestedAuthor);

        // Terminates the program normally.
        System.exit(0);
    }
}
