package googleScholarWrapper;

import googleScholarWrapper.selenium.SeleniumScraper;

public class GoogleScholarWrapper {

    public static void main(String[] args) {
        SeleniumScraper seleniumScraper = new SeleniumScraper();
        seleniumScraper.scrap();

        // Terminates the program normally.
        System.exit(0);
    }
}
