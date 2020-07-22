package browser;

import org.openqa.selenium.WebDriver;

public class BrowserConstructor {
    BrowserBuilder browserBuilder;

    public void setBrowserBuilder(BrowserBuilder browserBuilder) {
        this.browserBuilder = browserBuilder;
    }

    public Browser getBrowser() {
        return browserBuilder.getBrowser();
    }

    public void createBrowserInstance() {
        browserBuilder.configureName();
        browserBuilder.configureDriverPath();
        browserBuilder.configureBrowserOption();
        browserBuilder.configureDriver();
    }

    public WebDriver initializeWebDriver() {
        browserBuilder.configureWebDriver();
        browserBuilder.configureWebDriverWait();
        return browserBuilder.getBrowser().getWebDriver();
    }

}
