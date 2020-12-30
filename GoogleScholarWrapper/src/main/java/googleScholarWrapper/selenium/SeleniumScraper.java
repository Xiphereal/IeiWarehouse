package googleScholarWrapper.selenium;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SeleniumScraper {
    private static final String PROJECT_PATH = System.getProperty("user.dir") + "/GoogleScholarWrapper/";
    public static final int AWAIT_TIMEOUT_IN_MILLIS = 5000; // Maximum time to wait before failing.

    private ChromeDriver driver;

    public void scrap() {
        driver = openChromeInstanceWithGoogleScholar();

        openDrawerMenu();

        openAdvancedSearch();

        enterAdvancedSearchOptions();

        performAdvancedSearch();
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

    private void waitUntilClickable(WebElement webElement) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(AWAIT_TIMEOUT_IN_MILLIS));
        wait.until(ExpectedConditions.elementToBeClickable(webElement));
    }

    // TODO: Parameterize the search options.
    private void enterAdvancedSearchOptions() {
        WebElement authorTextBox = driver.findElement(By.xpath("//*[@id=\"gs_asd_sau\"]"));
        authorTextBox.sendKeys("\"" + "Author Name" + "\"");

        WebElement startYearTextBox = driver.findElement(By.xpath("//*[@id=\"gs_asd_ylo\"]"));
        startYearTextBox.sendKeys("2013");

        WebElement endYearTextBox = driver.findElement(By.xpath("//*[@id=\"gs_asd_yhi\"]"));
        endYearTextBox.sendKeys("2015");
    }

    private void performAdvancedSearch() {
        WebElement searchButton = driver.findElement(By.xpath("//*[@id=\"gs_asd_psb\"]"));
        searchButton.click();
    }
}
