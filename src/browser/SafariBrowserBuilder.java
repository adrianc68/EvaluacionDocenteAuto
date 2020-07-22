package browser;

import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SafariBrowserBuilder extends BrowserBuilder {

    public SafariBrowserBuilder() {
        super.browser = new Browser();
    }

    @Override
    public void configureName() {
        browser.setName("safari");
    }

    @Override
    public void configureDriverPath() {
        BrowserProperties browserProperties = new BrowserProperties();
        browser.setDriverPath( browserProperties.readSafariDriverPathProperty() );
    }

    @Override
    public void configureWebDriver() {
        SafariDriver safariDriver = new SafariDriver( browser.getBrowserOptions() );
        browser.setWebDriver(safariDriver);
    }

    @Override
    public void configureWebDriverWait() {
        WebDriverWait webDriverWait = new WebDriverWait( browser.getWebDriver(), browser.WAIT_TIME_IN_SECONDS );
        browser.setWebDriverWait(webDriverWait);
    }

    @Override
    public void configureBrowserOption() {
        SafariOptions safariOptions = new SafariOptions();
        safariOptions.setCapability("safari.cleanSession", true);
        browser.setBrowserOptions(safariOptions);
    }

    @Override
    public void configureDriver() {
        BrowserProperties browserProperties = new BrowserProperties();
        browser.setDriver( browserProperties.readSafariDriverProperty() );
    }
}
