package util;

import browser.Browser;
import browser.BrowserConstructor;
import browser.ChromeBrowserBuilder;
import browser.FirefoxBrowserBuilder;
import browser.SafariBrowserBuilder;
import evaluation.AnswerElement;
import evaluation.ProfessorElement;
import evaluation.QuestionElement;
import evaluation.QuestionType;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AutomaticBrowser {
    private final String URL;
    private final String USERNAME_ELEMENT_XPATH;
    private final String PASSWORD_ELEMENT_XPATH;
    private final String BUTTON_LOGIN_ELEMENT_XPATH;
    private final String BUTTON_NEXT_ELEMENT_XPATH;
    private final String PROFESSOR_ELEMENT_XPATH;
    private final String QUESTION_ELEMENT_XPATH;
    private final String QUESTION_OPTION_ELEMENT_XPATH;
    private final String POPUP_ELEMENT_XPATH;
    private final String PROFESSOR_WITH_ID_SELECTION_XPATH;
    private final String SELECTABLE_RADIO_OR_CHECKBUTTON_XPATH;
    private final int SIZE_NUMBER_SINGLE_QUESTION;
    private final String ACCOUNT;
    private final String PASSWORD;
    private Browser browser;

    public AutomaticBrowser(String account, String password) {
        AutomaticBrowserProperties automaticBrowserProperties = new AutomaticBrowserProperties();
        URL =automaticBrowserProperties.getUrl();
        USERNAME_ELEMENT_XPATH = automaticBrowserProperties.getUsernameElementXpath();
        PASSWORD_ELEMENT_XPATH = automaticBrowserProperties.getPasswordElementXPath();
        BUTTON_LOGIN_ELEMENT_XPATH = automaticBrowserProperties.getButtonLoginElementXpath();
        BUTTON_NEXT_ELEMENT_XPATH = automaticBrowserProperties.getButtonNextElementXpath();
        PROFESSOR_ELEMENT_XPATH = automaticBrowserProperties.getProfessorElementXpath();
        QUESTION_ELEMENT_XPATH = automaticBrowserProperties.getQuestionElementXpath();
        QUESTION_OPTION_ELEMENT_XPATH = automaticBrowserProperties.getQuestionOptionElementXpath();
        POPUP_ELEMENT_XPATH = automaticBrowserProperties.getPopUpElementXPath();
        PROFESSOR_WITH_ID_SELECTION_XPATH = automaticBrowserProperties.getProfessorWithIdSelectionElementXPath();
        SELECTABLE_RADIO_OR_CHECKBUTTON_XPATH = automaticBrowserProperties.getSelectableRadioOrCheckButtonElementXPath();
        SIZE_NUMBER_SINGLE_QUESTION = Integer.parseInt( automaticBrowserProperties.getSizeNumberElementXPath() );
        this.ACCOUNT = account;
        this.PASSWORD = password;
    }

    private void navigateInLoginPage() {
        WebDriver driver = browser.getWebDriver();
        driver.get(URL);
        driver.findElement(By.xpath(USERNAME_ELEMENT_XPATH)).sendKeys(ACCOUNT);
        driver.findElement(By.xpath(PASSWORD_ELEMENT_XPATH)).sendKeys(PASSWORD);
        driver.findElement(By.xpath(BUTTON_LOGIN_ELEMENT_XPATH)).click();
    }

    private List<WebElement> findElements(String xpath) {
        WebDriver driver = browser.getWebDriver();
        List<WebElement> list = driver.findElements(By.xpath(xpath));
        return list;
    }

    private WebElement findElement(String xpath) {
        WebDriver driver = browser.getWebDriver();
        WebElement webElement = driver.findElement(By.xpath(xpath));
        return webElement;
    }

    public boolean existElement(String xpath) {
        WebDriver driver = browser.getWebDriver();
        boolean existElement = true;
        try{
            driver.findElement(By.xpath(xpath));
        } catch (NoSuchElementException e) {
            existElement = false;
        }
        return existElement;
    }

    public Object[] getProfessorAndQuestions(String browserType) {
        List<ProfessorElement> professorElementsList = new ArrayList<>();
        List<QuestionElement> questionElementsList = new ArrayList<>();
        browser = buildConcreteBrowser(browserType);
        WebDriver driver = browser.getWebDriver();
        WebDriverWait wait = browser.getWebDriverWait();
        navigateInLoginPage();
        List<WebElement> professorElements = findElements(PROFESSOR_ELEMENT_XPATH);
        int professorWebPosition = 1;
        for(WebElement webElement: professorElements){
            ProfessorElement professorElement = new ProfessorElement();
            professorElement.setProfessor( webElement.getText() );
            professorElement.setPositionWebElement(professorWebPosition++);
            professorElementsList.add(professorElement);
        }
        professorElements.get(0).click();
        do {
            WebElement questionWebElement = findElement(QUESTION_ELEMENT_XPATH);
            QuestionElement questionElement = new QuestionElement();
            questionElement.setQuestion( questionWebElement.getText() );
            List<WebElement> optionsElements = findElements(QUESTION_OPTION_ELEMENT_XPATH);
            List<AnswerElement> answerElementsList = new ArrayList<>();
            if(optionsElements.size() > SIZE_NUMBER_SINGLE_QUESTION) {
                questionElement.setQuestionType(QuestionType.MULTIPLE);
            } else {
                questionElement.setQuestionType(QuestionType.SINGLE);
            }
            int positionAnswerWebElement = 1;
            for(WebElement webElement: optionsElements) {
                AnswerElement answerElement = new AnswerElement();
                answerElement.setAnswer( webElement.getText() );
                answerElement.setElementPosition(positionAnswerWebElement);
                answerElementsList.add(answerElement);
                positionAnswerWebElement++;
            }
            questionElement.setAnswerElements(answerElementsList);
            System.out.println(questionElement);
            questionElementsList.add(questionElement);
            wait.until( ExpectedConditions.elementToBeClickable( By.xpath(BUTTON_NEXT_ELEMENT_XPATH) ) ).click();
        } while ( !existElement(POPUP_ELEMENT_XPATH) );
        Object[] professorAndQuestionArray = new Object[2];
        professorAndQuestionArray[0] = professorElementsList;
        professorAndQuestionArray[1] = questionElementsList;
        driver.close();
        return professorAndQuestionArray;
    }

    public void evaluateOptions(String browserType, HashMap<String, AnswerElement> selectedAnswerHashMap, String professorName) {
        browser = buildConcreteBrowser(browserType);
        WebDriver driver = browser.getWebDriver();
        WebDriverWait wait = browser.getWebDriverWait();
        navigateInLoginPage();
        wait.until( ExpectedConditions.elementToBeClickable( By.xpath( PROFESSOR_WITH_ID_SELECTION_XPATH.replace("?",professorName) ) ) ).click();
        selectedAnswerHashMap.forEach( (key, value) -> System.out.println(key));
        do {
            String pregunta = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( QUESTION_ELEMENT_XPATH ) ) ).getText();
            if( selectedAnswerHashMap.get(pregunta) != null ) {
                String xpath = SELECTABLE_RADIO_OR_CHECKBUTTON_XPATH.replace("?", String.valueOf( selectedAnswerHashMap.get(pregunta).getElementPosition() ) );
                wait.until( ExpectedConditions.elementToBeClickable( By.xpath(xpath) ) ).click();
            }
            wait.until( ExpectedConditions.elementToBeClickable(By.xpath( BUTTON_NEXT_ELEMENT_XPATH) ) ).click();
        } while( !existElement(POPUP_ELEMENT_XPATH) );
        driver.close();
    }

    private Browser buildConcreteBrowser(String browserType) {
        BrowserConstructor browserConstructor = new BrowserConstructor();
        if( browserType.equals( BrowserType.CHROME ) ) {
            browserConstructor.setBrowserBuilder( new ChromeBrowserBuilder() );
        } else if( browserType.equals( BrowserType.FIREFOX ) ) {
            browserConstructor.setBrowserBuilder( new FirefoxBrowserBuilder() );
        } else if( browserType.equals( BrowserType.SAFARI ) ) {
            browserConstructor.setBrowserBuilder( new SafariBrowserBuilder() );
        }
        browserConstructor.createBrowserInstance();
        System.setProperty( browserConstructor.getBrowser().getDriver(), browserConstructor.getBrowser().getDriverPath() );
        browserConstructor.initializeWebDriver();
        Browser browser = browserConstructor.getBrowser();
        return browser;
    }

}
