package com.example.service;

import com.google.common.collect.Iterables;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;

@Service
public class WebScraping {

    private static final Logger log = LoggerFactory.getLogger(WebScraping.class);

    public void start() {
        try {
//            System.setProperty("webdriver.chrome.driver", "/developer/chromedriver_win32/chromedriver.exe");
//            WebDriver driver = new ChromeDriver();
            WebDriver driver = new RemoteWebDriver(new URL("http://localhost:9515"), DesiredCapabilities.chrome());

            // Maximize browser window
            driver.manage().window().maximize();

            // Navigate to URL
            driver.get("https://mvnrepository.com/artifact/org.seleniumhq.selenium/selenium-safari-driver");

            // Read page content
            String content = driver.getPageSource();

            // Print the page content
            System.out.println(content);

            // Find the table element using xpath
            WebElement table = driver.findElement(By.xpath("//*[@id=\"maincontent\"]/div[3]/div/table"));

            // Go through each major version
            List<WebElement> mainVersions = table.findElements(By.tagName("tbody"));

            for(WebElement mver: mainVersions) {
                for(WebElement ver: mver.findElements(By.tagName("tr"))) {
                    List<WebElement> attributes = ver.findElements(By.tagName("a"));
                    WebElement version = attributes.get(0);
                    WebElement repository = attributes.get(1);
                    WebElement usages = attributes.get(2);
                    WebElement date = Iterables.getLast(ver.findElements(By.tagName("td")));

                    System.out.println("Version    : " + version.getText());
                    System.out.println("Repository : " + repository.getText());
                    System.out.println("Usages     : " + usages.getText());
                    System.out.println("Date       : " + date.getText());
                    System.out.println("------------------------------");
                }
            }

            // Close driver
            driver.quit();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
