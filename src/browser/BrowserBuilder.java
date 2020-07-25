package browser;

import exceptions.BrowserErrorException;

public abstract class BrowserBuilder {
    Browser browser;

    public Browser getBrowser() {
        return browser;
    }

    public abstract void configureName();
    public abstract void configureDriverPath();
    public abstract void configureWebDriver() throws BrowserErrorException;
    public abstract void configureWebDriverWait();
    public abstract void configureBrowserOption();
    public abstract void configureDriver();

}
