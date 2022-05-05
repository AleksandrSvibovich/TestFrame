package steps;

import com.codeborne.selenide.Selenide;
import configs.SetUp;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pageObject.MainPage;

public class CommonSteps {
    private static CommonSteps commonSteps;
    private SetUp setUp;
    private WebDriver driver;

    private CommonSteps() {
        setUp = new SetUp();
        driver = setUp.getDriver();
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
