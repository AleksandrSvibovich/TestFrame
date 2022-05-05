package UItests;

import io.qameta.allure.*;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;
import pageObject.MainPage;
import steps.CommonSteps;

import static com.codeborne.selenide.Selenide.screenshot;

public class Tests {
    String url = "http://www.google.com";
    private MainPage mainPage;
    private CommonSteps commonSteps;

    @DataProvider(name = "dp")
    public Object[][] dp() {
        return new Object[][]{
                new Object[]{"Штош"},
                new Object[]{"Коты"},
                new Object[]{"Собаки"}
        };
    }

    @BeforeClass
    public void beforeRun() {
        mainPage = MainPage.getInstance();
        commonSteps = CommonSteps.getInstance();
    }

    @Severity(value = SeverityLevel.NORMAL)
    @Test
    @Description(value = "test for opening page")
    public void checkThatPageIsOpened() {
        commonSteps.openPage(url);
        screenshot("my_file_name");
        WebElement input = commonSteps.findElement(mainPage.getInputField());
        Assert.assertTrue(input.isDisplayed());
    }

    @Severity(value = SeverityLevel.CRITICAL)
    @Test
    @Description(value = "test for input")
    public void checkInsertToInput() {
        commonSteps.openPage(url);
        WebElement input = commonSteps.findElement(mainPage.getInputField());
        input.sendKeys("ну штош");
        screenshot("my_file_name2");
        input.sendKeys(Keys.ENTER);
        screenshot("my_file_name3");
        Assert.assertTrue(commonSteps.findElement(mainPage.getResultPageElement()).isDisplayed());
    }

    @Severity(value = SeverityLevel.BLOCKER)
    @Test(dataProvider = "dp", description = "check result of search process")
    @Description(value = "test for search with Data Provider")
    public void checkInsertToInputDP(String inputText) {
        commonSteps.openPage(url);
        WebElement input = commonSteps.findElement(mainPage.getInputField());
        input.sendKeys(inputText);
        input.sendKeys(Keys.ENTER);
        screenshot(inputText);
        Assert.assertTrue(commonSteps.findElement(mainPage.getResultPageElement()).isDisplayed());
    }





    @AfterClass
    public void ShutDown() {
        commonSteps.configBrowser.ShutDown();
    }

}
