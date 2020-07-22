package browser;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FirefoxBrowserBuilder extends BrowserBuilder {

    public FirefoxBrowserBuilder() {
        super.browser = new Browser();
    }

    @Override
    public void configureName() {
        browser.setName("gecko");
    }

    @Override
    public void configureDriverPath() {
        BrowserProperties browserProperties = new BrowserProperties();
        browser.setDriverPath( browserProperties.readFirefoxDriverPathProperty() );
    }

    @Override
    public void configureWebDriver() {
        FirefoxDriver firefoxDriver = new FirefoxDriver( browser.getBrowserOptions() );
        browser.setWebDriver(firefoxDriver);
    }

    @Override
    public void configureWebDriverWait() {
        WebDriverWait webDriverWait = new WebDriverWait( browser.getWebDriver(), browser.WAIT_TIME_IN_SECONDS);
        browser.setWebDriverWait(webDriverWait);
    }

    @Override
    public void configureBrowserOption() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        browser.setBrowserOptions(firefoxOptions);
    }

    @Override
    public void configureDriver() {
        BrowserProperties browserProperties = new BrowserProperties();
        browser.setDriver( browserProperties.readFirefoxDriverProperty() );
    }
}
