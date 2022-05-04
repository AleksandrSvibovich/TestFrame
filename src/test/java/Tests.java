
import com.codeborne.selenide.Selenide;
import configs.SetUp;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;
import pageObject.MainPage;

import static com.codeborne.selenide.Selenide.screenshot;


public class Tests {
    String url = "http://www.google.com";
    WebDriver driver;
    private SetUp setUp;
    private MainPage mainPage;


    @BeforeSuite
    public void beforeRun() {
        setUp = new SetUp();
        driver = setUp.getDriver();
        mainPage = MainPage.getInstance();
    }

    @Test
    public void checkInputExist() {
        System.out.println("Thread name - " + Thread.currentThread().getId());
        Selenide.open(url);
        screenshot("my_file_name");
        WebElement input = driver.findElement(By.xpath(mainPage.getInputField()));
        Assert.assertTrue(input.isDisplayed());
    }

    @Test
    public void checkInsertToInput() {
        System.out.println("Thread name - " + Thread.currentThread().getId());
        Selenide.open(url);
        WebElement input = driver.findElement(By.xpath(mainPage.getInputField()));
        input.sendKeys("ну штош");
        screenshot("my_file_name2");
        input.sendKeys(Keys.ENTER);
        screenshot("my_file_name3");
        Assert.assertTrue(driver.findElement(By.xpath(mainPage.getResultPageElement())).isDisplayed());
    }

    @AfterSuite
    public void ShutDown() {
        driver.quit();
        System.out.println("driver is terminated");

    }

}
