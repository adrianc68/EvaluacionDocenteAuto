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
import exceptions.BrowserErrorException;
import exceptions.ElementNotFoundException;
import exceptions.IncorrectUserException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.NoSuchElementException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private final String SIZE_QUESTIONNAIRE_XPATH;
    private final String SPAN_VALIDATION_ERROR_XPATH;
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
        SIZE_QUESTIONNAIRE_XPATH = automaticBrowserProperties.getSizeQuestionnarieXPath();
        SPAN_VALIDATION_ERROR_XPATH = automaticBrowserProperties.getSpanFieldValidationErrorXPath();
        this.ACCOUNT = account;
        this.PASSWORD = password;
    }

    public List<ProfessorElement> getProfessor(String browserType) throws BrowserErrorException, IncorrectUserException, ElementNotFoundException {
        List<ProfessorElement> professorsList = new ArrayList<>();
        browser = buildConcreteBrowser(browserType);
        WebDriver driver = browser.getWebDriver();
        navigateInLoginPage();
        if( !existElement(PROFESSOR_ELEMENT_XPATH) ){
            driver.close();
            throw new ElementNotFoundException("¡No se ha encontrado la lista de profesores!");
        }
        List<WebElement> professorElements = findElements(PROFESSOR_ELEMENT_XPATH);
        int professorWebPosition = 1;
        for(WebElement webElement: professorElements){
            ProfessorElement professorElement = new ProfessorElement();
            professorElement.setProfessor( webElement.getText() );
            professorElement.setPositionWebElement(professorWebPosition++);
            professorsList.add(professorElement);
        }
        driver.close();
        return professorsList;
    }

    public List<QuestionElement> getQuestions(String browserType) throws BrowserErrorException, IncorrectUserException, ElementNotFoundException {
        List<QuestionElement> questionsList = new ArrayList<>();
        browser = buildConcreteBrowser(browserType);
        WebDriver driver = browser.getWebDriver();
        WebDriverWait wait = browser.getWebDriverWait();
        navigateInLoginPage();
        if( !existElement(PROFESSOR_ELEMENT_XPATH) ){
            driver.close();
            throw new ElementNotFoundException("¡No se ha encontrado la lista de profesores!");
        }
        List<WebElement> professorWebElements = findElements(PROFESSOR_ELEMENT_XPATH);
        professorWebElements.get(0).click();
        int sizeQuestionnaire = getCuestionnaireSize();
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
            questionsList.add(questionElement);
            WebElement nextButtonWebElement =  wait.until( ExpectedConditions.elementToBeClickable(By.xpath( BUTTON_NEXT_ELEMENT_XPATH) ) );
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", nextButtonWebElement);
            sizeQuestionnaire--;
        } while ( sizeQuestionnaire > 0 );
        driver.close();
        return questionsList;
    }

    public void evaluateOptions(String browserType, HashMap<String, List<AnswerElement>> selectedAnswerHashMap, String professorName) throws BrowserErrorException, IncorrectUserException {
        browser = buildConcreteBrowser(browserType);
        WebDriver driver = browser.getWebDriver();
        WebDriverWait wait = browser.getWebDriverWait();
        navigateInLoginPage();
        if( existElement (PROFESSOR_WITH_ID_SELECTION_XPATH.replace("?", professorName) ) ) {
            wait.until( ExpectedConditions.elementToBeClickable( By.xpath( PROFESSOR_WITH_ID_SELECTION_XPATH.replace("?",professorName) ) ) ).click();
            int sizeQuestionnaire = getCuestionnaireSize();
            do {
                String question = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( QUESTION_ELEMENT_XPATH ) ) ).getText();
                if( selectedAnswerHashMap.get(question) != null ) {
                    List<AnswerElement> selectedAnswer = selectedAnswerHashMap.get(question);
                    for(AnswerElement answerElement: selectedAnswer) {
                        String xpath = SELECTABLE_RADIO_OR_CHECKBUTTON_XPATH.replace("?", String.valueOf( answerElement.getElementPosition() ) );
                        wait.until( ExpectedConditions.elementToBeClickable( By.xpath(xpath) ) ).click();
                    }
                }
                WebElement nextButtonWebElement =  wait.until( ExpectedConditions.elementToBeClickable(By.xpath( BUTTON_NEXT_ELEMENT_XPATH) ) );
                JavascriptExecutor executor = (JavascriptExecutor) driver;
                executor.executeScript("arguments[0].click();", nextButtonWebElement);
                sizeQuestionnaire--;
            } while( sizeQuestionnaire > 0);
        }
        driver.close();
    }

    private void navigateInLoginPage() throws IncorrectUserException {
        WebDriver driver = browser.getWebDriver();
        driver.get(URL);
        driver.findElement( By.xpath(USERNAME_ELEMENT_XPATH) ).sendKeys(ACCOUNT);
        driver.findElement( By.xpath(PASSWORD_ELEMENT_XPATH) ).sendKeys(PASSWORD);
        driver.findElement( By.xpath(BUTTON_LOGIN_ELEMENT_XPATH) ).click();
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            Logger.getLogger( AutomaticBrowser.class.getName() ).log(Level.SEVERE, null, e);
        }
        if( findElement(SPAN_VALIDATION_ERROR_XPATH) != null ) {
            driver.close();
            throw new IncorrectUserException("El usuario o la contraseña son incorrectos");
        }
    }

    private boolean existElement(String xpath) {
        WebDriverWait wait = browser.getWebDriverWait();
        boolean existElement = true;
        try{
            wait.until( ExpectedConditions.elementToBeClickable( By.xpath(xpath) ) );
        } catch (NoSuchElementException | TimeoutException e) {
            return false;
        }
        return existElement;
    }

    private List<WebElement> findElements(String xpath) {
        WebDriver driver = browser.getWebDriver();
        List<WebElement> list = driver.findElements(By.xpath(xpath));
        return list;
    }

    private WebElement findElement(String xpath) {
        WebDriver driver = browser.getWebDriver();
        WebElement webElement;
        try{
            webElement = driver.findElement(By.xpath(xpath));
        } catch (NoSuchElementException e) {
            return null;
        }
        return webElement;
    }

    private int getCuestionnaireSize() {
        int size = 0;
        size = findElements(SIZE_QUESTIONNAIRE_XPATH).size();
        return size;
    }

    private Browser buildConcreteBrowser(String browserType) throws BrowserErrorException {
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
