package googleScholarWrapper.selenium;

import domainModel.Person;
import domainModel.utils.YearRange;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class SeleniumScraper {
    private static final String SCREENSHOTS_FILE_PATH = System.getProperty("user.dir")
            + "/IeiExtractorProject/Selenium/ErrorScreenshots";

    /**
     * Maximum time to wait before failing.
     */
    private static final int AWAIT_TIMEOUT_IN_SECONDS = 5;
    private static final int NUMBER_OF_RESULTS_PAGES_TO_SCRAP = 2;

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

        try {
            scrapCitationsAsBibtex();
        } catch (Exception e) {
            System.err.println(System.lineSeparator() +
                    "An error has occurred while scrapping citations from Google Scholar. " +
                    "A screenshot has been saved to: " + SCREENSHOTS_FILE_PATH);

            e.printStackTrace();

            saveScreenshot();
        } finally {
            driver.quit();
        }

        return searchResultsAsBibtex;
    }

    @NotNull
    private ChromeDriver openChromeInstanceWithGoogleScholar() {
        System.setProperty("webdriver.chrome.driver", getClass().getResource("/chromedriver_ver87.exe").getPath());

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
            yearRange = new YearRange(1000L, 2999L);
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
        scrapResultsInCurrentResultPage();

        // If there is more than one number of result pages requested, the consequent
        // pages will be also scrapped. If not, the scrapping is already done.
        if (NUMBER_OF_RESULTS_PAGES_TO_SCRAP <= 1)
            return;

        for (int nextResultsPageIndex = 2;
             nextResultsPageIndex <= NUMBER_OF_RESULTS_PAGES_TO_SCRAP;
             nextResultsPageIndex++) {
            scrapResultsInCurrentResultPage();

            WebElement nextResultsPageButton = getSearchResultsPages().get(nextResultsPageIndex);
            nextResultsPageButton.click();
        }
    }

    /**
     * Even though the desired and (presumably) move efficient behaviour would be
     * to just iterate through the only-once-retrieved search results, it seems
     * impossible since a WebElement is a reference to a DOM element and the DOM
     * is not the same when the page is exited.
     * If there's a better a approach, don't hesitate to replace the current with it.
     */
    private void scrapResultsInCurrentResultPage() {
        List<WebElement> searchResults = getSearchResults();

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
            waitUntilClickable(quoteDialogCancelButton);
            quoteDialogCancelButton.click();

            // Reload the reference to the DOM elements: the search results.
            searchResults = getSearchResults();
        }
    }

    private List<WebElement> getSearchResultsPages() {
        return driver.findElements(By.xpath("//*[@id=\"gs_n\"]/center/table/tbody/tr/td"));
    }

    private List<WebElement> getSearchResults() {
        return driver.findElements(By.xpath("//*[@id=\"gs_res_ccl_mid\"]/div"));
    }

    private void saveScreenshot() {
        File screenshot = driver.getScreenshotAs(OutputType.FILE);

        try {
            Files.copy(Paths.get(screenshot.getPath()),
                    Paths.get(SCREENSHOTS_FILE_PATH + "selenium_error_screenshot.png"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        WebDriverWait wait = new WebDriverWait(driver, AWAIT_TIMEOUT_IN_SECONDS);
        wait.until(ExpectedConditions.elementToBeClickable(webElement));
    }
}
