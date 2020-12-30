package googleScholarWrapper.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumScraper {
    private static final String PROJECT_PATH = System.getProperty("user.dir") + "/GoogleScholarWrapper/";

    public static void scrap() {
        System.setProperty("webdriver.chrome.driver", PROJECT_PATH + "src/main/resources/chromedriver_ver87.exe");
        ChromeDriver chromeDriver = new ChromeDriver();
        chromeDriver.get("https://www.google.com");

        WebElement searchTextBox = chromeDriver.findElement(By.name("q"));
        searchTextBox.sendKeys("Selenium 4 works as charms! :)" + Keys.ENTER);

        chromeDriver.quit();
    }
}
