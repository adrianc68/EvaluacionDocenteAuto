package browser;

import exceptions.BrowserErrorException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ChromeBrowserBuilder extends BrowserBuilder {

    public ChromeBrowserBuilder() {
        super.browser = new Browser();
    }

    @Override
    public void configureName() {
        browser.setName("chrome");
    }

    @Override
    public void configureDriverPath() {
        BrowserProperties browserProperties = new BrowserProperties();
        browser.setDriverPath( browserProperties.readChromeDriverPathProperty() );
    }

    @Override
    public void configureWebDriver() throws BrowserErrorException {
        ChromeDriver chromeDriver;
        try{
            chromeDriver = new ChromeDriver( browser.getBrowserOptions() );
        } catch (Exception e) {
            throw new BrowserErrorException("¡No se pudo iniciar el navegador seleccionado", e);
        }
        browser.setWebDriver(chromeDriver);
    }

    @Override
    public void configureWebDriverWait() {
        WebDriverWait webDriverWait = new WebDriverWait( browser.getWebDriver(), browser.WAIT_TIME_IN_SECONDS);
        browser.setWebDriverWait(webDriverWait);
    }

    @Override
    public void configureBrowserOption() {
        ChromeOptions chromeOptions = new ChromeOptions();
        browser.setBrowserOptions(chromeOptions);
    }

    @Override
    public void configureDriver() {
        BrowserProperties browserProperties = new BrowserProperties();
        browser.setDriver( browserProperties.readChromeDriverProperty() );
    }
}
