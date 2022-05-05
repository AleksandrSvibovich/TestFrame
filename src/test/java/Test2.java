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

    @BeforeClass
    public void beforeRun() {
        setUp = new SetUp();
        driver = setUp.getDriver();
        mainPage = MainPage.getInstance();
    }
    @Test
    public void checkInputExist() {
        openPage(url);
        screenshot("my_file_name");
        WebElement input = findElement(mainPage.getInputField());
        Assert.assertTrue(input.isDisplayed());
    }

    @Test
    public void checkInsertToInput() {
        openPage(url);
        WebElement input = findElement(mainPage.getInputField());
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

    @AfterClass
    public void ShutDown() {
        driver.quit();
        System.out.println("driver is terminated");

    }

}
