package googleScholarWrapper.selenium;

import domainModel.Person;
import domainModel.utils.YearRange;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SeleniumScraper {
    private static final String PROJECT_PATH = System.getProperty("user.dir") + "/GoogleScholarWrapper/";

    /**
     * Maximum time to wait before failing.
     */
    private static final int AWAIT_TIMEOUT_IN_MILLIS = 5000;

    private ChromeDriver driver;

    private final List<String> searchResultsAsBibtex;

    public SeleniumScraper() {
        searchResultsAsBibtex = new ArrayList<>();
    }

    public List<String> retrieveCitationsAsBibtex(YearRange yearRange, Person requestedAuthor) {
        driver = openChromeInstanceWithGoogleScholar();

        openDrawerMenu();
        openAdvancedSearch();
        enterAdvancedSearchOptions(yearRange, requestedAuthor);
        performAdvancedSearch();

        scrapCitationsAsBibtex();

        return searchResultsAsBibtex;
    }

    @NotNull
    private ChromeDriver openChromeInstanceWithGoogleScholar() {
        System.setProperty("webdriver.chrome.driver", PROJECT_PATH + "src/main/resources/chromedriver_ver87.exe");

        ChromeDriver chromeDriver = new ChromeDriver();
        chromeDriver.get("https://scholar.google.com/");

        return chromeDriver;
    }

    private void openDrawerMenu() {
        WebElement drawerMenuButton = driver.findElement(By.xpath("//*[@id=\"gs_hdr_mnu\"]"));
        drawerMenuButton.click();
    }

    private void openAdvancedSearch() {
        WebElement advancedSearchMenuItem = driver.findElement(By.xpath("//*[@id=\"gs_hp_drw_adv\"]"));

        waitUntilClickable(advancedSearchMenuItem);

        advancedSearchMenuItem.click();
    }

    private void enterAdvancedSearchOptions(YearRange yearRange, Person requestedAuthor) {
        if (requestedAuthor != null) {
            WebElement authorTextBox = driver.findElement(By.xpath("//*[@id=\"gs_asd_sau\"]"));
            authorTextBox.sendKeys("\"" + requestedAuthor.getFullName() + "\"");
        }

        // If no year range has been specified, the default values for the
        // start and end year are 1000 and 2999 respectively.
        if (yearRange == null) {
            yearRange = new YearRange();
        }

        WebElement startYearTextBox = driver.findElement(By.xpath("//*[@id=\"gs_asd_ylo\"]"));
        startYearTextBox.sendKeys(yearRange.getStartYear().toString());

        WebElement endYearTextBox = driver.findElement(By.xpath("//*[@id=\"gs_asd_yhi\"]"));
        endYearTextBox.sendKeys(yearRange.getEndYear().toString());
    }

    private void performAdvancedSearch() {
        WebElement searchButton = driver.findElement(By.xpath("//*[@id=\"gs_asd_psb\"]"));
        searchButton.click();
    }

    private void scrapCitationsAsBibtex() {
        List<WebElement> searchResults = getSearchResults();

        // Even though the desired and (presumably) move efficient behaviour would be
        // to just iterate through the only-once-retrieved search results, it seems
        // impossible since a WebElement is a reference to a DOM element and the DOM
        // is not the same when the page is exited.
        // If there's a better a approach, don't hesitate to replace the current with it.
        for (int i = 0; i < searchResults.size(); i++) {
            WebElement result = searchResults.get(i);
            WebElement quoteButton = result.findElement(By.xpath(".//a[@class='gs_or_cit gs_nph']"));
            quoteButton.click();

            waitForMillis(500);

            WebElement bibtexLink = driver.findElement(By.xpath("//div[@id='gs_citi']/a[1]"));
            waitUntilClickable(bibtexLink);
            bibtexLink.click();

            WebElement publicationCitationInBibtex = driver.findElement(By.xpath("/html/body/pre"));
            searchResultsAsBibtex.add(publicationCitationInBibtex.getText());

            driver.navigate().back();

            WebElement quoteDialogCancelButton = driver.findElement(By.xpath("//*[@id=\"gs_cit-x\"]"));
            quoteDialogCancelButton.click();

            // Reload the reference to the DOM elements: the search results.
            searchResults = getSearchResults();
        }
    }

    private List<WebElement> getSearchResults() {
        return driver.findElements(By.xpath("//*[@id=\"gs_res_ccl_mid\"]/div"));
    }

    private void waitForMillis(long timeInMillis) {
        try {
            synchronized (driver) {
                driver.wait(timeInMillis);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void waitUntilClickable(WebElement webElement) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(AWAIT_TIMEOUT_IN_MILLIS));
        wait.until(ExpectedConditions.elementToBeClickable(webElement));
    }
}
