package browser;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Browser {
    protected final int WAIT_TIME_IN_SECONDS;
    private String name;
    private String driverPath;
    private String driver;
    private WebDriver webDriver;
    private WebDriverWait webDriverWait;
    private MutableCapabilities browserOptions;

    public Browser() {
        BrowserProperties browserProperties = new BrowserProperties();
        WAIT_TIME_IN_SECONDS = browserProperties.readWaitTimeProperty();
    }

    public int getWAIT_TIME_IN_SECONDS() {
        return WAIT_TIME_IN_SECONDS;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDriverPath() {
        return driverPath;
    }

    public void setDriverPath(String driverPath) {
        this.driverPath = driverPath;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public WebDriverWait getWebDriverWait() {
        return webDriverWait;
    }

    public void setWebDriverWait(WebDriverWait webDriverWait) {
        this.webDriverWait = webDriverWait;
    }

    public MutableCapabilities getBrowserOptions() {
        return browserOptions;
    }

    public void setBrowserOptions(MutableCapabilities browserOptions) {
        this.browserOptions = browserOptions;
    }

    @Override
    public String toString() {
        return "Browser{" +
                "WAIT_TIME_IN_SECONDS=" + WAIT_TIME_IN_SECONDS +
                ", name='" + name + '\'' +
                ", driverPath='" + driverPath + '\'' +
                ", driver='" + driver + '\'' +
                ", webDriver=" + webDriver +
                ", webDriverWait=" + webDriverWait +
                ", browserOptions=" + browserOptions +
                '}';
    }

}
