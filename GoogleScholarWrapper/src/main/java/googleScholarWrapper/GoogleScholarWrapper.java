package googleScholarWrapper;

import googleScholarWrapper.selenium.SeleniumScraper;

public class GoogleScholarWrapper {

    public static void main(String[] args) {
        SeleniumScraper.scrap();

        // Terminates the program normally.
        System.exit(0);
    }
}
