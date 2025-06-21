package com.balsam.automation.core;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.nio.file.Files;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseTest {

    protected static WebDriver driver;
    protected static String baseUrl;
    public static Logger log = LogManager.getLogger();
	static WebDriverWait wait;

    @BeforeEach
    public void setUp() throws Exception {
        loadProperties();
        launchBrowser();
    }

    @AfterEach
    public void tearDown() {
        // Cleanup code after each test
        if (driver != null) {
            driver.quit();
        }
    }

    public void loadProperties() throws IOException {
        Properties props = new Properties();
        props.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
        baseUrl = props.getProperty("base.url");
    }
    

	public void clickElement(String xpath) {
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
		driver.findElement(By.xpath(xpath)).click();
		log.info("Clicked element with xpath: {}", xpath);
	}

	public WebElement getElementByXpath(String xpath) {
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
		return driver.findElement(By.xpath(xpath));
	}

    public WebElement getNthElementByXpath(String xpath, int n) {
        String indexedXpath = "(" + xpath + ")[" + n + "]";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(indexedXpath)));
        return driver.findElement(By.xpath(indexedXpath));
    }

	public static void launchBrowser() {
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		log.info("Launching: {}", baseUrl);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		driver.get(baseUrl);
	}

	public static void closeBrowser() {
		log.info("Closing: {} ", baseUrl);
		driver.quit();
	}

	public void verifyElementIsDisplayed(String xpath) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		assertTrue(driver.findElement(By.xpath(xpath)).isDisplayed());
	}

	public void verifyElementIsNotDisplayed(String xpath) {
		assertTrue(driver.findElements(By.xpath(xpath)).size() == 0);
	}

	public void enterText(String xpath, String text) {
		WebElement element = driver.findElement(By.xpath(xpath));
		element.clear();
		element.sendKeys(text);
	}

	public String getText(String xpath) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		return driver.findElement(By.xpath(xpath)).getText();
	}

	public void closeCookieBannerIfPresent() {
    List<WebElement> banners = driver.findElements(By.id("cookieBanner"));
    if (!banners.isEmpty() && banners.get(0).isDisplayed()) {
        // Try to find and click the accept/close button inside the banner
        List<WebElement> buttons = banners.get(0).findElements(By.xpath(".//button"));
        if (!buttons.isEmpty()) {
            buttons.get(0).click();
        } else {
            // Or hide the banner with JS if no button is found
            ((JavascriptExecutor) driver).executeScript("arguments[0].style.display='none';", banners.get(0));
        }
        // Optionally wait for the banner to disappear
        new WebDriverWait(driver, Duration.ofSeconds(5))
            .until(ExpectedConditions.invisibilityOf(banners.get(0)));
    }
}
public void scrollIntoView(String xpath) {
    WebElement element = driver.findElement(By.xpath(xpath));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
}

	public static JsonNode loadJson(String fileName) {
		JsonNode jn = null;
		try {
			String str = new String(Files.readAllBytes(Paths.get(fileName)));
			ObjectMapper mapper = new ObjectMapper();
			jn = mapper.readValue(str, JsonNode.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jn;
	}

    public void clickIfExists(String xpath) {
    List<WebElement> elements = driver.findElements(By.xpath(xpath));
    if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
        elements.get(0).click();
        log.info("Clicked element if exists: {}", xpath);
    }
}

	public void closeAnyPopUps() {
    // Example: Close DY Guide Popup by ID
    try {
        List<WebElement> popups = driver.findElements(By.xpath("//*[contains(@id,'dy-guide-popup')]"));
        if (!popups.isEmpty() && popups.get(0).isDisplayed()) {
            // Try to find a close button inside the popup
            List<WebElement> closeBtns = popups.get(0).findElements(By.xpath(".//button[contains(@class,'close')]"));
            if (!closeBtns.isEmpty()) {
                closeBtns.get(0).click();
                wait.until(ExpectedConditions.invisibilityOf(popups.get(0)));
            } else {
                // Fallback: hide with JS
                ((JavascriptExecutor) driver).executeScript("arguments[0].style.display='none';", popups.get(0));
            }
        }
    } catch (Exception ignored) {}
}
}