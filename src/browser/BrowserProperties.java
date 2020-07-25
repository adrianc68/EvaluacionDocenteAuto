package browser;

import util.SystemInformation;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BrowserProperties {
    private static final String PROPERTIES_PATH = "configuration/driver/configuration.properties";

    public boolean writeProperties() {
        boolean result = false;
        Properties properties = readProperties();
        try ( OutputStream output = new FileOutputStream(PROPERTIES_PATH) ) {
            properties.clear();
            properties.put("browser.selected.wait.time", "10" );
            properties.put("browser.firefox.windows.path", "driver/firefox/windows/geckodriver.exe" );
            properties.put("browser.chrome.windows.path", "driver/chrome/windows/chromedriver.exe" );
            properties.put("browser.chrome.mac.path", "driver/chrome/mac/chromedriver" );
            properties.put("browser.firefox.mac.path", "driver/firefox/mac/geckodriver" );
            properties.put("browser.safari.path", "/System/Library/CoreServices/SafariSupport.bundle/Contents/MacOS/safaridriver");
            properties.put("browser.firefox.driver", "webdriver.gecko.driver");
            properties.put("browser.chrome.driver", "webdriver.chrome.driver");
            properties.put("browser.safari.driver", "webdriver.safari.driver");
            properties.store(output, null);
            result = true;
        } catch (IOException io) {
            Logger.getLogger( BrowserProperties.class.getName() ).log(Level.SEVERE, null, io);
        }
        return result;
    }

    public String readSafariDriverPathProperty() {
        Properties properties = readProperties();
        return properties.getProperty("browser.safari.path");
    }

    public String readFirefoxDriverPathProperty() {
        Properties properties = readProperties();
        String operatingSystem = SystemInformation.getOSName().toUpperCase();
        String driverPath;
        if( operatingSystem.contains("MAC") ) {
            driverPath = properties.getProperty("browser.firefox.mac.path");
        } else {
            driverPath = properties.getProperty("browser.firefox.windows.path");
        }
        return driverPath;
    }

    public String readChromeDriverPathProperty() {
        Properties properties = readProperties();
        String operatingSystem = SystemInformation.getOSName().toUpperCase();
        String driverPath;
        if( operatingSystem.contains("MAC") ) {
            driverPath = properties.getProperty("browser.chrome.mac.path");
        } else {
            driverPath = properties.getProperty("browser.chrome.windows.path");
        }
        return driverPath;
    }

    public int readWaitTimeProperty() {
        Properties properties = readProperties();
        return Integer.parseInt( properties.getProperty("browser.selected.wait.time") );
    }

    public String readSafariDriverProperty() {
        Properties properties = readProperties();
        return properties.getProperty("browser.safari.driver");
    }

    public String readFirefoxDriverProperty() {
        Properties properties = readProperties();
        return properties.getProperty("browser.firefox.driver");
    }

    public String readChromeDriverProperty() {
        Properties properties = readProperties();
        return properties.getProperty("browser.chrome.driver");
    }

    private Properties readProperties() {
        Properties properties = new Properties();
        try ( InputStream input = new FileInputStream(PROPERTIES_PATH) ) {
            properties.load(input);
        } catch (IOException io) {
            Logger.getLogger( BrowserProperties.class.getName() ).log(Level.SEVERE, null, io);
        }
        return properties;
    }

}
