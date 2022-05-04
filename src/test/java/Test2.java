import com.codeborne.selenide.Selenide;
import configs.SetUp;
import io.qameta.allure.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;
import pageObject.MainPage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.codeborne.selenide.Selenide.screenshot;

public class Test2 {
    String url = "http://www.google.com";
    WebDriver driver;
    private SetUp setUp;
    private MainPage mainPage;

    @DataProvider(name = "dp")
    public Object[][] dp() {
        return new Object[][]{
                new Object[]{"Штош"},
                new Object[]{"Коты"},
                new Object[]{"Собаки"}
        };
    }

    @BeforeSuite
    public void beforeRun() {
        setUp = new SetUp();
        driver = setUp.getDriver();
        mainPage = MainPage.getInstance();
    }


    @Flaky
    @Severity(value = SeverityLevel.BLOCKER)
    @Test(dataProvider = "dp", description = "check result of search process")
    @Description(value = "Check that image exist")
    public void checkInsertToInput(String inputText) {
        openPage(url);
        WebElement input = findElement(mainPage.getInputField());
        input.sendKeys(inputText);
        input.sendKeys(Keys.ENTER);
        screenshot(inputText);
        Assert.assertTrue(findElement(mainPage.getResultPageElement()).isDisplayed());
    }



    @Step("find element by id - {inputField}")
    private WebElement findElement(String inputField) {
        return driver.findElement(By.xpath(inputField));
    }

    @Step("Open url in browser - {url}")
    private void openPage(String url) {
        Selenide.open(url);
    }

    @AfterSuite
    public void ShutDown() {
        driver.quit();
        System.out.println("driver is terminated");

    }

}
