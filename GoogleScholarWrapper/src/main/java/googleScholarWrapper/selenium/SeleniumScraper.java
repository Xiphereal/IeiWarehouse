package googleScholarWrapper.selenium;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumScraper {
    private static final String PROJECT_PATH = System.getProperty("user.dir") + "/GoogleScholarWrapper/";

    public static void scrap() {
        ChromeDriver chromeDriver = openChromeInstance();

        openDrawerMenu(chromeDriver);

        openAdvancedSearch(chromeDriver);
    }

    private static void openAdvancedSearch(ChromeDriver chromeDriver) {
        WebElement advancedSearchMenuItem = chromeDriver.findElement(By.xpath("//*[@id=\"gs_hp_drw_adv\"]"));
        advancedSearchMenuItem.click();
    }

    @NotNull
    private static ChromeDriver openChromeInstance() {
        System.setProperty("webdriver.chrome.driver", PROJECT_PATH + "src/main/resources/chromedriver_ver87.exe");
        ChromeDriver chromeDriver = new ChromeDriver();
        chromeDriver.get("https://scholar.google.com/");

        return chromeDriver;
    }

    private static void openDrawerMenu(ChromeDriver chromeDriver) {
        WebElement drawerMenuButton = chromeDriver.findElement(By.xpath("//*[@id=\"gs_hdr_mnu\"]"));
        drawerMenuButton.click();
    }
}
