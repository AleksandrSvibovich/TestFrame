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
import steps.CommonSteps;

import static com.codeborne.selenide.Selenide.screenshot;

public class Test2 {
    String url = "http://www.google.com";
    WebDriver driver;
    private SetUp setUp;
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
        setUp = new SetUp();
        driver = setUp.getDriver();
        mainPage = MainPage.getInstance();
        commonSteps = CommonSteps.getInstance();
    }
    @Test
    public void checkInputExist() {
        commonSteps.openPage(url);
        screenshot("my_file_name");
        WebElement input = commonSteps.findElement(mainPage.getInputField());
        Assert.assertTrue(input.isDisplayed());
    }

    @Test
    public void checkInsertToInput() {
        commonSteps.openPage(url);
        WebElement input = commonSteps.findElement(mainPage.getInputField());
        input.sendKeys("ну штош");
        screenshot("my_file_name2");
        input.sendKeys(Keys.ENTER);
        screenshot("my_file_name3");
        Assert.assertTrue(driver.findElement(By.xpath(mainPage.getResultPageElement())).isDisplayed());
    }

    @Severity(value = SeverityLevel.BLOCKER)
    @Test(dataProvider = "dp", description = "check result of search process")
    @Description(value = "Check that image exist")
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
        driver.quit();
        System.out.println("driver is terminated");

    }

}
