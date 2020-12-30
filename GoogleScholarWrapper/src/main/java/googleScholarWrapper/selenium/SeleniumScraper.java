package googleScholarWrapper.selenium;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumScraper {
    private static final String PROJECT_PATH = System.getProperty("user.dir") + "/GoogleScholarWrapper/";

    private ChromeDriver driver;

    public void scrap() {
        driver = openChromeInstance();

        openDrawerMenu();

        openAdvancedSearch();
    }

    @NotNull
    private ChromeDriver openChromeInstance() {
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

        waitForWebElement(advancedSearchMenuItem, 500);

        advancedSearchMenuItem.click();
    }

    private void waitForWebElement(WebElement webElement, long timeInMillis) {
        try {
            synchronized (webElement) {
                webElement.wait(timeInMillis);
            }
        } catch (InterruptedException e) {
            System.err.println("A web element await has been interrupted during the Selenium data scraping: ");
            e.printStackTrace();
        }
    }
}
