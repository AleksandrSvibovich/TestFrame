package pageObject;

public class MainPage {
    private static MainPage mainPage;

    private MainPage() {

    }

    public static MainPage getInstance() {
        if (mainPage == null) {
            mainPage = new MainPage();
        }
        return mainPage;
    }

    public String getInputField() {
        return ".//input[contains(@name,'q')]";
    }

    public String getResultPageElement() {
        return ".//div[@aria-current='page']";
    }
}
