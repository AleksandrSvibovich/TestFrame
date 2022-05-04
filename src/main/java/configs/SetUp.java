package configs;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.openqa.selenium.WebDriver;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class SetUp {
    private WebDriver driver;
    private String url = "http://www.google.com";
    private String browserName = "chrome";

    public SetUp(){
        Configuration.baseUrl = url;
        Configuration.browser = browserName;
        Selenide.open();
        driver = getWebDriver();
        driver.manage().window().maximize();
        System.out.println("driver is ready");
    }

    public WebDriver getDriver(){
        return driver;
    }
}
