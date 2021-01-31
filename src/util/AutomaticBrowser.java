package util;

import browser.Browser;
import browser.BrowserConstructor;
import browser.ChromeBrowserBuilder;
import browser.FirefoxBrowserBuilder;
import browser.SafariBrowserBuilder;
import evaluation.AnswerElement;
import evaluation.OpenAnswerElement;
import evaluation.QuestionElement;
import evaluation.QuestionType;
import evaluation.TableAnswerElement;
import exceptions.BrowserErrorException;
import exceptions.ElementNotFoundException;
import exceptions.IncorrectUserException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutomaticBrowser {
    private final int SIZE_NUMBER_SINGLE_QUESTION;
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
    private final String QUESTION_FORM_XPATH;
    private final String QUESTION_TEXTAREA_XPATH;
    private final String QUESTION_OPTION_TABLE_ELEMENT_XPATH;
    private final String QUESTION_INSTRUCTIONS_TABLE_ELEMENT_XPATH;
    private final String SIZE_QUESTIONNAIRE_XPATH;
    private final String SPAN_VALIDATION_ERROR_XPATH;
    private final String QUESTION_TYPE_SINGLE_XPATH;
    private final String QUESTION_TYPE_MULTIPLE_XPATH;
    private final String QUESTION_TYPE_OPEN_XPATH;
    private final String QUESTION_TYPE_TABLE_XPATH;
    private final String ACCOUNT;
    private final String PASSWORD;
    private Browser browser;

    public AutomaticBrowser(String account, String password) {
        AutomaticBrowserProperties automaticBrowserProperties = new AutomaticBrowserProperties();
        URL = automaticBrowserProperties.getUrl();
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
        SIZE_NUMBER_SINGLE_QUESTION = Integer.parseInt(automaticBrowserProperties.getSizeNumberElementXPath());
        SIZE_QUESTIONNAIRE_XPATH = automaticBrowserProperties.getSizeQuestionnarieXPath();
        SPAN_VALIDATION_ERROR_XPATH = automaticBrowserProperties.getSpanFieldValidationErrorXPath();
        QUESTION_FORM_XPATH = automaticBrowserProperties.getQuestionFormElementXpath();
        QUESTION_TEXTAREA_XPATH = automaticBrowserProperties.getTextAreaElementXpath();
        QUESTION_TYPE_SINGLE_XPATH = automaticBrowserProperties.getSingleQuestionTypeElementXpath();
        QUESTION_TYPE_MULTIPLE_XPATH = automaticBrowserProperties.getMultipleQuestionTypeElementXpath();
        QUESTION_TYPE_TABLE_XPATH = automaticBrowserProperties.getTableQuestionTypeElementXpath();
        QUESTION_TYPE_OPEN_XPATH = automaticBrowserProperties.getOpenedQuestionTypeElementXpath();
        QUESTION_OPTION_TABLE_ELEMENT_XPATH = automaticBrowserProperties.getQuestionOptionTableElementXpath();
        QUESTION_INSTRUCTIONS_TABLE_ELEMENT_XPATH = automaticBrowserProperties.getQuestionInstructionTableElementXpath();
        this.ACCOUNT = account;
        this.PASSWORD = password;
    }

    public List<QuestionElement> getQuestions(String browserType) throws BrowserErrorException, IncorrectUserException, ElementNotFoundException {
        List<QuestionElement> questionsList = new ArrayList<>();
        browser = buildConcreteBrowser(browserType);
        WebDriver driver = browser.getWebDriver();
        WebDriverWait wait = browser.getWebDriverWait();
        navigateInLoginPage();
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(BUTTON_NEXT_ELEMENT_XPATH)));
        } catch (NoSuchElementException | TimeoutException e) {
            driver.close();
            throw new ElementNotFoundException("No se encontró opciones para evaluar.");
        }
        int sizeQuestionnaire = getCuestionnaireSize();
        List<WebElement> optionsElements;
        do {
            WebElement questionWebElement = findElement(QUESTION_ELEMENT_XPATH);
            QuestionElement questionElement = new QuestionElement();
            questionElement.setQuestion(questionWebElement.getText());
            if (findElement(QUESTION_TYPE_SINGLE_XPATH) != null) {
                questionElement.setQuestionType(QuestionType.SINGLE);
                optionsElements = findElements(QUESTION_OPTION_ELEMENT_XPATH);
                questionElement.setAnswerElements(getSimpleAnswersElements(optionsElements));
            } else if (findElement(QUESTION_TYPE_MULTIPLE_XPATH) != null) {
                questionElement.setQuestionType(QuestionType.MULTIPLE);
                optionsElements = findElements(QUESTION_OPTION_ELEMENT_XPATH);
                questionElement.setAnswerElements(getSimpleAnswersElements(optionsElements));
            } else if (findElement(QUESTION_TYPE_TABLE_XPATH) != null) {
                questionElement.setQuestionType(QuestionType.TABLE);
                optionsElements = findElements(QUESTION_OPTION_TABLE_ELEMENT_XPATH);
                questionElement.setTableAnswerElements(getTableAnswersElements(optionsElements));
            } else if (findElement(QUESTION_TYPE_OPEN_XPATH) != null) {
                questionElement.setQuestionType(QuestionType.OPEN);
                optionsElements = findElements(QUESTION_TYPE_OPEN_XPATH);
                questionElement.setOpenAnswerElement( getOpenAnswerElement(optionsElements));
            }
            questionsList.add(questionElement);
            System.out.println(questionElement);
            WebElement nextButtonWebElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(BUTTON_NEXT_ELEMENT_XPATH)));
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", nextButtonWebElement);
            sizeQuestionnaire--;
        } while (sizeQuestionnaire > 0);
        driver.close();
        return questionsList;
    }

    private List<AnswerElement> getSimpleAnswersElements(List<WebElement> optionsElements) {
        List<AnswerElement> answerElementsList = new ArrayList<>();
        int positionAnswerWebElement = 1;
        for (WebElement webElement : optionsElements) {
            AnswerElement answerElement = new AnswerElement();
            answerElement.setInstruction(webElement.getText());
            answerElement.setElementPosition(positionAnswerWebElement);
            answerElementsList.add(answerElement);
            positionAnswerWebElement++;
        }
        return answerElementsList;
    }

    private TableAnswerElement getTableAnswersElements(List<WebElement> optionsElements) {
        TableAnswerElement tableAnswerElement = new TableAnswerElement();
        LinkedHashMap<String, List<AnswerElement>> rows = new LinkedHashMap<>();
        tableAnswerElement.setRows(rows);
        List<WebElement> subInstructionsTable = findElements(QUESTION_INSTRUCTIONS_TABLE_ELEMENT_XPATH);
        for (WebElement webElement : optionsElements) {
            List<AnswerElement> answersToRow = new ArrayList<>();
            for (int i = 1; i < subInstructionsTable.size(); i++) {
                AnswerElement answerElement = new AnswerElement();
                answerElement.setInstruction(subInstructionsTable.get(i).getText());
                answerElement.setElementPosition(i);
                answersToRow.add(answerElement);
            }
            rows.put(webElement.getText(), answersToRow);
        }
        return tableAnswerElement;
    }

    private OpenAnswerElement getOpenAnswerElement(List<WebElement> optionsElements) {
        OpenAnswerElement openAnswerElement = null;
        for (WebElement webElement : optionsElements) {
            openAnswerElement = new OpenAnswerElement();
            openAnswerElement.setInstruction( webElement.getText() );
        }
        return openAnswerElement;
    }

    public void evaluateOptions(String browserType, HashMap<String, List<AnswerElement>> selectedAnswerHashMap) throws BrowserErrorException, IncorrectUserException, ElementNotFoundException {
        browser = buildConcreteBrowser(browserType);
        WebDriver driver = browser.getWebDriver();
        WebDriverWait wait = browser.getWebDriverWait();
        navigateInLoginPage();
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(BUTTON_NEXT_ELEMENT_XPATH)));
        } catch (NoSuchElementException | TimeoutException e) {
            driver.close();
            throw new ElementNotFoundException("No se encontró opciones para evaluar.");
        }
        int sizeQuestionnaire = getCuestionnaireSize();
        do {
            String question = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(QUESTION_ELEMENT_XPATH))).getText();
            if (selectedAnswerHashMap.get(question) != null) {
                List<AnswerElement> selectedAnswer = selectedAnswerHashMap.get(question);
                for (AnswerElement answerElement : selectedAnswer) {
                    String xpath = SELECTABLE_RADIO_OR_CHECKBUTTON_XPATH.replace("?", String.valueOf(answerElement.getElementPosition()));
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))).click();
                }
            }
            WebElement nextButtonWebElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(BUTTON_NEXT_ELEMENT_XPATH)));
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", nextButtonWebElement);
            sizeQuestionnaire--;
        } while (sizeQuestionnaire > 0);

        driver.close();
    }

    private void navigateInLoginPage() throws IncorrectUserException {
        WebDriver driver = browser.getWebDriver();
        driver.get(URL);
        driver.findElement(By.xpath(USERNAME_ELEMENT_XPATH)).sendKeys(ACCOUNT);
        driver.findElement(By.xpath(PASSWORD_ELEMENT_XPATH)).sendKeys(PASSWORD);
        driver.findElement(By.xpath(BUTTON_LOGIN_ELEMENT_XPATH)).click();
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            Logger.getLogger(AutomaticBrowser.class.getName()).log(Level.SEVERE, null, e);
        }
        if (findElement(SPAN_VALIDATION_ERROR_XPATH) != null) {
            driver.close();
            throw new IncorrectUserException("El usuario o la contraseña son incorrectos");
        }
    }

    private boolean existElement(String xpath) {
        WebDriverWait wait = browser.getWebDriverWait();
        boolean existElement = true;
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
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
        try {
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
        if (browserType.equals(BrowserType.CHROME)) {
            browserConstructor.setBrowserBuilder(new ChromeBrowserBuilder());
        } else if (browserType.equals(BrowserType.FIREFOX)) {
            browserConstructor.setBrowserBuilder(new FirefoxBrowserBuilder());
        } else if (browserType.equals(BrowserType.SAFARI)) {
            browserConstructor.setBrowserBuilder(new SafariBrowserBuilder());
        }
        browserConstructor.createBrowserInstance();
        System.setProperty(browserConstructor.getBrowser().getDriver(), browserConstructor.getBrowser().getDriverPath());
        browserConstructor.initializeWebDriver();
        Browser browser = browserConstructor.getBrowser();
        return browser;
    }

}
