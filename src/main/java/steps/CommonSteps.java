package steps;

import com.codeborne.selenide.Selenide;
import configs.ConfigBrowser;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CommonSteps {
    private static CommonSteps commonSteps;
    public ConfigBrowser configBrowser;
    private WebDriver driver;

    private CommonSteps() {
        configBrowser = new ConfigBrowser();
        driver = configBrowser.getDriver();
    }

    public static CommonSteps getInstance() {
        if (commonSteps == null) {
            commonSteps = new CommonSteps();
        }
        return commonSteps;
    }

    @Step("find element by id - {inputField}")
    public WebElement findElement(String inputField) {
        return driver.findElement(By.xpath(inputField));
    }

    @Step("Open url in browser - {url}")
    public void openPage(String url) {
        Selenide.open(url);
    }

}
