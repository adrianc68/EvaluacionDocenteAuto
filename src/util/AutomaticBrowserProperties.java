package util;

import browser.BrowserProperties;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutomaticBrowserProperties {
    private static final String PROPERTIES_PATH = "driver/elementsXPath.properties";

    public boolean writeProperties() {
        boolean result = false;
        Properties properties = readProperties();
        try ( OutputStream output = new FileOutputStream(PROPERTIES_PATH) ) {
            properties.clear();
            properties.put("url", "www.google.com" );
            properties.put("username.element.xpath", "test" );
            properties.put("password.element.xpath", "test" );
            properties.put("button.login.element.xpath", "test");
            properties.put("button.next.element.xpath", "test");
            properties.put("professor.element.xpath", "test");
            properties.put("question.element.xpath", "test");
            properties.put("question.option.element.xpath", "test");
            properties.put("popup.element.xpath", "test");
            properties.put("professor.with.id.selection", "test");
            properties.put("selectable.radio.or.checkbutton.element", "test");
            properties.put("size.number.single.question.type", "test");
            properties.put("size.questionnarie.xpath", "test");
            properties.put("span.field.validation.error.with.message.element.xpath", "test");
            properties.store(output, null);
            result = true;
        } catch (IOException io) {
            Logger.getLogger( AutomaticBrowserProperties.class.getName() ).log(Level.SEVERE, null, io);
        }
        return result;
    }

    public String getUrl() {
        Properties properties = readProperties();
        String url = properties.getProperty("url");
        return url;
    }

    public String getUsernameElementXpath() {
        Properties properties = readProperties();
        String usernameXpath = properties.getProperty("username.element.xpath");
        return usernameXpath;
    }

    public String getPasswordElementXPath() {
        Properties properties = readProperties();
        String passwordXPath = properties.getProperty("password.element.xpath");
        return passwordXPath;
    }

    public String getButtonLoginElementXpath() {
        Properties properties = readProperties();
        String buttonLoginXPath = properties.getProperty("button.login.element.xpath");
        return buttonLoginXPath;
    }

    public String getButtonNextElementXpath() {
        Properties properties = readProperties();
        String buttonNextXPath = properties.getProperty("button.next.element.xpath");
        return buttonNextXPath;
    }

    public String getQuestionElementXpath() {
        Properties properties = readProperties();
        String questionXPath = properties.getProperty("question.element.xpath");
        return questionXPath;
    }

    public String getQuestionOptionElementXpath() {
        Properties properties = readProperties();
        String questionOptionXPath = properties.getProperty("question.option.element.xpath");
        return questionOptionXPath;
    }

    public String getProfessorElementXpath() {
        Properties properties = readProperties();
        String professorElement = properties.getProperty("professor.element.xpath");
        return professorElement;
    }

    public String getPopUpElementXPath() {
        Properties properties = readProperties();
        String popupElement = properties.getProperty("popup.element.xpath");
        return popupElement;
    }

    public String getProfessorWithIdSelectionElementXPath() {
        Properties properties = readProperties();
        String professorSelectionElement = properties.getProperty("professor.with.id.selection");
        return professorSelectionElement;
    }

    public String getSelectableRadioOrCheckButtonElementXPath() {
        Properties properties = readProperties();
        String selectableOrCheckButtonElement = properties.getProperty("selectable.radio.or.checkbutton.element");
        return selectableOrCheckButtonElement;
    }

    public String getSizeNumberElementXPath() {
        Properties properties = readProperties();
        String sizeNumberSingleQuestionElement = properties.getProperty("size.number.single.question.type");
        return sizeNumberSingleQuestionElement;
    }

    public String getSizeQuestionnarieXPath() {
        Properties properties = readProperties();
        String sizeQuestionnarieElement = properties.getProperty("size.questionnarie.xpath");
        return sizeQuestionnarieElement;
    }

    public String getSpanFieldValidationErrorXPath() {
        Properties properties = readProperties();
        String spanValidationError = properties.getProperty("span.field.validation.error.with.message.element.xpath");
        return spanValidationError;
    }

    private Properties readProperties() {
        Properties properties = new Properties();
        try ( InputStream input = new FileInputStream(PROPERTIES_PATH) ) {
            properties.load(input);
        } catch (IOException io) {
            Logger.getLogger( AutomaticBrowserProperties.class.getName() ).log(Level.SEVERE, null, io);
        }
        return properties;
    }

}
